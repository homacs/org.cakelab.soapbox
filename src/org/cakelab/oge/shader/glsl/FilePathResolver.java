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

public class FilePathResolver implements PathResolver {

	private ArrayList<File> paths = new ArrayList<File>();

	/** Add a path to be searched for files */
	public void add(File path) {
		paths.add(path);
	}

	/** 
	 * to be called from {@link GLSLSourceSet#resolve(File)}
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream resolve(File file) throws FileNotFoundException {
		for (File path : paths) {
			File f = new File(path, file.getPath());
			if (f.exists()) return new FileInputStream(f);
		}
		throw new FileNotFoundException(file.getPath());
	}

	@Override
	public InputStream resolve(Path path) throws IOException {
		return PathResolver.FALLBACK.resolve(path);
	}

	@Override
	public InputStream resolve(URL url) throws IOException {
		return PathResolver.FALLBACK.resolve(url);
	}

	@Override
	public InputStream resolve(URI uri) throws IOException {
		return PathResolver.FALLBACK.resolve(uri);
	}

	@Override
	public InputStream resolve(String sourceStringName) throws IOException {
		return PathResolver.FALLBACK.resolve(sourceStringName);
	}
	
}
