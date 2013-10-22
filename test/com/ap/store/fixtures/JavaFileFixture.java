/*
 * @author: Jean Lazarou
 * @date: March 21, 2004
 */
package com.ap.store.fixtures;

import java.io.File;

import com.ap.util.Files;

import com.ap.store.Store;
import com.ap.store.JavaFile;

import com.ap.store.conformance.*;

public class JavaFileFixture implements StoreFixture {

	public void setUp() throws Exception {
		file = new File("test-temp/one.txt");
	}

	public void tearDown() throws Exception {
		Files.deleteFolder(new File("test-temp"));
	}

	public String getStoreName() {
		return "JavaFile";
	}

	public Store newTestStore() {
		return new JavaFile(file);
	}

	File file;

}
