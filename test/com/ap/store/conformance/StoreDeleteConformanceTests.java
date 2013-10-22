/*
 * @author: Jean Lazarou
 * @date: March 21, 2004
 */
package com.ap.store.conformance;

import com.ap.store.Store;

public class StoreDeleteConformanceTests extends StoreConformanceTestCase {

	public StoreDeleteConformanceTests(StoreFixture fixture, String method) {
		super(fixture, method);
	}

	public void testWithoutCreate() {		
		
		assertTrue(!one.exists());
		
		one.delete();
		
		assertTrue(!one.exists());
	}

	public void testEmptyStore() {		
		
		one.create();

		assertTrue(one.exists());
		
		one.delete();

		assertTrue(!one.exists());
	}

	public void testStoreWithoutChildren() {
		
		writeHello(one);

		one.delete();
				
		assertTrue(!one.exists());
	}

	public void testFailIfHasChildren() {
		
		Store child = one.add("a-child.txt");
				
		child.create();
			
		assertTrue(!one.delete());
		
	}

	public void testReuse() {

		String name = one.getName();
		String type = one.getType();
				
		one.create();
		
		one.delete();
	
		try {
			one.add("impossible.doc");
			fail("Should not accept to add a child is deleted");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			one.child("unknown");
			fail("Should not accept to retrieve a child from a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			one.children();
			fail("Should not accept to get the children of a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			one.remove("hello");
			fail("Should not accept to remove anything from a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			one.delete();
			fail("Should not accept to delete a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			writeWorld(one);
			fail("Should not accept to write to a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			one.reader();
			fail("Should not accept to read from a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		try {
			one.setParent(null);
			fail("Should not accept to set a parent to a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	
		assertEquals(name, one.getName());		
		assertEquals(type, one.getType());		
		assertTrue(!one.exists());		
		assertEquals(null, one.getParent());
		
		try {
			one.create();
			fail("May not re-created a deleted store");
		} catch(IllegalStateException e) {
			// expected
		}
	}

	public void testParentWithoutChildren() {
		
		Store child = one.add("a-child.txt");
				
		child.create();
		
		writeHello(one);

		child.delete();
		one.delete();
		
		assertTrue(!one.exists());
	}

	public void testOneChild() {
		
		Store child = one.add("a-child.txt");
				
		child.create();
		
		child.delete();

		assertTrue(!child.exists());
		assertTrue(one.exists());
	}

	public void testOneOfTheChildren() throws Exception {
		
		writeHello(one.add("one.txt"));
		writeHello(one.add("two.txt"));
		writeHello(one.add("three.txt"));
		
		one.child("two.txt").delete();
		
		// check
		// sync is necessary here 
		//one = fixture.newTestStore();

		assertIsHello(one.child("one.txt"));
		assertIsHello(one.child("three.txt"));
		
		assertEquals(null, one.child("two.txt"));
	}

	public void testDeleteDescendantStore() throws Exception {

		writeHello(one.add("one.txt"));
		writeHello(one.add("two.txt"));
		writeHello(one.add("three.txt"));
		
		Store store = one.add("sub");
		
		writeHello(store.add("two.txt"));
		writeHello(store.add("three.txt"));

		store = store.add("subsub");

		writeHello(store.add("four.txt"));
		writeHello(store.add("five.txt"));
		
		store.child("four.txt").delete();

		store = store.getParent();
		store.child("three.txt").delete();
		
		// check
		// sync is necessary here 
		//one = fixture.newTestStore();

		store = one.child("sub");		
		assertEquals(null, store.child("three.txt"));

		store = store.child("subsub");		
		assertEquals(null, store.child("four.txt"));
	}

}
