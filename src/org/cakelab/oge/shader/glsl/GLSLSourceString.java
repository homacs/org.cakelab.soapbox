package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.cakelab.appbase.fs.FileSystem;

/**
 * Single source string associated with a human readable name for debugging. 
 * <br/>
 * <p>
 * When calling the GLSL compiler via the OpenGL API, you can provide an 
 * array of strings, containing parts of the GLSL source code. Thus, 
 * the full GLSL source code of a shader is a concatenation of 
 * these source code strings.
 * </p><p>
 * If the compiler reports errors, he always adds the source 
 * string number which is the index of the source string in the
 * array, originally provided to him for compilation. The Shader 
 * classes (i.e. derived classes of {@link org.cakelab.org.oge.shader.Shader}),
 * accept {@link GLSLSourceString}s as input and decode compiler errors
 * to point out the actual path to the source (file, or anything) 
 * of an error.
 * </p><p>
 * This class also provides constructors to read the source string from 
 * files or input strings.
 * </p><p>
 * Use {@link GLSLSourceSet} if you need to combine multiple source strings 
 * into one glsl shader code and have kind of <code>#include</code> functionality
 * to support it.
 * </p>
 * @author homac
 *
 */
public class GLSLSourceString {
	private String code;
	private String name;

	public GLSLSourceString(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public GLSLSourceString(String name, InputStream glslSource) throws IOException {
		this(name, read(glslSource));
	}

	public GLSLSourceString(File glslSource) throws IOException {
		this(toName(glslSource), new FileInputStream(glslSource));
	}

	public String getName() {
		return name;
	}


	public String getCode() {
		return code;
	}

	/**
	 * Utility method which reads a string from input stream.
	 * 
	 * @param in Input stream to be read into a string
	 * @return
	 * @throws IOException
	 */
	static String read(InputStream in) throws IOException {
		return FileSystem.readText(in, Charset.defaultCharset());
	}

	/**
	 * Returns a human readable name referring to the given resource (e.g. its path).
	 * 
	 * @param resource
	 * @return Human readable name
	 * @throws IOException
	 */
	static String toName(File resource) throws IOException {
		return resource.getCanonicalPath();
	}

	/**
	 * Returns a human readable name referring to the given resource (e.g. its path).
	 * 
	 * @param resource
	 * @return Human readable name
	 * @throws IOException
	 */
	static String toName(Path resource) {
		return resource.toString();
	}

	/**
	 * Returns a human readable name referring to the given resource (e.g. its path).
	 * 
	 * @param resource
	 * @return Human readable name
	 * @throws IOException
	 */
	static String toName(URI resource) {
		return resource.toString();
	}

	/**
	 * Returns a human readable name referring to the given resource (e.g. its path).
	 * 
	 * @param resource
	 * @return Human readable name
	 * @throws IOException
	 */
	static String toName(URL resource) {
		return resource.toString();
	}

	/**
	 * Returns a human readable name referring to the given resource (e.g. its path).
	 * 
	 * @param resource
	 * @return Human readable name
	 * @throws IOException
	 */
	static String toName(String path) {
		return path;
	}


	/**
	 * Returns the string (GLSL source code) of this source string. Same as {@link #getCode()}.
	 */
	public String toString() {
		return code;
	}

	public static String toName(Object path) throws IOException {
		if (path instanceof File) return toName((File)path);
		else if (path instanceof String) return toName((String)path);
		else if (path instanceof URI) return toName((URI)path);
		else if (path instanceof URL) return toName((URL)path);
		return path.toString();
	}

}
