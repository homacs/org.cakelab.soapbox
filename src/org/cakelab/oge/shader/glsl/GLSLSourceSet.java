package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;


/**
 * A class to maintain source string sets.
 * <p>
 * First of all: You don't need it if you just use one 
 * glsl source string per shader.
 * </p><p>
 * This class is supposed to resemble the functionality usually 
 * provided by the #include directive in cpp. But here you use 
 * the <code>include()</code> methods to include strings and 
 * the include path is defined in a {@link PathResolver}.
 * </p><p>
 * This class just provides the functionality to assemble
 * the source code strings. A subclass is meant to represent 
 * the source code structure in terms of subsequent calls to 
 * for example {@link #include(File)}. 
 * </p><p>
 * For example like this:
 * </p>
 * <pre>
 * class MyShaderSource extends GLSLSourceSet {
 * 
 *    public MyShaderSource (PathResolver path) {
 *    	   super(path);
 *    
 *         // include the source strings
 *    	   include("common-version", "#version 430 core");
 *         include(new File("preamble.glsl"));
 *         include(new File("tools.glsl"));
 *         include(new File("myshader-main.glsl"));
 *    }
 *    
 * }
 * </pre>
 * <p>
 * Names of files or explicitly provided names are used to 
 * refer to the source strings in case of compiler errors.
 * </p><p>
 * If a  {@link GLSLSourceSet} object is included by another  {@link GLSLSourceSet} 
 * object, it will add all the source string of the included
 * object.
 * </p>
 * <h3>Include Paths and Resource Resolution</h3>
 * When you include a resource, such as a file, this class will call its {@link PathResolver}
 * to resolve it to an input stream. The path resolver allows to set include paths 
 * and resolve a relative path to an existing resource.
 * </p><p>
 * By default, a {@link GLSLSourceSet} does not support path resolution and 
 * will throw FileNotFoundException if path resolution was necessary until a 
 * proper path resolver was set.
 * </p><p>
 * Path resolution is used only in the following methods:
 * <ul>
 * <li>{@link #include(String)}</li>
 * <li>{@link #include(File)}</li>
 * <li>{@link #include(Path)}</li>
 * <li>{@link #include(URL)}</li>
 * <li>{@link #include(URI)}</li>
 * </ul>
 * </p><p>
 * But path resolution is <b>NOT</b> use in the following methods:
 * <ul>
 * <li>{@link #include(String, String)}</li>
 * <li>{@link #include(String, InputStream)}</li>
 * <li>{@link #include(GLSLSourceString)}</li>
 * <li>{@link #include(GLSLSourceSet)}</li>
 * </ul>
 * Thus, if you don't want or need to maintain paths, for example 
 * you have all source string actually as string in the code, then 
 * you can still use this class to collect them.
 * </p>
 * 
 * @author homac
 *
 */
public abstract class GLSLSourceSet implements Iterable<GLSLSourceString>{
	
	private int capacity = 8;
	private int size = 0;
	
	private GLSLSourceString[] sources = new GLSLSourceString[capacity];

	private PathResolver resolver = PathResolver.FALLBACK;
	
	/**
	 * Default constructor.
	 * 
	 * Path resolution is not supported until a path resolver 
	 * is set (see {@link #setResolver(PathResolver)}.
	 */
	public GLSLSourceSet() {
	}
	
	/**
	 * Instantiates a source set, which uses the given path resolver to resolve
	 * paths.
	 * 
	 * @param resolver
	 */
	public GLSLSourceSet(PathResolver resolver) {
		this.resolver = resolver;
	}
	
	
	/**
	 * Returns an iterator to the array of source strings.
	 * 
	 * @return
	 */
	public Iterator<GLSLSourceString> iterator() {
		return new Iterator<GLSLSourceString>() {
			int i = 0;
			
			@Override
			public boolean hasNext() {
				return i < capacity;
			}

			@Override
			public GLSLSourceString next() {
				return sources[i];
			}
		};
	}

	/**
	 * Set the path resolver for this instance.
	 * @param resolver
	 */
	public void setResolver(PathResolver resolver) {
		this.resolver = resolver;
	}

	/**
	 * @return The path resolver used by this instance.
	 */
	public PathResolver getResolver() {
		return resolver;
	}

	/**
	 * Includes another source at the end of this source.
	 * 
	 * This method does not use path resolution.
	 * 
	 * @param sourceSet
	 */
	public void include(GLSLSourceSet sourceSet) {
		capacity(+sourceSet.size());
		System.arraycopy(sourceSet.sources, 0, sources, size, sourceSet.size);
		size += sourceSet.size();
	}

	/**
	 * includes the given source string at the end of the list.
	 * 
	 * This method does not use path resolution.
	 * 
	 * @param source
	 */
	public void include(GLSLSourceString source) {
		capacity(+1);
		sources[size++] = source;
	}

	/**
	 * Add the given source string to the end of the list of source strings.
	 * 
	 * This method does not use path resolution.
	 * 
	 * @param name Name used to refer to the sources string in case of compiler errors.
	 * @param sourceString The source string
	 */
	public void include(String name, String sourceString) {
		include(new GLSLSourceString(name, sourceString));
	}

