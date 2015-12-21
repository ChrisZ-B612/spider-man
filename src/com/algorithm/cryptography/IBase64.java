package com.algorithm.cryptography;

/**
 * @author Chris, Z
 * @date Aug 14, 2012 4:22:47 PM
 */
public interface IBase64 {
	char[] encode(byte[] code);

	byte[] decode(char[] code);
}