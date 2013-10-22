/*
 * @author: Jean Lazarou
 * @date: 24 févr. 04
 */
package com.ap.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class MemoryStore implements Store, Comparable {

	public MemoryStore(String name) {
		this(name, null);
	}

	public MemoryStore(String name, Content provider) {
		this.name = name;
		this.provider = provider;
	}


	public String getName() {
		return name;
	}
	
	public String getType() {
		int i = getName().lastIndexOf('.');
		
		return i == -1 ? "" : getName().substring(i + 1).toLowerCase();
	}


	public void sync() {
	}
	
	public void create() {

		checkState();

		if (created) return;
		
		if (parent != null && !parent.exists()) { 
			parent.create();
		}
		
		if (provider != null) {
			provider.create();
			return; 
		}

		content = new ByteArrayOutputStream();

		deleted = false;
		created = true;
	}

	public boolean exists() {
		if (deleted) return false;
		
		if (provider != null) {
			return provider.exists(); 
		}

		return created;
	}

	public boolean delete() {
		
		checkState();
			
		if (kids.size() > 0) return false;
		
		if (parent == null) {
			deleted = true;
			return true;
		} else if (parent.remove(name)) {
			deleted = true;
			return true;
		}
		
		return false;
		
	}


	public InputStream input() {

		checkState();
			
		if (provider != null) {
			InputStream r = provider.input();
			
			if (r == null) { 
				throw new StoreException(name + " has no content");
			}
			
			return r;
		}
		
		if (content == null) {
			throw new StoreException(name + " has no content");
		}
		
		return new ByteArrayInputStream(content.toByteArray());

	}
	
	public OutputStream output() {
		
		checkState();

		create();
			
		if (provider != null) {
			return provider.output(); 
		}
		
		content = new ByteArrayOutputStream();
		
		return content;

	}

	public Reader reader() {
		return new InputStreamReader(input());
	}


	public Writer writer() {
		return new OutputStreamWriter(output());
	}

	public PrintWriter printWriter() {		
		return new PrintWriter(writer());		
	}


	public Store getParent() {
		return parent;
	}
	
	public void setParent(Store store) {
		checkState();
			
		parent = store;
	}


	public void attach(Store store) {
		checkState();
			
		store.setParent(this);

		kids.put(store.getName(), store);
	}
	
	public void detach(Store store) {
		checkState();
			
		if (kids.remove(store.getName()) != null) {
			store.setParent(null);
		}
	}
	

	public Collection children() {
		checkState();
			
		return kids.values();
	}

	public Store child(String name) {
		checkState();
			
		return (Store) kids.get(name);
	}

	public Store add(String name) {
		checkState();
		
		return add(name, null);
	}

	public Store add(String name, Content content) {
		checkState();
			
		MemoryStore kid = new MemoryStore(name, content);
	
		kid.setParent(this);
			
		kids.put(name, kid);
		
		return kid;
	}

	public boolean remove(String name) {
		checkState();
			
		Store kid = (Store) kids.remove(name);
		
		if (kid != null) kid.setParent(null);
		
		return kid != null;
	}

	public int compareTo(Object obj) {
		Store other = (Store) obj;		
		return getName().compareTo(other.getName());
	}

	public String toString() {
		return super.toString() + " [name=" + name + " ]";
	}

	void checkState() {
		if (deleted) {
			throw new IllegalStateException("Store delete: " + getName());		
		}
	}

	Store parent;
	
	String name;
	ByteArrayOutputStream content;

	boolean created;
	
	Content provider;
		
	Map kids = new HashMap();

	boolean deleted = false; 
}
