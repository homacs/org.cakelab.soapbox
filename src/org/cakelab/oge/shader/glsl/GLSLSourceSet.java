package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;

import org.cakelab.appbase.fs.FileSystem;

/**
 * This is supposed to resemble the functionality usually 
 * provided by the #include directive in cpp.
 * 
 * But here you use the include() methods to include strings.
 * 
 * This class just provides the functionality to assemble
 * the strings. A subclass is meant to represent the source
 * code structure in terms of subsequent calls to add() in 
 * its constructor. For example like this:
 * 
 * class MyShaderSource {
 *    public MyShaderSource (File path) {
 *         include("common-version", "#version 430 core");
 *         include(new File("preamble.glsl"));
 *         include(new File("tools.glsl"));
 *         include(new File("myshader-main.glsl"));
 *    }
 *    
 *    // example of a resolve method
 *    public InputStream resolve(File f) throws IOException {
 *    		return new FileInputStream(path, f);
 *    }
 * }
 * 
 * The names of files or explicitly provided are used to 
 * refer to them in case of compiler errors.
 * 
 * If a GLSLSource object is included by another GLSLSource 
 * object, it will add all the source string of the included
 * object.
 * 
 * <h3>Include Paths</h3>
 * When you include a file which does not exist in the current
 * working directory, this class will call the {@link #resolve(File)}
 * method, which is meant to search include paths for the given file. 
 * 
 * The <code>resolve</code> methods in this class have no functionality
 * and will just throw an exception, but you can override them in a 
 * subclass. 
 * 
 * @author homac
 *
 */
public abstract class GLSLSourceSet {
	
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> sources = new ArrayList<String>();

	private PathResolver resolver = PathResolver.FALLBACK;
	
	
	/**
	 * Returns the name that was originally associated with a source string.
	 * This is needed to determine source of errors received from the GLSL 
	 * compiler. The compiler refers to the source strings by its index in 
	 * the source string array (see {@link #getSourceStrings()}).
	 * 
	 * @param sourceStringIndex Index received through an error message of the compiler.
	 * @return Name associated with the source string (e.g. a path).
	 */
	public String getName(int sourceStringIndex) {
		return names.get(sourceStringIndex);
	}
	
	/**
	 * Returns the array of source string in the order they have been added to this.
	 * This is in needed by the compiler.
	 * 
	 * @return
	 */
	public String[] getSourceStrings() {
		return sources.toArray(new String[0]);
	}

	/**
	 * Includes another source at the end of this source.
	 * @param source
	 */
	public void include(GLSLSource source) {
		for (String n : source.getNames()) names.add(n);
		for (String s : source.getSourceStrings()) sources.add(s);
	}
	
	/**
	 * Add the given source string to the end of the list of source strings.
	 * @param name Name used to refer to the sources string in case of compiler errors.
	 * @param source The source string
	 */
	public void include(String name, String source) {
		names.add(name);
		sources.add(source);
	}

	/**
	 * reads the entire file in a string and uses its file name to identify it.
	 * 
	 * If the file does not exist in the current path, this method will 
	 * call {@link #resolve(File)}. This method has to be implemented by a 
	 * sub class.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void include(File file) throws IOException {
		InputStream in = null;
		if (!file.exists()) in = resolver.resolve(file);
		else in = new FileInputStream(file);
		include(file.getCanonicalPath(), in);
	}

	/**
	 * Includes a resources identified by path as source string.
	 * This method calls {@link #resolve(Path)} to resolve it to an
	 * input stream.
	 * @param path absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(Path path) throws IOException {
		include(path.toString(), resolver.resolve(path));
	}
	
	/**
	 * Includes a resources identified by url as source string.
	 * This method calls {@link #resolve(URL)} to resolve it to an
	 * input stream.
	 * @param url absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(URL url) throws IOException {
		include(url.toString(), resolver.resolve(url));
	}

	/**
	 * Includes a resources identified by path as source string.
	 * This method calls {@link #resolve(String)} to resolve it to an
	 * input stream.
	 * @param path absolute or relative path to a resource to be read.
	 * @throws IOException
	 */
	public void include(String path) throws IOException {
		include(path, resolver.resolve(path));
	}

	/**
	 * Reads all from the given input stream and stores it in 
	 * one source string, which will be referred to by 'name'.
	 * 
	 * @param name The name to be associated with the source string.
	 * @param in The data to be read.
	 * @throws IOException 
	 */
	public void include(String name, InputStream in) throws IOException {
		String source = FileSystem.readText(in, Charset.defaultCharset());
		include(name, source);
	}

	/**
	 * Returns a GLSL compliant concatenation of all sources strings 
	 * contained in the set.
	 * Compliant means, it will add #line directives to tell the compiler 
	 * the actual origin of the strings.
	 */
	public String toString() {
		StringBuffer out = new StringBuffer();
		// no #line directive before the #version directive
		toString(out, 0);
		for (int i = 1; i < sources.size(); i++) {
			out.append("#line 0 ").append(i).append('\n');
			toString(out,i);
		}
		return out.toString();
	}

	private void toString(StringBuffer out, int i) {
		out.append("/* start of '").append(names.get(i)).append("' */\n");
		out.append(sources.get(0));
		out.append("/* end of '" + names.get(0) + "' */\n");
	}

}
