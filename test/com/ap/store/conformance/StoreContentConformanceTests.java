/*
 * @author: Jean Lazarou
 * @date: April 1, 2004
 */
package com.ap.store.conformance;

import java.io.InputStream;
import java.io.Reader;

import com.ap.store.Store;
import com.ap.store.StoreException;

public class StoreContentConformanceTests extends StoreConformanceTestCase {

	public StoreContentConformanceTests(StoreFixture fixture, String method) {
		super(fixture, method);
	}

	public void testFailToReadNotCreated() throws Exception {

		assertTrue(!one.exists());

		try {
			fixture.newTestStore().reader();
			fail("Should not read a store that does not exist!");
		} catch (StoreException ex) {
			// fine...
		}
	}

	public void testCanInputAfterCreate() throws Exception {
		
		one.create();

		InputStream in = one.input();
		
		assertNotNull(in);
		assertEquals(-1, in.read());
		
		in.close(); 		

	}

	public void testCanInputParentAfterCreate() throws Exception {
		
		one.create();

		one.add("empty.txt");
		
		InputStream in = one.input();
		
		assertNotNull(in);
		assertEquals(-1, in.read());
		
		in.close(); 		

	}
	
	public void testCanReadAfterCreate() throws Exception {
		
		one.create();
		
		Reader r = one.reader();
		
		r.close();
		
	}
	
	public void testCanReadParentAfterCreate() throws Exception {

		Store empty = one.add("empty.txt");

		empty.create();
		
		Reader r = one.reader();
		
		r.close();
		
	}

	public void testExistsAfterAddingAChild() throws Exception {
		
		writeHello(one);

		one.sync();
		
		assertIsHello(one);
		
	}

	public void testMayBeDeleted() throws Exception {
		
		writeHello(one);

		Store empty = one.add("empty.txt");
		
		empty.delete();
		one.delete();
		
		one.sync();
		
		assertTrue(!one.exists());
		
	}

	public void testMayBeDeletedAfterSync() throws Exception {
		
		writeHello(one);

		Store empty = one.add("empty.txt");
		
		one.sync();
		
		empty.delete();
		one.delete();
		
		one.sync();
		
		assertTrue(!one.exists());
		
	}

}
