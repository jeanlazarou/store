/*
 * @author: Jean Lazarou
 * @date: April 1, 2004
 */
package com.ap.store.conformance;

import com.ap.store.Store;

public class StoreCreateConformanceTests extends StoreConformanceTestCase {

	public StoreCreateConformanceTests(StoreFixture fixture, String method) {
		super(fixture, method);
	}

	public void testWithCreateMethod() throws Exception {
		
		assertTrue(!one.exists());
		
		one.create();
		
		assertTrue(one.exists());
		
	}

	public void testByWritingContent() throws Exception {
		
		assertTrue(!one.exists());
		
		writeHello(one);
		
		assertTrue(one.exists());
		
		assertEquals(new String[] {"Hello"}, one.reader());
		
	}

	public void testAddContent() throws Exception {
		
		one.create();
		
		assertTrue(one.exists());
		
		writeHello(one);
		
		assertTrue(one.exists());

		assertIsHello(one);
		
	}

	public void testParentWhenChildIsCreated() throws Exception {
		
		assertTrue(!one.exists());

		Store child = one.add("my-child");
		
		assertTrue(!child.exists());
		
		writeHello(child);
		
		assertTrue(one.exists());
		assertTrue(child.exists());

	}
}
