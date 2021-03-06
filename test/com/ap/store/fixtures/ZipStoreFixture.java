/*
 * @author: Jean Lazarou
 * @date: March 21, 2004
 */
package com.ap.store.fixtures;

import java.io.File;

import com.ap.util.Files;

import com.ap.store.Store;
import com.ap.store.ZipStore;

import com.ap.store.conformance.*;

public class ZipStoreFixture implements StoreFixture {

	public void setUp() throws Exception {
		zip = new File("test-temp/test.zip");
	}

	public void tearDown() throws Exception {
		Files.deleteFolder(new File("test-temp"));
	}

	public String getStoreName() {
		return "ZipStore";
	}

	public Store newTestStore() {
		return new ZipStore(zip);
	}
	
	File zip;

}
