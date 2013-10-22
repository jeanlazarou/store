/*
 * @author: Jean Lazarou
 * @date: March 11, 2004
 */
package com.ap.store;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.ap.util.Files;

import com.ap.store.conformance.StoreTestCase;

public class TestZipStore extends StoreTestCase {

	public TestZipStore(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		
		super.setUp();

		zip = new ZipStore("test-temp/test.zip");
				
	}

	protected void tearDown() throws Exception {

		if (zipfile != null) zipfile.close();

		Files.deleteFolder(new File(("test-temp")));
		
	}

	public void testWriteOneFile() throws Exception {
		
		writeHello(zip.add("one.txt"));
		
		zip.sync();

		// check		
		zipfile = new ZipFile(new File("test-temp/test.zip"));
		
		assertEquals(1, zipfile.size());
		
		ZipEntry entry = zipfile.getEntry("one.txt");
		
		assertNotNull(entry);
		assertEquals(new String[] {"Hello"}, new InputStreamReader(zipfile.getInputStream(entry)));
		
	}
	
	public void testWriteLargeStructure() throws Exception {
		
		writeHello(zip.add("one.txt"));
		
		Store store = zip.add("sub");
		
		writeHello(store.add("two.txt"));
		writeHello(store.add("three.txt"));

		store = store.add("subsub");

		writeHello(store.add("four.txt"));

		zip.sync();

		// check		
		zipfile = new ZipFile(new File("test-temp/test.zip"));
		
		assertEquals(6, zipfile.size());
		
		ZipEntry entry;
		Iterator it = sort(zipfile);

		entry = (ZipEntry) it.next();
		assertIsHello("one.txt", entry);		

		entry = (ZipEntry) it.next();
		assertEquals("sub/", entry.getName());		

		entry = (ZipEntry) it.next();
		assertEquals("sub/subsub/", entry.getName());

		entry = (ZipEntry) it.next();
		assertIsHello("sub/subsub/four.txt", entry);

		entry = (ZipEntry) it.next();
		assertIsHello("sub/three.txt", entry);		

		entry = (ZipEntry) it.next();
		assertIsHello("sub/two.txt", entry);		
		
		assertTrue(!it.hasNext());
		
	}

	public void testWriteZipContent() throws Exception {

		writeHello(zip);
		
		zip.sync();

		// check		
		zipfile = new ZipFile(new File("test-temp/test.zip"));
		
		assertEquals(1, zipfile.size());
		
		ZipEntry entry = (ZipEntry) zipfile.entries().nextElement();
		
		assertEquals(new String[] {"Hello"}, new InputStreamReader(zipfile.getInputStream(entry)));

	}

	public void testDirectoriesAreSaved() throws Exception {
		
		Store dir = zip.add("dir");
		
		dir = dir.add("sub");
		dir = dir.add("subsub");
		
		Store empty =  dir.add("empty.txt");
		
		empty.create();
		empty.delete();
		
		zip.sync();
		
		// check
		zipfile = new ZipFile(new File("test-temp/test.zip"));
		
		assertEquals(3, zipfile.size());
		
		ZipEntry entry;
		Enumeration it = zipfile.entries();

		entry = (ZipEntry) it.nextElement();
		assertEquals("dir/", entry.getName());		

		entry = (ZipEntry) it.nextElement();
		assertEquals("dir/sub/", entry.getName());		

		entry = (ZipEntry) it.nextElement();
		assertEquals("dir/sub/subsub/", entry.getName());
					
	}
	
	protected void assertIsHello(String expected, ZipEntry entry) throws Exception {
		assertEquals(expected, entry.getName());
		
		if (!entry.isDirectory()) {
			assertEquals(new String[] {"Hello"}, new InputStreamReader(zipfile.getInputStream(entry)));
		}
	}

	protected void assertIsWorld(String expected, ZipEntry entry) throws Exception {
		assertEquals(expected, entry.getName());
		
		if (!entry.isDirectory()) {
			assertEquals(new String[] {"World!"}, new InputStreamReader(zipfile.getInputStream(entry)));
		}
	}

	protected Iterator sort(ZipFile zipfile) {
		
		List list = new ArrayList();
		
		Enumeration it = zipfile.entries();

		while(it.hasMoreElements()) {
			list.add(it.nextElement());
		}
		
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				ZipEntry entry1 = (ZipEntry) o1;
				ZipEntry entry2 = (ZipEntry) o2;
				
				return entry1.getName().compareTo(entry2.getName());
			}
		});
		
		return list.iterator();
	}
	
	ZipStore zip;
	ZipFile zipfile;
	
}
