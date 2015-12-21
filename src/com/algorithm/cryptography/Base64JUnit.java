package com.algorithm.cryptography;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author Chris, Z
 * @date Aug 14, 2012 1:44:09 PM
 */
public class Base64JUnit {
	
	@Test
	public void grace() {
		test(Base64Type.GRACE);
	}
	
	@Test
	public void simple() {
		test(Base64Type.SIMPLE);
	}
	
	@Test
	public void official() {
		test(Base64Type.OFFICIAL);
	}

	private void test(Base64Type type) {
		String input = "keep calm and carry on";
		char[] encode = type.encode(input.getBytes());
		byte[] decode = type.decode(encode);
		System.out.printf("%-8s(%s): ", type.name(), Arrays.equals(input.getBytes(), decode));
		System.out.println(new String(encode));
	}
}
