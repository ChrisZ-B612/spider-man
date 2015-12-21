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
        public char[] encode(byte[] data) {
            int length = (int) Math.ceil(data.length / 3.0) * 4;
            char[] out = new char[length];
            for (int i = 0, j = 0; i < data.length; i += 3, j += 4) {
                boolean quad = false;
                boolean trip = false;
                int val = 0xff & data[i];
                val <<= 8;
                if (i + 1 < data.length) {
                    val |= 0xff & data[i + 1];
                    trip = true;
                }
                val <<= 8;
                if (i + 2 < data.length) {
                    val |= 0xff & data[i + 2];
                    quad = true;
                }
                out[j + 3] = alphabet[quad ? (0x3f & val) : 64];
                val >>= 6;
                out[j + 2] = alphabet[trip ? (0x3f & val) : 64];
                val >>= 6;
                out[j + 1] = alphabet[0x3f & val];
                val >>= 6;
                out[j] = alphabet[0x3f & val];
            }
            return out;
        }

        public byte[] decode(char[] data) {
            if (data.length % 4 != 0) throw new InvalidParameterException("Invalid length of array : " + data.length);
            int length = data.length / 4 * 3;
            if (data.length > 0 && data[data.length - 1] == '=') length--;
            if (data.length > 1 && data[data.length - 2] == '=') length--;
            byte[] out = new byte[length];
            int shift = 0, valley = 0x00, index = 0;
            for (int i = 0; i < data.length; i++) {
                int value = codes[data[i]];
                if (value >= 0) {
                    valley <<= 6;
                    valley |= value;
                    shift += 6;
                    if (shift >= 8) {
                        shift -= 8;
                        out[index++] = (byte) ((valley >> shift) & 0xff);
                    }
                }
            }
            return out;
        }
    }),

    SIMPLE(new IBase64() {
        public char[] encode(byte[] data) {
            int count = data.length / 3;
            int rm = data.length % 3;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < count; i++) {
                int index = i * 3;
                baos.write(alphabet[(data[index] & 0xfc) >> 2]);
                baos.write(alphabet[((data[index] & 0x03) << 4 | (data[index + 1] & 0xf0) >> 4)]);
                baos.write(alphabet[(data[index + 1] & 0x0f) << 2 | (data[index + 2] & 0xc0) >> 6]);
                baos.write(alphabet[data[index + 2] & 0x3f]);
            }
            if (rm == 1) {
                baos.write(alphabet[(data[data.length - 1] & 0xfc) >> 2]);
                baos.write(alphabet[(data[data.length - 1] & 0x03) << 4]);
                baos.write('=');
                baos.write('=');
            } else if (rm == 2) {
                baos.write(alphabet[(data[data.length - 2] & 0xfc) >> 2]);
                baos.write(alphabet[(data[data.length - 2] & 0x03) << 4 | (data[data.length - 1] & 0xf0) >> 4]);
                baos.write(alphabet[(data[data.length - 1] & 0x0f) << 2]);
                baos.write('=');
            }
            return baos.toString().toCharArray();
        }

        public byte[] decode(char[] data) {
            if (data.length % 4 != 0) throw new InvalidParameterException("Invalid length of array : " + data.length);
            int count = data.length / 4;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < count - 1; i++) {
                int index = i * 4;
                baos.write((codes[data[index]] & 0x3f) << 2 | (codes[data[index + 1]] & 0x30) >> 4);
                baos.write((codes[data[index + 1]] & 0x0f) << 4 | (codes[data[index + 2]] & 0x3c) >> 2);
                baos.write((codes[data[index + 2]] & 0x03) << 6 | codes[data[index + 3]] & 0x3f);
            }
            if (data[data.length - 2] == '=') {
                baos.write((codes[data[data.length - 4]] & 0x3f) << 2 | ((codes[data[data.length - 3]] & 0x30) >> 4));
            } else if (data[data.length - 1] == '=') {
                baos.write((codes[data[data.length - 4]] & 0x3f) << 2 | ((codes[data[data.length - 3]] & 0x30) >> 4));
                baos.write((codes[data[data.length - 3]] & 0x0f) << 4 | ((codes[data[data.length - 2]] & 0x3c)) >> 2);
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
