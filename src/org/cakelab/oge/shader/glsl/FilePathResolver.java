package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * File path resolver interprets everything as simple 
 * file system paths.
 * 
 * @author homac
 *
 */
public class FilePathResolver implements PathResolver {

	private ArrayList<File> paths = new ArrayList<File>();

	public FilePathResolver() {
	}
	
	public FilePathResolver(File ... paths) {
		for (File path : paths) add(path);
	}
	
	public FilePathResolver(String ... paths) {
		for (String path : paths) add(path);
	}
	
	public FilePathResolver(URL ... paths) {
		for (URL path : paths) add(path);
	}
	
	public FilePathResolver(URI ... paths) {
		for (URI path : paths) add(path);
	}
	
	/** interprets the given path as file path */
	public void add(String path) {
		add(new File(path));
	}
	
	/** Add a path to be searched for files */
	public void add(File path) {
		paths.add(path);
	}
	
	/** interprets the given path as file path. */
	public void add(Path path) {
		add(path.toFile());
	}

	/** interprets the given path as file path. */
	public void add(URI path) {
		add(new File(path.getPath()));
	}
	
	/** interprets the given path as file path. */
	public void add(URL path) {
		add(new File(path.getPath()));
	}
	
	
	/** 
	 * to be called from {@link GLSLSourceSet#resolve(File)}
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public InputStream resolve(File file) throws IOException {
		if (file.exists()) return new FileInputStream(file);
		
		for (File path : paths) {
			File f = new File(path, file.getPath());
			if (f.exists()) return new FileInputStream(f);
		}
		throw new FileNotFoundException(file.getPath());
	}

	/** interpretes the given path as file path */
	@Override
	public InputStream resolve(Path path) throws IOException {
		return resolve(path.toFile());
	}

	@Override
	public InputStream resolve(URL url) throws IOException {
		return PathResolver.FALLBACK.resolve(url);
	}

	@Override
	public InputStream resolve(URI uri) throws IOException {
		return PathResolver.FALLBACK.resolve(uri);
	}

	/** interpretes the given string as a file name */
	@Override
	public InputStream resolve(String sourceStringName) throws IOException {
		return resolve(new File(sourceStringName));
	}

	
}
