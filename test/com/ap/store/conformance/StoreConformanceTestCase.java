/*
 * @author: Jean Lazarou
 * @date: April 1, 2004
 */
package com.ap.store.conformance;

import com.ap.store.Store;

public class StoreConformanceTestCase extends StoreTestCase {

	public StoreConformanceTestCase(StoreFixture fixture, String method) {
		super(method);

		this.fixture = fixture;
	}

	protected void setUp() throws Exception {
		fixture.setUp();
		one = fixture.newTestStore();
	}

	protected void tearDown() throws Exception {
		one.sync();
		fixture.tearDown();
	}

	Store one;
	StoreFixture fixture;
}
