/*
 * @author: Jean Lazarou
 * @date: February 25, 2004
 */
package com.ap.store.conformance;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import com.ap.store.Store;

import junit.framework.TestCase;

public class StoreTestCase extends TestCase {

	public StoreTestCase(String name) {
		super(name);
	}

	protected void writeHello(Store store) {
			
		PrintWriter pw = store.printWriter();
			
		pw.println("Hello");
			
		pw.close();
			
	}

	protected void writeWorld(Store store) {
			
		PrintWriter pw = store.printWriter();
			
		pw.println("World!");
			
		pw.close();
			
	}

	protected void assertIsHello(Store store) throws Exception {
		assertEquals(new String[] {"Hello"}, store.reader());
	}

	protected void assertIsWorld(Store store) throws Exception {
		assertEquals(new String[] {"World!"}, store.reader());
	}

	static public void assertEquals(String[] expected, Reader actual) {
		
		StringWriter buffer = new StringWriter();
		PrintWriter pw = new PrintWriter(buffer);
		
		for (int i = 0; i < expected.length; i++) {
			pw.println(expected[i]);
		}
		
		assertEquals(buffer.toString(), actual);
		
	}
		
	static public void assertEquals(String expected, Reader actual) {

		try {
			
			char[] buffer = new char[expected.length() + 20];
		
			int n = actual.read(buffer);
			
			assertEquals(expected.length(), n);			
			assertEquals(-1, actual.read());
			
			assertEquals(expected, new String(buffer, 0, n));

		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {
				actual.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}

}
