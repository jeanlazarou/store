/*
 * @author: Jean Lazarou
 * @date: April 1, 2004
 */
package com.ap.store.conformance;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.ap.store.Store;

public class SavedStoreConformanceTests extends StoreConformanceTestCase {

	public SavedStoreConformanceTests(StoreFixture fixture, String method) {
		super(fixture, method);
	}
	
	protected void setUp() throws Exception {

		super.setUp();
		
		writeHello(one.add("test.txt"));		
		writeHello(one.add("test2.txt"));		
		writeHello(one.add("test3.txt"));		

		Store sub = one.add("temp");
		
		writeHello(sub.add("test5.txt"));
		writeHello(sub.add("test6.txt"));
		writeHello(sub.add("test7.txt"));
		
		one.sync();
		
		one = fixture.newTestStore();
	}

	public void testCreateExistingStore() throws Exception {
		one.create();
		assertTrue(one.exists());
	}

	public void testReloadStructure() throws Exception {
		
		List list = new ArrayList(one.children());
		
		Collections.sort(list);

		assertEquals(4, list.size());
		assertEquals("temp", ((Store)list.get(0)).getName());
		assertEquals("test.txt", ((Store)list.get(1)).getName());
		assertEquals("test2.txt", ((Store)list.get(2)).getName());
		assertEquals("test3.txt", ((Store)list.get(3)).getName());
		
		Store store = one.child("temp");

		list = new ArrayList(store.children());
		
		Collections.sort(list);

		assertEquals(3, list.size());
		assertEquals("test5.txt", ((Store)list.get(0)).getName());
		assertEquals("test6.txt", ((Store)list.get(1)).getName());
		assertEquals("test7.txt", ((Store)list.get(2)).getName());
		
	}
	
	public void testReloadLargeStructure() throws Exception {
				
		Store store = one.add("sub");
		
		writeHello(store.add("two.txt"));
		writeHello(store.add("three.txt"));

		store = store.add("subsub");

		writeHello(store.add("four.txt"));

		one.sync();
				
		// check
		one = fixture.newTestStore();

		Collection list = one.children();
		
		assertEquals(5, list.size());		
		assertIsHello(one.child("test.txt"));		
		assertIsHello(one.child("test2.txt"));		
		assertIsHello(one.child("test3.txt"));
		
		Store dir = one.child("temp");		

		list = dir.children();

		assertEquals(3, list.size());		
		assertIsHello(dir.child("test5.txt"));
		assertIsHello(dir.child("test6.txt"));
		assertIsHello(dir.child("test7.txt"));
		
		dir = one.child("sub");		

		list = dir.children();

		assertEquals(3, list.size());		
		assertIsHello(dir.child("two.txt"));		
		assertIsHello(dir.child("three.txt"));
		
		dir = dir.child("subsub");		

		list = dir.children();
		
		assertEquals(1, list.size());		
		assertIsHello(dir.child("four.txt"));
	}

	public void testReReadDirContent() throws Exception {

		writeHello(one);		
		one.sync();

		one = fixture.newTestStore();

		assertIsHello(one);
				
	}

	public void testModifyZipStructure() throws Exception {
		
		writeWorld(one.child("test.txt"));

		one.sync();

		// check		
		one = fixture.newTestStore();		
		
		List list = new ArrayList(one.children());
		
		Collections.sort(list);

		assertEquals(4, list.size());
		
		assertIsWorld(one.child("test.txt"));
		assertIsHello(one.child("test2.txt"));
		assertIsHello(one.child("test3.txt"));
		
		assertNotNull(one.child("temp"));
		
	}
	
	public void testReadModified() throws Exception {
		
		writeWorld(one.add("test.txt"));

		assertIsHello(one.child("test2.txt"));		
		assertIsWorld(one.child("test.txt"));		
		assertIsHello(one.child("test3.txt"));		

	}
	
	public void testDeleteChildStore() throws Exception {
		
		one.child("test2.txt").delete();
		
		one.sync();
		
		// check
		one = fixture.newTestStore();

		assertIsHello(one.child("test.txt"));
		assertIsHello(one.child("test3.txt"));
		
		assertEquals(null, one.child("test2.txt"));
	}

	public void testDeleteModifedChildStore() throws Exception {
		
		writeWorld(one.child("test2.txt"));
		
		one.child("test2.txt").delete();
		
		one.sync();
		
		// check
		one = fixture.newTestStore();

		assertIsHello(one.child("test.txt"));
		assertIsHello(one.child("test3.txt"));
		
		assertEquals(null, one.child("test2.txt"));
	}

	public void testDeleteDescendantStore() throws Exception {
		
		// extend the structure
		Store store = one.child("temp");
		
		store = store.add("subsub");

		writeHello(store.add("four.txt"));
		writeHello(store.add("five.txt"));

		one.sync();

		// delete some stores		
		one = fixture.newTestStore();

		store = one.child("temp");
		store.child("test6.txt").delete();
		
		store = store.child("subsub");
		store.child("four.txt").delete();
		
		one.sync();

		// check
		one = fixture.newTestStore();

		store = one.child("temp");		
		assertEquals(null, store.child("test3.txt"));

		store = store.child("subsub");		
		assertEquals(null, store.child("four.txt"));
	}
	
}
