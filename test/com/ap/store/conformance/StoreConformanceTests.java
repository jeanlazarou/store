/*
 * @author: Jean Lazarou
 * @date: March 22, 2004
 */
package com.ap.store.conformance;

import junit.framework.TestSuite;

import com.ap.junit.TestSuites;

public class StoreConformanceTests extends TestSuite {
	
	public StoreConformanceTests(StoreFixture[] fixtures) {
		
		super("Store Conformance Tests");
		
		for (int i = 0; i < fixtures.length; i++) {
			
			StoreFixture fix = fixtures[i];
			
			TestSuite suite = new TestSuite(fix.getStoreName());

			addTest(suite);
			
			addTestMethods(suite, StoreDeleteConformanceTests.class, fix);
			addTestMethods(suite, StoreDeleteWithSyncConformanceTests.class, fix);
			addTestMethods(suite, StoreCreateConformanceTests.class, fix);
			addTestMethods(suite, SavedStoreConformanceTests.class, fix);
			addTestMethods(suite, StoreContentConformanceTests.class, fix);					
			addTestMethods(suite, StoreStreamIOConformanceTests.class, fix);
				   			
		}
		
	}

	protected static void addTestMethods(TestSuite parent, Class theClass, StoreFixture fix) {
		TestSuites.addTestMethods(parent, theClass, StoreFixture.class, fix);
	}
}
