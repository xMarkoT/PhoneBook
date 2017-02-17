package com.algorithms.sort;

import java.util.Random;

public class FindBiggestInteger {

	public static void main(String[] args) {
		int[] array = new int[10_000_000];
		int[] populatedArray = populateIntegerArray(array);
		
		long start = System.currentTimeMillis();
        long end = start + 1000; // 1000 ms/sec
        int counter = 0;
        while (System.currentTimeMillis() < end) {
            counter += getBiggestInt(populatedArray);
        }
        System.out.println(counter);

	}
	
	public static int getBiggestInt(int[] arr){
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			if(max < arr[i]) max = arr[i];
		}
		return 1;
	}
	
	private static int[] populateIntegerArray(int[] array) {
		int maxInt =  Integer.MAX_VALUE;

		Random random = new Random();

		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt(maxInt);
		}

		return array;
	}
}
