package com.gun.rxjavapractice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RunWith(JUnit4.class)
public class RxUnitTest {
    ReactiveSum reactiveSum;

    @Before
    public void setUp() throws Exception {
        reactiveSum = new ReactiveSum();
    }

    @Test
    public void checkReactiveSumTest() throws InterruptedException {
        //안드로이드 콘솔창 입력 불가 -> editText로 input 받아야 함
        reactiveSum.run();
    }

    @Test
    public void alogTest() {
        String hexNum = "10";
        int deciamlNum = Integer.parseInt(hexNum, 16);
        for (int i = 1; i < 16; i++) {
            System.out.println(hexNum + "*" + Integer.toHexString(i).toUpperCase() + "=" + Integer.toHexString(deciamlNum * i).toUpperCase());
        }
    }

    @Test
    public void algo2Test() {
        int[] arr = {30, 50, 7, 40, 88, 15, 44, 55, 22, 33, 77, 99, 11, 66, 1, 85};

        arr = radixSort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }

    int[] radixSort(int[] arr) {
        for (int shift = Integer.SIZE - 1; shift > -1; shift--) {
            System.out.println(shift);
            int[] tmp = new int[arr.length];
            int j = 0;
            for (int i = 0; i < arr.length; i++) {
                boolean move = arr[i] << shift >= 0;
                if (shift == 0 ? !move : move) {
                    tmp[j] = arr[i];
                    j++;
                } else {
                    arr[i - j] = arr[i];
                }
            }
            for (int i = j; i < tmp.length; i++) {
                tmp[i] = arr[i - j];
            }
            arr = tmp;
        }
        return arr;
    }

    @Test
    public void algoriTest() {
        int X = 20;
        int Y = 2;
        String y = String.valueOf(Y);
        int A = 0, B = 0, C = 1;
        for (int i = 1; i <= X; i++) {
            if (String.valueOf(i).contains(y)) {
                A += 1;
                B = B + i;
                C = C * i;
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(A);
        arrayList.add(B);
        arrayList.add(C);
        System.out.println(arrayList);
    }

    @Test
    public void exampleTest1() {
        int r = 2;
        int g = 3;
        int b = 2;
        int c = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < g; j++) {
                for (int k = 0; k < b; k++) {
                    System.out.printf("%d %d %d\n", i, j, k);
                    c++;
                }
            }
        }
        System.out.printf("%d", c);
    }

    @Test
    public void exampleTest2() {
        int input = 10;
        int sum = 0;
        for (int i = 0; sum < input; i++) {
            sum += i;
        }
        System.out.printf("%d", sum);
    }

    @Test
    public void exampleTest3() {
        int num = 10;
        for (int i = 0; i <= num; i++) {
            if (i % 3 == 0) continue;
            System.out.print(i + " ");
        }
    }

    @Test
    public void exampleTest4() {
        int a = 1;
        int m = -2;
        int d = 1;
        int n = 8;
        for (int i = 1; i < n; i++) {
            a = a * m + d;
        }
        System.out.print(a + " ");
        Scanner scanner = new Scanner(System.in);

    }

    @Test
    public void exampleTest5() {
        int a = 3;
        int b = 7;
        int c = 9;
        int day = 1;
        while (day % a != 0 || day % b != 0 || day % c != 0) {
            day += 1;
        }
        System.out.println(day);
    }

    @Test
    public void exampleTest6() {
        int a = 10;
        int[] b = new int[24];
        for (int i = 1; i <= a; i++) {
//            int num = scan.nextInt();
//            b[num] += 1;
        }
        for (int i = 0; i < 24; i++) {
            System.out.printf("%d ", b[i]);
        }
    }

    @Test
    public void exampleTest7() {
        String[] arg = {"09:05 10", "12:20 5","13:25 6","14:24 5"};

        String currentParam = "12:05";
        int k = 11;
        long currentTime = getTimeValue(currentParam);
        for (int i = 0; i <= arg.length-1; i++) {
        long time = getTimeValue(arg[i]);
        int made = getValueK(arg[i]);
            if (currentTime <= time) {
                k -= made;
                if (k <= 0) {
                    time -= currentTime;
                    System.out.println(time);
                    return;
                }
                if (arg.length-1 == i){
                    System.out.println(-1);
                }
            }
        }
    }



    long getTimeValue(String bakery_schedule) {
        String[] firstPart = bakery_schedule.split(":");
        String[] secondPart = firstPart[1].split(" ");
        String hourStr = firstPart[0];
        String minStr = secondPart[0];
        long timeHour = Long.parseLong(hourStr) * 60;
        long timeMin = Long.parseLong(minStr);
        return timeHour + timeMin;
    }

    int getValueK(String bakery_schedule) {
        String[] firstPart = bakery_schedule.split(":");
        String[] secondPart = firstPart[1].split(" ");
        return Integer.parseInt(secondPart[1]);
    }

    @Test
    public void exampleTest8() {
        Scanner scan = new Scanner(System.in);
        int a = scan.nextInt();
        int[][] ground = new int[20][20];
        for(int i =1; i<=a; i++) {
            ground[scan.nextInt()][scan.nextInt()] = 1;
        }
        for(int i = 1; i<20; i++) {
            for(int j =1; j<20; j++) {
                System.out.print(ground[i][j]);
            }
            System.out.println();
        }
    }

}