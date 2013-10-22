/*
 * @author: Jean Lazarou
 * @date: March 31, 2004
 */
package com.ap.store.fixtures;

import com.ap.store.Store;
import com.ap.store.MemoryStore;

import com.ap.store.conformance.*;

public class MemoryStoreFixture implements StoreFixture {

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
		theStore = null;
	}

	public String getStoreName() {
		return "MemoryStore";
	}

	public Store newTestStore() {
		
		if (theStore == null) {
			theStore = new MemoryStore("one.txt");
		}

		return theStore;
		
	}
	
	MemoryStore theStore;

}
