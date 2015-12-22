package com.algorithm.cryptography;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * @author Chris, Z
 * @date Aug 14, 2012 4:36:22 PM
 */
public enum Base64Type {

    GRACE(new IBase64() {
        public char[] encode(byte[] input) {
            int length = input.length;
            char[] output = new char[(int) Math.ceil(length / 3.0) * 4];
            for (int i = 0, j = 0; i < length; i += 3, j += 4) {
                boolean trip = false, quad = false;
                int val = 0xff & input[i], bits = 0x3f;
                val <<= 8;
                if (i + 1 < length) {
                    val |= 0xff & input[i + 1];
                    trip = true;
                }
                val <<= 8;
                if (i + 2 < length) {
                    val |= 0xff & input[i + 2];
                    quad = true;
                }
                output[j + 3] = alphabet[quad ? (bits & val) : 64];
                val >>= 6;
                output[j + 2] = alphabet[trip ? (bits & val) : 64];
                val >>= 6;
                output[j + 1] = alphabet[bits & val];
                val >>= 6;
                output[j] = alphabet[bits & val];
            }
            return output;
        }

        public byte[] decode(char[] input) {
            if (input.length % 4 != 0) throw new InvalidParameterException("Invalid length of array : " + input.length);
            int length = input.length / 4 * 3;
            if (input.length > 0 && input[input.length - 1] == '=') length--;
            if (input.length > 1 && input[input.length - 2] == '=') length--;
            byte[] output = new byte[length];
            int offset = 0, val = 0x00, index = 0;
            for (int i = 0; i < input.length; i++) {
                int code = codes[input[i]];
                if (code >= 0) {
                    val <<= 6;
                    val |= code;
                    offset += 6;
                    if (offset >= 8) {
                        offset -= 8;
                        output[index++] = (byte) ((val >> offset) & 0xff);
                    }
                }
            }
            return output;
        }
    }),

    SIMPLE(new IBase64() {
        public char[] encode(byte[] input) {
            int count = input.length / 3;
            int rm = input.length % 3;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < count; i++) {
                int index = i * 3;
                baos.write(alphabet[(input[index] & 0xfc) >> 2]);
                baos.write(alphabet[((input[index] & 0x03) << 4 | (input[index + 1] & 0xf0) >> 4)]);
                baos.write(alphabet[(input[index + 1] & 0x0f) << 2 | (input[index + 2] & 0xc0) >> 6]);
                baos.write(alphabet[input[index + 2] & 0x3f]);
            }
            if (rm == 1) {
                baos.write(alphabet[(input[input.length - 1] & 0xfc) >> 2]);
                baos.write(alphabet[(input[input.length - 1] & 0x03) << 4]);
                baos.write('=');
                baos.write('=');
            } else if (rm == 2) {
                baos.write(alphabet[(input[input.length - 2] & 0xfc) >> 2]);
                baos.write(alphabet[(input[input.length - 2] & 0x03) << 4 | (input[input.length - 1] & 0xf0) >> 4]);
                baos.write(alphabet[(input[input.length - 1] & 0x0f) << 2]);
                baos.write('=');
            }
            return baos.toString().toCharArray();
        }

        public byte[] decode(char[] input) {
            if (input.length % 4 != 0) throw new InvalidParameterException("Invalid length of array : " + input.length);
            int count = input.length / 4;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < count - 1; i++) {
                int index = i * 4;
                baos.write((codes[input[index]] & 0x3f) << 2 | (codes[input[index + 1]] & 0x30) >> 4);
                baos.write((codes[input[index + 1]] & 0x0f) << 4 | (codes[input[index + 2]] & 0x3c) >> 2);
                baos.write((codes[input[index + 2]] & 0x03) << 6 | codes[input[index + 3]] & 0x3f);
            }
            if (input[input.length - 2] == '=') {
                baos.write((codes[input[input.length - 4]] & 0x3f) << 2 | ((codes[input[input.length - 3]] & 0x30) >> 4));
            } else if (input[input.length - 1] == '=') {
                baos.write((codes[input[input.length - 4]] & 0x3f) << 2 | ((codes[input[input.length - 3]] & 0x30) >> 4));
                baos.write((codes[input[input.length - 3]] & 0x0f) << 4 | ((codes[input[input.length - 2]] & 0x3c)) >> 2);
            }
            return baos.toByteArray();
        }
    }),

    OFFICIAL(new IBase64() {

        @Override
        public char[] encode(byte[] code) {
            return encoder.encode(code).toCharArray();
        }

        @Override
        public byte[] decode(char[] code) {
            try {
                return decoder.decodeBuffer(new String(code));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private BASE64Encoder encoder = new BASE64Encoder();
        private BASE64Decoder decoder = new BASE64Decoder();
    });

    Base64Type(IBase64 base64) {
        this.base64 = base64;
    }

    private IBase64 base64;

    public char[] encode(byte[] code) {
        return this.base64.encode(code);
    }

    public byte[] decode(char[] code) {
        return this.base64.decode(code);
    }

    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static byte[] codes = new byte[128];

    static {
        for (int i = 0; i < codes.length; i++) {
            codes[i] = -1;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            codes[i] = (byte) (i - 'A');
        }
        for (int i = 'a'; i <= 'z'; i++) {
            codes[i] = (byte) (26 + i - 'a');
        }
        for (int i = '0'; i <= '9'; i++) {
            codes[i] = (byte) (52 + i - '0');
        }
        codes['+'] = 62;
        codes['/'] = 63;
    }
}
