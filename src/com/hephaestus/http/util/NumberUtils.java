package com.hephaestus.http.util;

import java.util.Formatter;

public class NumberUtils {
	// table to convert a nibble to a hex char.
	private static char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

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

			// Dump the next 16 bytes on the line
			int bytesDumped = 0;
			while (lengthLeft > 0 && bytesDumped < 16) {
				int lengthToDump = lengthLeft;
				if (lengthToDump > 2) {
					lengthToDump = 2;
				}
				sb.append(' ');
				sb.append(toHexString(b, byteIndex, lengthToDump));

				bytesDumped += lengthToDump;
				lengthLeft -= lengthToDump;
				byteIndex += lengthToDump;

			}
			sb.append("\n"); //$NON-NLS-1$
		}

		return sb.toString();
	}

}
