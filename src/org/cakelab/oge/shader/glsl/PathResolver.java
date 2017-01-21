package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * Instances of {@link PathResolver}'s provide the functionality 
 * of path variables in build environments for {@link GLSLSourceStringSet}s.
 * 
 * A path resolver basically resolves relative paths given to include 
 * methods of {@link GLSLSourceStringSet} to actual existing resources, 
 * such as files, using the set of search paths given to them.
 *
 * Usage goes like this:
 * <ul>
 * <li> You create a single path resolver, </li>
 * <li> add the various paths it should search when called </li>
 * <li> and use it in all source string sets.
 * </ul>
 * 
 * To combine multiple path resolvers have a look at {@link MetaPathResolver}.
 * 
 * 
 * @author homac
 *
 */
public interface PathResolver {

	/* I would have loved to use generics here but there are 
	 * all kinds of barriers which will slow down a generic 
	 * implementation significantly.
	 */
	
	/**
	 * This is the fallback path resolver used if no other path resolver was provided. 
	 * It basically throws {@link FileNotFoundExceptio}s with the given path argument.
	 * 
	 * So, if you implement another path resolver, you can use {@link #FALLBACK} as
	 * general fallback :D
	 */
	public static final PathResolver FALLBACK = new PathResolver() {
		
		@Override
		public InputStream resolve(File file) throws IOException {
			throw new FileNotFoundException("can't resolve file '" + file.getPath() + "'");
		}

		@Override
		public InputStream resolve(Path path) throws IOException {
			throw new FileNotFoundException("can't resolve path '" + path.toString() + "'");
		}

		@Override
		public InputStream resolve(URL url) throws IOException {
			throw new FileNotFoundException("can't resolve url '" + url.toString() + "'");
		}

		@Override
		public InputStream resolve(URI uri) throws IOException {
			throw new FileNotFoundException("can't resolve uri '" + uri.toString() + "'");
		}

		@Override
		public InputStream resolve(String sourceStringName) throws IOException {
			throw new FileNotFoundException("can't resolve source string '" + sourceStringName + "'");
		}

		@Override
		public void add(String path) {
			throw new RuntimeException("not supported by FALLBACK path resolver");
		}

		@Override
		public void add(File path) {
			throw new RuntimeException("not supported by FALLBACK path resolver");
		}

		@Override
		public void add(Path path) {
			throw new RuntimeException("not supported by FALLBACK path resolver");
		}

		@Override
		public void add(URI path) {
			throw new RuntimeException("not supported by FALLBACK path resolver");
		}

		@Override
		public void add(URL path) {
			throw new RuntimeException("not supported by FALLBACK path resolver");
		}
	};

	
	
	/** Add a path to the set of search paths. */
	void add(String path) throws IOException;
	/** Add a path to the set of search paths. */
	void add(File path) throws IOException;
	/** Add a path to the set of search paths. */
	void add(Path path) throws IOException;
	/** Add a path to the set of search paths. */
	void add(URI path) throws IOException;
	/** Add a path to the set of search paths. */
	void add(URL path) throws IOException;

	/**
	 * Method resolves a given path to an existing resource by prepending the 
	 * search paths.
	 * 
	 * A miss is indicated by returning null.
	 * 
	 * @param file Relative path of the resource to be found
	 * @return input stream to the given resource or null if not found.
	 * @throws IOException Thrown in case an IO error happened during path resolution.
	 */
	InputStream resolve(File file) throws IOException;

	/**
	 * Method resolves a given path to an existing resource by prepending the 
	 * search paths.
	 * 
	 * A miss is indicated by returning null.
	 * 
	 * @param file Relative path of the resource to be found
	 * @return input stream to the given resource or null if not found.
	 * @throws IOException Thrown in case an IO error happened during path resolution.
	 */
	InputStream resolve(Path path) throws IOException;

	/**
	 * Method resolves a given path to an existing resource by prepending the 
	 * search paths.
	 * 
	 * A miss is indicated by returning null.
	 * 
	 * @param file Relative path of the resource to be found
	 * @return input stream to the given resource or null if not found.
	 * @throws IOException Thrown in case an IO error happened during path resolution.
	 */
	InputStream resolve(URL url) throws IOException;

	/**
	 * Method resolves a given path to an existing resource by prepending the 
	 * search paths.
	 * 
	 * A miss is indicated by returning null.
	 * 
	 * @param file Relative path of the resource to be found
	 * @return input stream to the given resource or null if not found.
	 * @throws IOException Thrown in case an IO error happened during path resolution.
	 */
	InputStream resolve(URI uri) throws IOException;

	/**
	 * Method resolves a given path to an existing resource by prepending the 
	 * search paths.
	 * 
	 * A miss is indicated by returning null.
	 * 
	 * @param file Relative path of the resource to be found
	 * @return input stream to the given resource or null if not found.
	 * @throws IOException Thrown in case an IO error happened during path resolution.
	 */
	InputStream resolve(String sourceStringName) throws IOException;

}
