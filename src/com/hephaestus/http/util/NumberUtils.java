package com.hephaestus.http.util;

import java.util.Formatter;

public class NumberUtils {

	// table to convert a nibble to a hex char.
	private static char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static char[] PARTIAL_ASCII_TABLE = { ' ', // 032 040 020 00100000
														// SP (Space)
			'!', // 033 041 021 00100001 ! (exclamation mark)
			'"', // 034 042 022 00100010 " (double quote)
			'#', // 035 043 023 00100011 # (number sign)
			'$', // 036 044 024 00100100 $ (dollar sign)
			'%', // 037 045 025 00100101 % (percent)
			'&', // 038 046 026 00100110 & (ampersand)
			'\'',// 039 047 027 00100111 ' (single quote)
			'(', // 040 050 028 00101000 ( (left/open parenthesis)
			')', // 041 051 029 00101001 ) (right/closing parenth.)
			'*', // 042 052 02A 00101010 * (asterisk)
			'+', // 043 053 02B 00101011 + (plus)
			',', // 044 054 02C 00101100 , (comma)
			'-', // 045 055 02D 00101101 - (minus or dash)
			'.', // 046 056 02E 00101110 . (dot)
			'/', // 047 057 02F 00101111 / (forward slash)
			'0', // 048 060 030 00110000 0
			'1', // 049 061 031 00110001 1
			'2', // 050 062 032 00110010 2
			'3', // 051 063 033 00110011 3
			'4', // 052 064 034 00110100 4
			'5', // 053 065 035 00110101 5
			'6', // 054 066 036 00110110 6
			'7', // 055 067 037 00110111 7
			'8', // 056 070 038 00111000 8
			'9', // 057 071 039 00111001 9
			':', // 058 072 03A 00111010 : (colon)
			';', // 059 073 03B 00111011 ; (semi-colon)
			'<', // 060 074 03C 00111100 < (less than)
			'=', // 061 075 03D 00111101 = (equal sign)
			'>', // 062 076 03E 00111110 > (greater than)
			'?', // 063 077 03F 00111111 ? (question mark)
			'@', // 064 100 040 01000000 @ (AT symbol)
			'A', // 065 101 041 01000001 A
			'B', // 066 102 042 01000010 B
			'C', // 067 103 043 01000011 C
			'D', // 068 104 044 01000100 D
			'E', // 069 105 045 01000101 E
			'F', // 070 106 046 01000110 F
			'G', // 071 107 047 01000111 G
			'H', // 072 110 048 01001000 H
			'I', // 073 111 049 01001001 I
			'J', // 074 112 04A 01001010 J
			'K', // 075 113 04B 01001011 K
			'L', // 076 114 04C 01001100 L
			'M', // 077 115 04D 01001101 M
			'N', // 078 116 04E 01001110 N
			'O', // 079 117 04F 01001111 O
			'P', // 080 120 050 01010000 P
			'Q', // 081 121 051 01010001 Q
			'R', // 082 122 052 01010010 R
			'S', // 083 123 053 01010011 S
			'T', // 084 124 054 01010100 T
			'U', // 085 125 055 01010101 U
			'V', // 086 126 056 01010110 V
			'W', // 087 127 057 01010111 W
			'X', // 088 130 058 01011000 X
			'Y', // 089 131 059 01011001 Y
			'Z', // 090 132 05A 01011010 Z
			'[', // 091 133 05B 01011011 [ (left/opening bracket)
			'\\',// 092 134 05C 01011100 \ (back slash)
			']', // 093 135 05D 01011101 ] (right/closing bracket)
			'^', // 094 136 05E 01011110 ^ (caret/circumflex)
			'_', // 095 137 05F 01011111 _ (underscore)
			'`', // 096 140 060 01100000 `
			'a', // 097 141 061 01100001 a
			'b', // 098 142 062 01100010 b
			'c', // 099 143 063 01100011 c
			'd', // 100 144 064 01100100 d
			'e', // 101 145 065 01100101 e
			'f', // 102 146 066 01100110 f
			'g', // 103 147 067 01100111 g
			'h', // 104 150 068 01101000 h
			'i', // 105 151 069 01101001 i
			'j', // 106 152 06A 01101010 j
			'k', // 107 153 06B 01101011 k
			'l', // 108 154 06C 01101100 l
			'm', // 109 155 06D 01101101 m
			'n', // 110 156 06E 01101110 n
			'o', // 111 157 06F 01101111 o
			'p', // 112 160 070 01110000 p
			'q', // 113 161 071 01110001 q
			'r', // 114 162 072 01110010 r
			's', // 115 163 073 01110011 s
			't', // 116 164 074 01110100 t
			'u', // 117 165 075 01110101 u
			'v', // 118 166 076 01110110 v
			'w', // 119 167 077 01110111 w
			'x', // 120 170 078 01111000 x
			'y', // 121 171 079 01111001 y
			'z', // 122 172 07A 01111010 z
			'{', // 123 173 07B 01111011 { (left/opening brace)
			'|', // 124 174 07C 01111100 | (vertical bar)
			'}', // 125 175 07D 01111101 } (right/closing brace)
			'~', // 126 176 07E 01111110 ~ (tilde)
	};

