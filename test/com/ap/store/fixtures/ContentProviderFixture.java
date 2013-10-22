/*
 * @author: Jean Lazarou
 * @date: March 31, 2004
 */
package com.ap.store.fixtures;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ap.store.Store;
import com.ap.store.Content;
import com.ap.store.MemoryStore;

import com.ap.store.conformance.*;

public class ContentProviderFixture implements StoreFixture, Content {

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
		
		theStore = null;
		created = false;
		
		if (content != null) content.reset();
	}

	public String getStoreName() {
		return "MemoryStoreWithContentProvider";
	}

	public Store newTestStore() {
		
		if (theStore == null) {
			theStore = new MemoryStore("one.txt", this);
		}

		return theStore;
		
	}
	
	//----- Content interface -------------
	
	public long size() {
		return content.size();
	}

	public void create() {
		created = true;
	}

	public boolean exists() {
		return created;
	}

	public OutputStream output() {

		if (!created) return null;

		content = new ByteArrayOutputStream();
		
		return content;
	}

	public InputStream input() {
		
		if (!created) return null;
		
		return new ByteArrayInputStream(content.toByteArray());
		
	}

	MemoryStore theStore;

	boolean created;
	ByteArrayOutputStream content;
}
