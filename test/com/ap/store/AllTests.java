/*
 * @author: Jean Lazarou
 * @date: 15 févr. 04
 */
package com.ap.store;

import com.ap.store.fixtures.*;
import com.ap.store.conformance.*;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
    public static Test suite() {

		StoreFixture[] fixtures = new StoreFixture[] {
			new MemoryStoreFixture(),
			new ContentProviderFixture(),
			new JavaFileFixture(),
			new ZipStoreFixture(),
			new ZipEntryStoreFixture(),
		};

        TestSuite suite = new TestSuite("Store Project Tests");

		suite.addTest(new StoreConformanceTests(fixtures));
		
		suite.addTest(new TestSuite(TestJavaFile.class));
		
		suite.addTest(new TestSuite(TestZipStore.class));

        return suite;
    }
}
