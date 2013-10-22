/*
 * @author: Jean Lazarou
 * @date: 1 mars 04
 */
package com.ap.store;

import java.io.InputStream;
import java.io.OutputStream;

public interface Content {
	
	long size();
	
	void create();
	boolean exists();
	
	InputStream input();
	OutputStream output();
	
}
