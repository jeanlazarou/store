/*
 * @author: Jean Lazarou
 * @date: May 20, 2004
 */
package com.ap.store.conformance;

import java.io.InputStream;
import java.io.OutputStream;

import com.ap.store.Store;

public class StoreStreamIOConformanceTests extends StoreConformanceTestCase {

	public StoreStreamIOConformanceTests(StoreFixture fixture, String method) {
		super(fixture, method);
	}
	
	public void testBinaryIO() throws Exception {

		Store store = one.add("new.txt");

		OutputStream out = store.output();

		for (int i = 0; i < 256; i++) {
			out.write(i);
		}
		
		out.close();
		
		InputStream in = store.input();

		for (int i = 0; i < 256; i++) {
			assertEquals(i, in.read());
		}
		
		assertEquals(-1, in.read());

		in.close();
		
	}
}
