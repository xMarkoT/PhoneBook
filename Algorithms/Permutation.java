package com.permutation;

public class Permutation {

	public static void main(String[] args) {
		int[] arr = { 1, 2, 3, 4, 5 };
		permutation(arr);
	}

	public static void permutation(int[] arr) {
		String str = "";
		for (int i = 0; i < arr.length; i++) {
			str += arr[i];
		}
		permutation("", str);
	}

	private static void permutation(String prefix, String str) {
		int n = str.length();
		if (n == 0)
			System.out.println(prefix);
		else {
			for (int i = 0; i < n; i++)
				permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
		}
	}

}
