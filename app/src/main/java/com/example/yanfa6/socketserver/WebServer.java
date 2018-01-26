package com.example.yanfa6.socketserver;

import android.util.Log;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yanfa6 on 2017/3/13.
 */
public class WebServer extends Thread{
    int port;

    public WebServer(int port){
        this.port=port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket=null;
        boolean isLoop;
        try {
            //创建服务器套接字
            serverSocket=new ServerSocket(port);
            //创建http协议拦截器
            BasicHttpProcessor httpproc=new BasicHttpProcessor();
            //增加http协议拦截器
            httpproc.addInterceptor(new ResponseDate());
            httpproc.addInterceptor(new ResponseServer());
            httpproc.addInterceptor(new ResponseContent());
            httpproc.addInterceptor(new ResponseConnControl());
            //创建http服务
            HttpService httpService=new HttpService(httpproc,new DefaultConnectionReuseStrategy(),new DefaultHttpResponseFactory());
            //创建http参数
            HttpParams params=new BasicHttpParams();
            params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                    .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,8 * 1024)
                    .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                    .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                    .setParameter(CoreProtocolPNames.ORIGIN_SERVER,"WebServer/1.1");
            //设置http参数
            httpService.setParams(params);
            // 创建HTTP请求执行器注册表
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();

//            reqistry.register("*"+"CameraCtrl", new CameraCtrl());
            reqistry.register("*"+"LightCtrl", new LightCtrl());
//            reqistry.register("*"+"TvCtrl", new TvCtrl());
            // 设置HTTP请求执行器
            httpService.setHandlerResolver(reqistry);
            //循环接受客户端的请求
            isLoop=true;
            while (isLoop&&!Thread.interrupted()){
                //接受客户端的连接
                Socket socket=serverSocket.accept();
                DefaultHttpServerConnection conn=new DefaultHttpServerConnection();
                conn.bind(socket,params);
                //派送至工程线程中处理
                Thread t = new WorkerThread(httpService, conn);
                t.setDaemon(true); // 设为守护线程
                t.start();
            }
        } catch (IOException e) {
            isLoop=false;
            e.printStackTrace();
        }finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private class WorkerThread extends Thread {
        private final HttpService httpservice;
        private final HttpServerConnection conn;
        public static final String TAG = "WorkerThread";

        public WorkerThread(final HttpService httpservice, final HttpServerConnection conn) {
            super();
            this.httpservice = httpservice;
            this.conn = conn;
        }


        @Override
        public void run() {
            Log.d(TAG, "HttpServer WorkerThread.run() New connection thread");
            HttpContext context = new BasicHttpContext(null);
            try {
                while (!Thread.interrupted() && this.conn.isOpen()) {
                    Thread ct = Thread.currentThread();
                    String threadName = ct.getName();
                    Log.d(TAG, "WorkerThread.run() Thread.name = " + threadName);
                    this.httpservice.handleRequest(this.conn, context);
                }
            } catch (ConnectionClosedException ex) {
                Log.e(TAG, "HttpServer WorkerThread.run() Client closed connection");
            } catch (IOException ex) {
                Log.e(TAG, "HttpServer WorkerThread.run() I/O error:" + ex.getMessage());
            } catch (HttpException ex) {
                Log.e(TAG, "HttpServer WorkerThread.run() Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