	/**
	 * Fast convert a byte array to a hex string with possible leading zero.
	 * Code copied from http://mindprod.com/jgloss/hex.html.
	 * 
	 * @param b
	 *            the byte array to convert.
	 * 
	 * @return a hex string with the same value as the byte array.
	 */
	public static String toHexString(byte[] b) {
		return toHexString(b, 0, b.length);
	}

	/**
	 * Convert a byte-array to a hex string with possible leading zero.
	 * 
	 * @param b
	 *            the byte array to convert
	 * @param offset
	 *            the offset into the byte array
	 * @param length
	 *            the length of the subset of the byte array to convert starting
	 *            at the offset.
	 * 
	 * @return the hex string
	 */
	public static String toHexString(byte[] b, int offset, int length) {
		StringBuilder sb = new StringBuilder(length * 2);

		for (int i = offset; i < (offset + length); i++) {

			// Look up the high-nibble char
			sb.append(HEX_CHAR[(b[i] & 0xf0) >>> 4]);

			// Look up the low nibble char
			sb.append(HEX_CHAR[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * Interprets a portion of the specified buffer as a text string. Only
	 * printable ASCII characters will be placed in the output string; all other
	 * characters will be replaced with a '.'. The output string is guaranteed
	 * to be <em>length</em> characters long. If necessary, the output string
	 * will be left-padded with ' ' to ensure the length.
	 * 
	 * @param b
	 *            the byte buffer to interpret as clear text.
	 * @param offset
	 *            the offset at which to start intpreting bytes.
	 * @param length
	 *            the number of bytes to interpret in the string. Since we are
	 *            interpreting only printable ASCII characters, this will also
	 *            be the final length of the resulting string.
	 * 
	 * @return the clear text string interpretation of the string of bytes.
	 */
	public static String toClearText(byte[] b, int offset, int length) {
		StringBuilder sb = new StringBuilder(length);

		for (int i = offset; i < b.length && i < (offset + length); i++) {
			if (b[i] >= 32 && b[i] <= 126) {
				sb.append(PARTIAL_ASCII_TABLE[b[i] - 32]);
			}
			else {
				sb.append('.');
			}
		}
		return padWithTrailingChars(sb.toString(), ' ', length);
	}

	/**
	 * Pad string with trailing a number of chars.
	 * 
	 * @param s
	 *            string to pad
	 * @param c
	 *            the padding char
	 * @param size
	 *            number of chars to pad
	 * @return String - padded string
	 */
	public static String padWithTrailingChars(String s, char c, int size) {
		StringBuffer sb = new StringBuffer(s);
		int numCharsToAdd = size - sb.length();

		for (int padNum = 0; padNum < numCharsToAdd; padNum++) {
			sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * Pad string with leading a number of chars.
	 * 
	 * @param s
	 *            string to pad
	 * @param c
	 *            the padding char
	 * @param size
	 *            number of chars to pad
	 * @return String - padded string
	 */
	public static String padWithLeadingChars(String s, char c, int size) {
		StringBuffer sb = new StringBuffer(s);
		int numCharsToAdd = size - sb.length();

		for (int padNum = 0; padNum < numCharsToAdd; padNum++) {
			sb.insert(0, c);
		}

		return sb.toString();
	}

	/**
	 * Formats the specified byte array as a Hex dump.
	 * 
	 * @param b
	 *            the byte array to format
	 * @return the formatted Hex dump.
	 */
	public static String formatHexDump(byte[] b) {
		StringBuilder sb = new StringBuilder();

		int lengthLeft = b.length;
		// int rowIndex = 0;
		int byteIndex = 0;

		StringBuilder fmtBuffer = new StringBuilder();
		Formatter fmt = new Formatter(fmtBuffer);
		while (lengthLeft > 0) {
			// Start with the index into the block of the dump
			fmtBuffer.delete(0, fmtBuffer.length());
			fmt.format("%07X", byteIndex); //$NON-NLS-1$
			sb.append(fmtBuffer.toString());

			String clearText = toClearText(b, byteIndex, 16);

			// Dump the next 16 bytes on the line
			int bytesDumped = 0;
			int charsDumped = 7;
			while (lengthLeft > 0 && bytesDumped < 16) {
				int lengthToDump = lengthLeft;
				if (lengthToDump > 2) {
					lengthToDump = 2;
				}
				sb.append(' ');
				charsDumped++;
				sb.append(toHexString(b, byteIndex, lengthToDump));

				bytesDumped += lengthToDump;
				lengthLeft -= lengthToDump;
				byteIndex += lengthToDump;
				charsDumped += (lengthToDump * 2);
			}

			// The length of a full string is 47
			sb.append("  "); //$NON-NLS-1$
			int more = 47 - charsDumped;
			if (more > 0) {
				sb.append(padWithLeadingChars(clearText, ' ', 16 + more));
			}
			else {
				sb.append(clearText);
			}
			sb.append("\n"); //$NON-NLS-1$
		}

		return sb.toString();
	}

}
