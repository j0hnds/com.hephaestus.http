package com.hephaestus.http.util;


import static org.junit.Assert.*;

import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMessageFormatting {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMessageFormatting() {
		String result = MessageFormat.format("This is a {0}, with a number: {1,number,integer}", new Object[] { "replacement", 18 }); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("This is a replacement, with a number: 18", result); //$NON-NLS-1$
	}
}
