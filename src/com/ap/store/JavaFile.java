/*
 * @author: Jean Lazarou
 * @date: 24 févr. 04
 */
package com.ap.store;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JavaFile implements Store, Comparable {

	public JavaFile(File file) {
		
		this.file = file.getAbsoluteFile();
		this.parent = file.getAbsoluteFile().getParentFile();

		if (file.isDirectory()) {
			this.content = new File(this.file, DIR_CONTENT_NAME);
		} else {
			this.content = this.file;
		}
		
	}

	public JavaFile(String file) {
		this(new File(file));
	}

	public String getName() {
		return file.getName();
	}
	
	public String getType() {
		int i = getName().lastIndexOf('.');
		
		return i == -1 ? "" : getName().substring(i + 1).toLowerCase();
	}

	public void sync() {
	}
	
	public void create() {

		checkState();
				
		try {

			if (file.exists()) return;
			
			getParent().create();
			
			if (kids.size() > 0) {
				file.mkdir();
			} else {
				file.createNewFile();			
				loaded = true;
			}

			deleted = false;
			
		} catch (IOException e) {
			throw new StoreException(e.getMessage());
		}
		
	}

	public boolean exists() {
		return file.exists();
	}
	
	public boolean delete() {
		
		checkState();
		
		loadChildren();
		
		if (file.isDirectory() && content.exists()) {
			kids.remove(content.getName());	
			content.delete();
		}
		
		if (kids.size() > 0) return false;
		
		if (getParent() != null && !getParent().remove(getName())) {
			return false;
		}
		
		if (file.exists() && !file.delete()) {
			throw new StoreException("Store " + getName() + " deleted but persited file " + file + " cannot be deleted");
		}
		
		deleted = true;
		
		return true;
		
	}

	public InputStream input() {
		try {
			checkState();

			if (file.exists() && !content.exists()) {
				return new ByteArrayInputStream(new byte[0]);			
			}
			
			return new FileInputStream(content);
		} catch (FileNotFoundException e) {
			throw new StoreException(e.getMessage());
		}
	}
	
	public OutputStream output() {
		
		try {
			
			checkState();
			
			return new FileOutputStream(content);
			
		} catch (IOException e) {
			
			if (!content.getParentFile().exists()) {
				create();
				
				try {
					return new FileOutputStream(content);
				} catch (IOException e1) {
					throw new StoreException(e.getMessage());
				}
				
			} else {
				throw new StoreException(e.getMessage());
			}
		}
				
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
		
		if (!deleted && parentStore == null) {
			JavaFile dad = new JavaFile(parent);
			dad.add(this);
		}
		
		return parentStore;
		
	}
	
	public void setParent(Store store) {
		checkState();
			
		loadChildren();
		
		parentStore = store;
	}

	public void attach(Store store) {
		checkState();
			
		loadChildren();
		
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
			
		loadChildren();		
		
		Collection res = new ArrayList(kids.values());
		
		if (kids.containsKey(DIR_CONTENT_NAME)) {
			res.remove(kids.get(DIR_CONTENT_NAME));
		}
		
		return res;
	}

	public Store child(String name) {
		
		checkState();
			
		loadChildren();		

		return (Store) kids.get(name);
	}

	public Store add(String name) {
		
		checkState();
			
		loadChildren();

		turnToDirectory();

		Store res = new JavaFile(new File(file, name));
		
		res.setParent(this);
		
		kids.put(name, res);
		
		return res;
	}

	public boolean remove(String name) {
		
		checkState();
			
		loadChildren();		

		Store kid = (Store) kids.remove(name);
		
		if (kid != null) kid.setParent(null);
		
		return kid != null;
		
	}

	public int compareTo(Object obj) {
		Store other = (Store) obj;		
		return getName().compareTo(other.getName());
	}

	public String toString() {
		return super.toString() + "[" + file + "]";
	}

	private Store add(JavaFile store) {
		
		loadChildren();		

		store.setParent(this);
		
		kids.put(store.getName(), store);
		
		return store;
		
	}

	protected void loadChildren() {
		
		if (loaded) return;
		
		File[] files = file.listFiles();
		
		if (files == null) return;
		
		for (int i = 0; i < files.length; i++) {
			
			if (!kids.containsKey(files[i].getName())) {
				kids.put(files[i].getName(), new JavaFile(files[i]));
			}
			
		}
		
		loaded = true;
		
	}

	private void turnToDirectory() {
		
		if (content.exists() && !content.isDirectory()) {
			
			if (content.length() == 0) {
				
				content.delete();
			
				file.mkdir();
			
				content = new File(file, DIR_CONTENT_NAME);
				
				try {
					content.createNewFile();
				} catch (IOException e) {
					throw new StoreException(e.getMessage());
				}

			} else {
				
				File temp;
				
				try {
					temp = File.createTempFile(content.getName(), "", parent);
				} catch (IOException e) {
					throw new StoreException(e.getMessage());
				}
				
				temp.delete();
				content.renameTo(temp);

				content.delete();
				
				content.mkdir();
				
				content = new File(file, DIR_CONTENT_NAME);
				
				temp.renameTo(content);
			}
			
		} else {
			content = new File(file, DIR_CONTENT_NAME);
		}
		
	}

	void checkState() {
		if (deleted) {
			throw new IllegalStateException("Store delete: " + getName());		
		}
	}
	
	Store parentStore;
	File file, parent, content;
	
	Map kids = new HashMap();

	protected boolean loaded = false;
	protected boolean deleted = false;
	
	protected static final String DIR_CONTENT_NAME = "dir.content";
}
