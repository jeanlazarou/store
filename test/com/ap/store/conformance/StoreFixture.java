/*
 * @author: Jean Lazarou
 * @date: March 21, 2004
 */
package com.ap.store.conformance;

import com.ap.store.Store;

public interface StoreFixture {

	void setUp() throws Exception;
	void tearDown() throws Exception;

	String getStoreName();
	
	Store newTestStore();

}
