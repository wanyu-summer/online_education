package com.project.guli.service.edu;

import java.util.*;

/**
 * @author wan
 * @create 2020-08-02-19:14
 */
public class Main {
    public static void main(String[] args) {
        Scanner src = new Scanner(System.in);
        int k = src.nextInt();
        int n = src.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = src.nextInt();
        }
        int backNum = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (i < n - 1 && sum == k) {
                System.out.print("paradox");
                break;
            }
            if (sum > k) {
                backNum++;
                sum = 2 * k - sum;
            }
        }
        if (sum == k) {
            System.out.print(0 + " " + backNum);
        } else if (sum < k) {
            System.out.println((k - sum) + " " + backNum);
        }else{
            backNum++;
            System.out.println((sum - k)+" "+backNum);
        }

    }
}
