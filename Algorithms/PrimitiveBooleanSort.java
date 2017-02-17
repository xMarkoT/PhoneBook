package com.booleansort;

import java.util.Random;

public class PrimitiveBooleanSort {

	public static void main(String[] args) {
		boolean[] array = populatePrimitiveBooleanArray(20);
		boolean[] sorted = sortArrayOfPrimitiveBooleans(array, true);
		for (int i = 0; i < sorted.length; i++) {
			System.out.print(sorted[i] + " ");
		}
	}

	private static boolean[] sortArrayOfPrimitiveBooleans(boolean[] array, boolean sortTrueFirst) {
		int c = 0;
		int length = array.length;
		for (int i = 0; i < length; i++) {
			if(array[i] == sortTrueFirst){
				c++;
			}
		}
		for (int i = 0; i < length; i++) {
			if (c > 0) {
				array[i] = sortTrueFirst;
				c--;
			} else {
				array[i] = !sortTrueFirst;
			}
		}
		return array;
	}

	private static boolean[] populatePrimitiveBooleanArray(int arrayLength) {
		boolean[] array = new boolean[arrayLength];
		Random random = new Random();
		for (int i = 0; i < arrayLength; i++) {
			array[i] = random.nextInt(2) == 1 ? true : false;
		}
		return array;
	}
}
