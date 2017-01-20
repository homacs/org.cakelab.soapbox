package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public interface PathResolver {
	
	public static final PathResolver FALLBACK = new PathResolver() {
		public InputStream resolve(File file) throws IOException {
			throw new FileNotFoundException(file.getPath());
		}

		public InputStream resolve(Path path) throws IOException {
			throw new IOException("can't resolve path '" + path.toString() + "'");
		}

		public InputStream resolve(URL url) throws IOException {
			throw new IOException("can't resolve url '" + url.toString() + "'");
		}

		public InputStream resolve(URI uri) throws IOException {
			throw new IOException("can't resolve uri '" + uri.toString() + "'");
		}

		public InputStream resolve(String sourceStringName) throws IOException {
			throw new IOException("can't resolve source string '" + sourceStringName + "'");
		}
	};

	
	InputStream resolve(File file) throws IOException;

	InputStream resolve(Path path) throws IOException;

	InputStream resolve(URL url) throws IOException;

	InputStream resolve(URI uri) throws IOException;

	InputStream resolve(String sourceStringName) throws IOException;

}