	/**
	 * 
	 * Reads all from the given input stream and stores it in 
	 * one source string, which will be referred to by 'name'.
	 *
	 * This method does not use path resolution.
     *
	 * @param name The name to be associated with the source string.
	 * @param in The data to be read.
	 * @throws IOException 
	 */
	public void include(String name, InputStream in) throws IOException {
		include(name, GLSLSourceString.read(in));
	}

	/**
	 * Includes a resources identified by path as source string.
	 * Uses path resolver to resolve the given path to a resource. 
	 * @param path absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(String path) throws IOException {
		InputStream in = resolver.resolve(path);
		if (in == null) throw new FileNotFoundException(GLSLSourceString.toName(path));
		include(GLSLSourceString.toName(path), in);
	}

	/**
	 * Uses path resolver to resolve the given path to a resource. Use {@link FilePathResolver} 
	 * for file path resolution.
	 * 
	 * Reads the entire file in a string and uses its file path to identify it.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void include(File path) throws IOException {
		InputStream in = resolver.resolve(path);
		if (in == null) throw new FileNotFoundException(GLSLSourceString.toName(path));
		include(GLSLSourceString.toName(path), in);
	}

	/**
	 * Includes a resources identified by path as source string.
	 * Uses path resolver to resolve the given path to a resource. 
	 * @param path absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(Path path) throws IOException {
		InputStream in = resolver.resolve(path);
		if (in == null) throw new FileNotFoundException(GLSLSourceString.toName(path));
		include(GLSLSourceString.toName(path), in);
	}
	
	/**
	 * Includes a resources identified by url as source string.
	 * Uses path resolver to resolve the given path to a resource. 
	 * @param url absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(URL path) throws IOException {
		InputStream in = resolver.resolve(path);
		if (in == null) throw new FileNotFoundException(GLSLSourceString.toName(path));
		include(GLSLSourceString.toName(path), in);
	}

	/**
	 * Includes a resources identified by url as source string.
	 * Uses path resolver to resolve the given path to a resource. 
	 * @param uri absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(URI path) throws IOException {
		InputStream in = resolver.resolve(path);
		if (in == null) throw new FileNotFoundException(GLSLSourceString.toName(path));
		include(GLSLSourceString.toName(path), in);
	}

	/**
	 * Amount of source strings added to the set.
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns a GLSL compliant concatenation of all sources strings 
	 * contained in the set.
	 * <p>
	 * Compliant means, it will add <code>#line</code> directives to tell the compiler 
	 * the actual origin of the strings.
	 * </p><p>
	 * The string will also contain comments, which explain where a 
	 * certain source string starts and ends and where it originates from.
	 * </p>
	 */
	public String toString() {
		return toString(true, true, "\n");
	}

	/**
	 * Concatenates all source strings into one (mostly for debugging purposes).
	 * <p>
	 * The method can add GLSL compliant #line directives (withLineDirectives = true)
	 * and additional comments (withComments = true) to explain where a certain string
	 * originates from.  
	 * </p><p>
	 * The method will also add the 'separator' between the concatenated source strings, 
	 * if separator is not null.
	 * </p><p>
	 * If withLineDirectives or withComments is true, the method has to add line breaks.
	 * </p>
	 * 
	 * 
	 * @param withLineDirectives Will add #line directives if true.
	 * @param withComments Will add comments to explain origin of the string if true
	 * @param separator Will add the separator string between source strings if !null
	 * @return concatenation of all source strings contained in this list.
	 */
	public String toString(boolean withLineDirectives, boolean withComments, String separator) {
		StringBuffer out = new StringBuffer();
		if (withLineDirectives) {
			// no #line directive before the #version directive
			toString(out, 0, withComments);
			for (int i = 1; i < sources.length; i++) {
				if (separator != null) out.append(separator);
				if (withLineDirectives) {
					if (out.charAt(out.length()-1) != '\n') out.append('\n');
					out.append("#line 0 ").append(i).append('\n');
				}
				toString(out, i, withComments);
			}
		} else {
			toString(out, 0, withComments);
			for (int i = 1; i < sources.length; i++) {
				if (separator != null) out.append(separator);
				toString(out, i, withComments);
			}
		}
		return out.toString();
	}


	/**
	 * Converts the n'th source string into a string, appending comments and a separator if requested.
	 * @param out Buffer to be extended with the generated string
	 * @param n Index of the source string
	 * @param withComments whether to add comments or not
	 * @param separator separator to be added after the source string
	 */
	protected void toString(StringBuffer out, int n, boolean withComments) {
		String name = sources[n].getName();
		if (withComments) out.append("// start of '").append(name).append("' \n");
		out.append(sources[0]);
		if (withComments) out.append("// end   of '").append(name).append("' \n");
	}

	/**
	 * checks current capacity the sources array, if it would fit the addition of n more elements.
	 * @param n
	 */
	private void capacity(int n) {
		int required = size+n;
		if (required > capacity) {
			capacity <<= 1;
			sources = Arrays.copyOf(sources, capacity);
		}
	}



}
