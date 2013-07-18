/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.util;

import java.util.Arrays;
import java.util.Random;
public class ArrayTest {

	public static long[] createRandomArray(int size) {
		long[] result = new long[size];
		Random random = new Random();

		for (int i = 0; i < result.length; i++) {
			result[i] = random.nextLong();
		}

		return result;
	}

	public static long[] filterAll(long[] input) {
		for (long l : input) {
			input = ArrayUtil.remove(input, l);
		}

		return input;
	}

	public static long[] filterEven(long[] input) {
		for (long l : input) {
			if (l % 2 == 0) {
				input = ArrayUtil.remove(input, l);
			}
		}

		return input;
	}

	public static long[] filterNone(long[] input) {
		for (long l : input) {
			if (l % 2 == 0) {
				input = input;
			}
		}

		return input;
	}

	public static void main(String[] args) {

		long[] numbers = createRandomArray(3);
		System.out.println(Arrays.toString(numbers));

		/* WARM UP */

		Even even = new Even();
		All allp = new All();
		None nonep = new None();

		for (int i = 0; i < 100000; i++) {
			long[] filtered = ArrayUtil.filter(numbers, even);
			long[] filtered2 = filterEven(numbers);
			long[] none = ArrayUtil.filter(numbers, allp);
			long[] none2 = filterAll(numbers);
			long[] all = ArrayUtil.filter(numbers, nonep);
			long[] all2 = filterNone(numbers);
		}

		int[] sizes = new int[] {2, 5, 10, 15, 20, 25, 30, 100};

		/* MEASURE */
		int repeats = 100 * 1000;

		for (int size : sizes) {
			System.out.println("Comparing filterEven with size: " + size);
			long[] randomArray = createRandomArray(size);
			long[] f1 = null;
			long[] f2 = null;
			long t1 = System.currentTimeMillis();

			for (int j=0; j<repeats; j++) {
				Even predicate = new Even();
				f1 = ArrayUtil.filter(randomArray, predicate);
			}

			long t2 = System.currentTimeMillis();

			System.out.println("NEWE("+size+"): " + (t2 - t1));
			long n1 = System.currentTimeMillis();

			for (int j=0; j<repeats; j++) {
				f2 = filterEven(randomArray);
			}

			long n2 = System.currentTimeMillis();
			System.out.println("OLDE("+size+"): " + (n2 - n1));

			if (!Arrays.equals(f1, f2)) {
				throw new RuntimeException("Not equal");
			}
		}

		for (int size : sizes) {
			System.out.println("Comparing filterAll with size: " + size);
			long[] randomArray = createRandomArray(size);
			long[] f1 = null;
			long[] f2 = null;
			long t1 = System.currentTimeMillis();

			for (int j=0; j<repeats; j++) {
				Predicate<Long> predicate = new All();
				f1 = ArrayUtil.filter(randomArray, predicate);
			}

			long t2 = System.currentTimeMillis();

			System.out.println("NEWA("+size+"): " + (t2 - t1));
			long n1 = System.currentTimeMillis();

			for (int j=0; j<repeats; j++) {
				f2 = filterAll(randomArray);
			}

			long n2 = System.currentTimeMillis();
			System.out.println("OLDA("+size+"): " + (n2 - n1));

			if (!Arrays.equals(f1, f2)) {
				throw new RuntimeException("Not equal");
			}
		}

//		for (int size : sizes) {
//			System.out.println("Comparing filterNone with size: " + size);
//			long[] randomArray = createRandomArray(size);
//			long[] f1 = null;
//			long[] f2 = null;
//			long t1 = System.currentTimeMillis();
//
//			for (int j=0; j<repeats; j++) {
//				Predicate<Long> predicate = new None();
//				f1 = ArrayUtil.filter(randomArray, predicate);
//			}
//
//			long t2 = System.currentTimeMillis();
//
//			System.out.println("NEWN("+size+"): " + (t2 - t1));
//			long n1 = System.currentTimeMillis();
//
//			for (int j=0; j<repeats; j++) {
//				f2 = filterNone(randomArray);
//			}
//
//			long n2 = System.currentTimeMillis();
//			System.out.println("OLDN("+size+"): " + (n2 - n1));
//
//			if (!Arrays.equals(f1, f2)) {
//				throw new RuntimeException("Not equal");
//			}
//		}
	}

	public static class All implements Predicate<Long> {
		@Override
		public boolean keep(Long item) {
			return false;
		}
	};

	public static class Even implements Predicate<Long> {
		@Override
		public boolean keep(Long item) {
			if (item % 2 == 0) {
				return false;
			}

			return true;
		}
	};

	public static class None implements Predicate<Long> {
		@Override
		public boolean keep(Long item) {
			boolean even = (item % 2 == 0);
			return true;
		}
	};

}