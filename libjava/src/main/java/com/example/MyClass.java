package com.example;

public class MyClass {
    public static void main(String[] args){
        int[] data = new int[]{12,13,15,3,2,8,7,6};
        bubble_sort(data);
        for(int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
    }

    private static void bubble_sort(int[] arr) {
        int trans=0;
        for (int i=0;i<arr.length-1;i++){
            for (int j=1;j<arr.length-i;j++){
                if (arr[j-1]>arr[j]){
                    trans=arr[j-1];
                    arr[j-1]=arr[j];
                    arr[j]=trans;
                }
            }
        }
    }
}
