/*
 * @author: Jean Lazarou
 * @date: February 25, 2004
 */
package com.ap.store;

import java.io.File;
import java.io.FileReader;

import com.ap.store.conformance.*;

import com.ap.util.Files;

public class TestJavaFile extends StoreTestCase {

	public TestJavaFile(String name) {
		super(name);
	}

	public void testExistMethod() throws Exception {
		
		assertTrue(!file.exists());
		assertTrue(!store.exists());
		
		writeHello(store);
		
		assertTrue(file.exists());
		assertTrue(store.exists());
		
	}

	public void testTurnStoreToDir() throws Exception {
		
		store.create();
		
		assertTrue(file.exists());
		assertTrue(!file.isDirectory());

		Store empty = store.add("empty.txt");
				
		assertTrue(file.exists());
		assertTrue(file.isDirectory());

		empty.create();

		assertTrue(new File("test-temp/empty.txt").exists());
		
	}

	public void testTurnStoreWithContentToDir() throws Exception {
		
		writeHello(store);

		assertEquals(new String[] {"Hello"}, new FileReader(new File("test-temp/dir.content")));
		
	}

	protected void setUp() throws Exception {
		file = new File("test-temp");
		store = new JavaFile(file);
	}

	protected void tearDown() throws Exception {		
		Files.deleteFolder(new File("test-temp"));
	}

	File file;
	JavaFile store;

}
