package com.hephaestus.http.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestHexDump {

	private static final byte[] BUFFER_32 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
			28, 29, 30, 31 };
	private static final byte[] BUFFER_31 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
			28, 29, 30 };
	private static final byte[] BUFFER_30 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
			28, 29 };

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHexDump32() {
		String dump = NumberUtils.formatHexDump(BUFFER_32);
		assertEquals(
				"0000000 0001 0203 0405 0607 0809 0a0b 0c0d 0e0f\n0000010 1011 1213 1415 1617 1819 1a1b 1c1d 1e1f\n",
				dump);
	}

	@Test
	public void testHexDump31() {
		String dump = NumberUtils.formatHexDump(BUFFER_31);
		assertEquals(
				"0000000 0001 0203 0405 0607 0809 0a0b 0c0d 0e0f\n0000010 1011 1213 1415 1617 1819 1a1b 1c1d 1e\n",
				dump);
	}

	@Test
	public void testHexDump30() {
		String dump = NumberUtils.formatHexDump(BUFFER_30);
		assertEquals(
				"0000000 0001 0203 0405 0607 0809 0a0b 0c0d 0e0f\n0000010 1011 1213 1415 1617 1819 1a1b 1c1d\n",
				dump);
	}
}
