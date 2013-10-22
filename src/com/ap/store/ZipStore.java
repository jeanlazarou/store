/*
 * @author: Jean Lazarou
 * @date: March 9, 2004
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.ap.util.Files;

import com.ap.io.zip.FilesZipper;

public class ZipStore extends JavaFile {

	public ZipStore(File file) {
		super(file);
		
		content = null;
	}

	public ZipStore(String file) {
		this(new File(file));
	}

	public String getName() {
		return file.getName();
	}

	public String getType() {
		return "zip";
	}

	public void create() {
		
		checkState();
		
		if (!file.exists()) {
			
			try {
			
				if (!parent.exists()) {
					parent.mkdirs();				
				}

				OutputStream out = new FileOutputStream(file);
				ZipOutputStream zipout = new ZipOutputStream(out);
				
				createDummyEntry(zipout);
				
				zipout.close();
			
				//dirty = false;
				deleted = false;
				loaded = true;
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new StoreException(e.getMessage());
			}
		}
				
	}
	
	public boolean delete() {
		
		checkState();
		
		loadChildren();
				
		if (kids.size() > 1) return false;

		if (kids.size() == 1 && !kids.containsKey(DIR_CONTENT_NAME)) {
			return false;
		}
		
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
		checkState();
			
		if (!file.exists()) {
			throw new StoreException("Store " + getName() + " does not exist");
		}
		
		createWorkingDirectory();
		
		content = new File(wrkDir, DIR_CONTENT_NAME);
		
		return input(DIR_CONTENT_NAME);
	}

	public OutputStream output() {

		checkState();
			
		dirty = true;
		
		createWorkingDirectory();
		
		try {
			
			content = new File(wrkDir, DIR_CONTENT_NAME);

			return new FileOutputStream(content);
			
		} catch (IOException e) {
			throw new StoreException(e.getMessage());
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

	public void setParent(Store store) {

		checkState();
			
		sync();

		super.setParent(store);
		
	}

	public Store add(String name) {

		checkState();
			
		dirty = true;
		
		loadChildren();

		ZipEntryStore kid = new ZipEntryStore(this, name, this);
		
		kid.setParent(this);
		
		kids.put(name, kid);
		
		return kid;

	}

	public boolean remove(String name) {
		
		dirty = true;
		
		return super.remove(name);
		
	}

	public void sync() {
		
		if (dirty) {
			
			File copy = null;
			
			try {
			
				if (!parent.exists()) {
					parent.mkdirs();
				}
				
				if (file.exists()) {
					if (file.length() == 0) {
						file.delete();
					} else {
						copy = File.createTempFile("copy-", ".zip", file.getParentFile());
				
						copy.delete();
						
						file.renameTo(copy);

					}
				}
				
				if (kids.size() > 0 || (content != null && content.exists())) {
				
					OutputStream out = new FileOutputStream(file);
					ZipOutputStreamWrapper zipout = new ZipOutputStreamWrapper(out);
			
					if (copy != null) {			
						copyZipContent(copy, zipout);
					}
					
					if (wrkDir != null) {						
						FilesZipper.zip(wrkDir.listFiles(), zipout);
					
						Files.deleteFolder(wrkDir);
					}
					
					if (zipout.numberOfEntries() == 0) {
						createStructure(zipout, kids);
					}
					
					if (zipout.numberOfEntries() == 0) {
						createDummyEntry(zipout);
					}
					
					zipout.close();
					
				} else if (!deleted) {					
					create();
				}
				
				wrkDir = null;		
				dirty = false;
				
			} catch (FileNotFoundException e) {
				throw new StoreException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new StoreException(e.getMessage());
			} finally {
				if (copy != null) copy.delete();
			}
		}
	}

	public void createChild(String path) {
		
		checkState();
			
		create();
		
		createWorkingDirectory();
		
		File child = new File(wrkDir, path);

		try {		
			child.getParentFile().mkdirs();
			child.createNewFile();
		} catch (IOException e) {
			throw new StoreException(e.getMessage() + " - " + child.getAbsolutePath());
		}			
		
		dirty = true;
	}

	public InputStream input(String path) {
		
		checkState();
			
		if (wrkDir != null) {
			
			File afile = new File(wrkDir, path);
			
			if (afile.exists()) {			
				try {
					if (afile.isDirectory()){

						File dirContent = new File(afile, DIR_CONTENT_NAME);

						if (!dirContent.exists()) {
							return new ByteArrayInputStream(new byte[0]);
						}

						afile = dirContent;

					}
					
					return new FileInputStream(afile);
				} catch (FileNotFoundException e) {
					throw new StoreException(e.getMessage());
				}
			}
		}
		
		try {
			
			ZipFile zipfile = new ZipFile(file);			
			ZipEntry entry = zipfile.getEntry(path);
			
			if (entry == null) {
				
				zipfile.close();
				
				throw new StoreException("Entry '" + path + "' not found");
			}
			
			if (entry.isDirectory()) {
				entry = zipfile.getEntry(entry.getName() + DIR_CONTENT_NAME);
				
				if (entry == null) {
					return new ByteArrayInputStream(new byte[0]);
				}
			}
			
			return new ZipWrappedInputStream(zipfile, entry);
			
		} catch (ZipException e) {
			throw new StoreException(e.getMessage());
		} catch (IOException e) {
			throw new StoreException(e.getMessage());
		}	
	
	}

	public OutputStream output(String path) {
		
		checkState();
			
		createWorkingDirectory();

		dirty = true;

		int i = path.lastIndexOf('/');
		
		if (i != -1) {
			String subdirs = path.substring(0, i);
			File dir = new File(wrkDir, subdirs);
			
			if (!dir.exists() && !dir.mkdirs()) {			
				throw new StoreException("Cannot create temporary structure:" + dir.getAbsolutePath());
			}
		}

		try {
			
			File theFile = new File(wrkDir, path);
			
			if (theFile.isDirectory()) {
				theFile = new File(theFile, DIR_CONTENT_NAME);
			}
			
			return new FileOutputStream(theFile);
			
		} catch (IOException e) {
			throw new StoreException(e.getMessage());
		}
		
	}
		
	public Reader reader(String path) {
		return new InputStreamReader(input(path));
	}
	
	public Writer writer(String path) {
		return new OutputStreamWriter(output(path));
	}


	public int compareTo(Object obj) {
		Store other = (Store) obj;		
		return getName().compareTo(other.getName());
	}

	protected boolean exists(String path) {
			
		if (wrkDir != null) {
			
			File toFind = new File(wrkDir, path);
			
			if (toFind.exists()) return true;
			
		}
			
		if (!file.exists()) return false;
		
		ZipFile zipfile = null;

		try {
		
			try {
				zipfile = new ZipFile(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new StoreException(e.getMessage());				
			}	
					
			ZipEntry entry = zipfile.getEntry(path);
			
			return entry != null;
			
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					throw new StoreException(e.getMessage());				
				}
			}
		}
		
	}

	protected void delete(String path) {
			
		if (wrkDir != null) {
			File toKill = new File(wrkDir, path);
			
			if (toKill.exists()) toKill.delete();
		}
		
	}

	protected void loadChildren() {
			
		if (loaded) return;
		
		if (!file.exists() || file.length() == 0) return;

		ZipFile zipfile = null;
		
		try {

			zipfile = new ZipFile(file);

			Enumeration it = zipfile.entries();

			while(it.hasMoreElements()) {
				
				ZipEntry entry = (ZipEntry) it.nextElement();
				
				String kidFullPath = entry.getName();
				
				int slash = kidFullPath.indexOf('/');
				
				if (slash != -1) {
					
					String kidName = kidFullPath.substring(0, slash);
						
					kidFullPath = kidFullPath.substring(slash + 1);

					ZipEntryStore kid = (ZipEntryStore) kids.get(kidName);
				
					if (kid == null) {

						kid = new ZipEntryStore(this, kidName, this);
								
						kids.put(kidName, kid);

					}

					if (kidFullPath.length() > 0) {
						loadChildren(kid, kidFullPath);
					}
					
				} else {
					Store kid = new ZipEntryStore(this, kidFullPath, this);		
					kids.put(kidFullPath, kid);
				}
			}
			
		} catch (ZipException e) {
			e.printStackTrace();
			throw new StoreException(e.getMessage());
		} catch (IOException e) {
			throw new StoreException(e.getMessage());
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e1) {
					throw new StoreException(e1.getMessage());
				}
			}
		}
		
		loaded = true;
		
	}

	protected void loadChildren(ZipEntryStore parent, String path) {
		
		int slash = path.indexOf('/');
				
		if (slash != -1) {
			
			String kidName = path.substring(0, slash);
			
			ZipEntryStore kid = (ZipEntryStore) parent.child(kidName);
			
			if (kid == null) {
				kid = (ZipEntryStore) parent.add(kidName);
			}
			
			path = path.substring(slash + 1);
			
			if (path.length() > 0) {
				loadChildren(kid, path);
			}

		} else {
			
			if (parent.child(path) == null) {
				parent.add(path);
			}
			
		}
	}
	
	protected void finalize() throws Throwable {
		sync();
	}

	private int copyZipContent(File source, ZipOutputStream zipout) throws ZipException, IOException {
		
		int copied = 0;
		
		ZipFile zipfile = null;
		
		try {

			zipfile = new ZipFile(source);
			Enumeration it = zipfile.entries();

			int sChunk = 8192;
					
			byte[] buffer = new byte[sChunk];
			
			while(it.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) it.nextElement();
				
				File newer = new File(wrkDir, entry.getName());
				
				if (!newer.exists() && contains(entry.getName())) {
					int length;
					
					InputStream in = zipfile.getInputStream(entry);
					
					zipout.putNextEntry(entry);
			
					while ((length = in.read(buffer, 0, sChunk)) != -1) {
						zipout.write(buffer, 0, length);
					}
			
					in.close();
					
					zipout.closeEntry();
					
					copied++;
				}
			}
			
		} finally {		
			if (zipfile != null) zipfile.close();
		}
		
		return copied;
	}

	private void createStructure(ZipOutputStream zipout, Map kids) throws IOException {

		Iterator it = kids.values().iterator();

		while (it.hasNext()){
			
			ZipEntryStore kid = (ZipEntryStore) it.next();
			
			if (kid.kids.size() > 0) {
				
				ZipEntry entry = new ZipEntry(kid.path + "/");
				
				zipout.putNextEntry(entry);
				zipout.closeEntry();
				
				createStructure(zipout, kid.kids);
				
			} else {
				ZipEntry entry = new ZipEntry(kid.path);
				
				zipout.putNextEntry(entry);
				zipout.closeEntry();
			}
		}
		
	}
	
	private void createDummyEntry(ZipOutputStream zipout) throws IOException {

		ZipEntry e = new ZipEntry(DIR_CONTENT_NAME);
				
		zipout.putNextEntry(e);
		zipout.closeEntry();
		
	}
	
	private void createWorkingDirectory() {

		if (wrkDir == null) {
			
			create();
		
			try {
				
				wrkDir = File.createTempFile("zip-", ".tmp", file.getParentFile());
				
				wrkDir.delete();
				
				if (!wrkDir.mkdir()) {			
					throw new StoreException("Cannot create temporary structure:" + wrkDir.getAbsolutePath());
				}
		
			} catch (IOException e) {
				throw new StoreException(e.getMessage());
			}
			
		}

	}
	
	private boolean contains(String path) {
		
		int prev = 0;
		int i = path.indexOf('/');
		
		Store parent = this;
		
		while (i != -1) {
			
			parent = parent.child(path.substring(prev, i));
			
			if (parent == null) return false;
			
			prev = i + 1;
			
			i = path.indexOf('/', prev);
			
		}
		
		String name = path.substring(prev);
		
		return name.length() == 0 || parent.child(name) != null;
	}
	
	boolean dirty = false;
	
	File wrkDir;
	
}

class ZipEntryStore implements Store, Comparable {

	ZipEntryStore(ZipStore root, String path, Store parent) {
		this.root = root;
		this.path = path;
		this.parent = parent;
		
		int i = path.lastIndexOf('/');
		
		this.name = i == -1 ? path : path.substring(i + 1); 
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		int i = getName().lastIndexOf('.');
		
		return i == -1 ? "" : getName().substring(i + 1).toLowerCase();
	}

	public void sync() {
		root.sync();
	}
	
	public void create() {
		checkState();
		root.createChild(path);
	}

	public boolean exists() {
		return !deleted && root.exists(path);
	}
	
	public boolean delete() {
		checkState();
			
		if (kids.size() > 0) return false;
		
		parent.remove(name);
		
		root.delete(path);
		root.dirty = true;

		deleted = true;
		
		return true;
	}

	public InputStream input() {
		checkState();
			
		return root.input(path);
	}
	
	public OutputStream output() {
		checkState();
			
		return root.output(path);
	}

	public Reader reader() {
		checkState();
			
		return root.reader(path);
	}

	public Writer writer() {		
		checkState();
			
		return root.writer(path);
	}

	public PrintWriter printWriter() {
		return new PrintWriter(writer());		
	}

	public Store getParent() {
		return parent;
	}
	
	public void setParent(Store store) {
		checkState();
		
		this.parent = store;			
	}

	public void attach(Store store) {
		checkState();			
	}
	
	public void detach(Store store) {
		checkState();			
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
			
		root.dirty = true;
		
		Store kid = new ZipEntryStore(root, path + "/" + name, this);
		
		kids.put(name, kid);
		
		return kid;
		
	}

	public boolean remove(String name) {
				
		checkState();
			
		root.dirty = true;

		ZipEntryStore kid = (ZipEntryStore) kids.remove(name);
		
		if (kid != null) {
			kid.setParent(null);	
		}
		
		return kid != null;
	}

	public int compareTo(Object obj) {
		Store other = (Store) obj;		
		return getName().compareTo(other.getName());
	}

	public String toString() {
		return super.toString() + "[name=" + name + ", path=" + path + "]";
	}

	void checkState() {
		if (deleted) {
			throw new IllegalStateException("Store delete: " + getName());		
		}
	}

	boolean deleted = false;

	String name;
	String path;
	
	Store parent;	
	
	ZipStore root;

	Map kids = new HashMap();

}

class ZipWrappedInputStream extends InputStream {

	public ZipWrappedInputStream(ZipFile zipfile, ZipEntry entry) {
		this.entry = entry;
		this.zipfile = zipfile;

		try {
			in = new InputStreamReader(zipfile.getInputStream(entry));
		} catch (IOException e) {
			throw new StoreException(e.getMessage());
		}
	}

	public int read() throws IOException {
		return in.read();
	}

	public void close() throws IOException {
		in.close();
		zipfile.close();
	}
	
	ZipEntry entry;
	ZipFile zipfile;
	InputStreamReader in;

}

class ZipOutputStreamWrapper extends ZipOutputStream {

	int nbEntries = 0;
	
	public ZipOutputStreamWrapper(OutputStream out) {
		super(out);
	}

	public void putNextEntry(ZipEntry e) throws IOException {
		super.putNextEntry(e);
		
		nbEntries++;
	}

	public int numberOfEntries() {
		return nbEntries;
	}
}