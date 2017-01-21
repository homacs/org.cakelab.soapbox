package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Path resolver which delegates each resolve request to its 
 * delegates.
 * 
 * Delegates can be given to the constructor or the method 
 * {@link #add(PathResolver)}.
 * 
 * The meta resolver will enumerate and call all delegates 
 * and the delegates will search their paths and possibly 
 * call their delegates until the path is either resolved 
 * to an existing resource.
 * 
 * If no resource is found, the resolve method will finally
 * throw a FileNotFoundException.
 * 
 * 
 * Obviously, a {@link MetaPathResolver} can be added to 
 * another instance of {@link MetaPathResolver} which allows
 * for modular path construction. For example project P1 uses path 
 * resolver A and another project P2 uses path resolver B, but
 * also needs parts of P1, then A and B can be combined using 
 * B.add(A) (or A.add(B) or new MetaPathResolver(A,B)). 
 * The advantage is, that you don't need to worry about what 
 * type of resources those in project P1 are or where exactly
 * they are.
 * 
 * 
 * @author homac
 *
 */
public class MetaPathResolver implements PathResolver {

	private ArrayList<PathResolver> delegates = new ArrayList<PathResolver>();


	/**
	 * Creates an instance with an empty list of delegates. 
	 * Thus, any call to resolve will throw FileNotFoundException, 
	 * unless {@link #add(PathResolver)} was called.
	 */
	public MetaPathResolver() {
	}

	/**
	 * Constructor taking an initial set of delegates
	 * @param delegates
	 */
	public MetaPathResolver(PathResolver ... delegates) {
		for (PathResolver delegate : delegates) add(delegate);
	}

	/**
	 * Add another resolver to the list of delegates.
	 * @param delegate
	 */
	public void add(PathResolver delegate) {
		delegates.add(delegate);
	}

	/**
	 * not supported by MetaPathResolver
	 */
	@Override
	public void add(String path) {
		throwNotSupportedException();
	}

	/**
	 * not supported by MetaPathResolver
	 */
	@Override
	public void add(File path) {
		throwNotSupportedException();
	}

	/**
	 * not supported by MetaPathResolver
	 */
	@Override
	public void add(Path path) {
		throwNotSupportedException();
	}

	/**
	 * not supported by MetaPathResolver
	 */
	@Override
	public void add(URI path) {
		throwNotSupportedException();
	}

	/**
	 * not supported by MetaPathResolver
	 */
	@Override
	public void add(URL path) {
		throwNotSupportedException();
	}

	
	/** 
	 * Calls its delegates to resolve the given resource path to an existing resource.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@Override
	public InputStream resolve(File path) throws IOException {
		for (PathResolver delegate : delegates ) {
			try {
				InputStream in = delegate.resolve(path);
				if (in != null) return in;
			} catch (IOException e) {
				// ignore file not found exceptions for now
			}
		}
		return null;
	}

	@Override
	public InputStream resolve(Path path) throws IOException {
		for (PathResolver delegate : delegates ) {
			try {
				
				InputStream in = delegate.resolve(path);
				if (in != null) return in;
			} catch (IOException e) {
				// ignore file not found exceptions for now
			}
		}
		throw new FileNotFoundException(GLSLSourceString.toName(path));
	}

	@Override
	public InputStream resolve(URL path) throws IOException {
		for (PathResolver delegate : delegates ) {
			try {
				
				InputStream in = delegate.resolve(path);
				if (in != null) return in;
			} catch (IOException e) {
				// ignore file not found exceptions for now
			}
		}
		throw new FileNotFoundException(GLSLSourceString.toName(path));
	}

	@Override
	public InputStream resolve(URI path) throws IOException {
		for (PathResolver delegate : delegates ) {
			try {
				
				InputStream in = delegate.resolve(path);
				if (in != null) return in;
			} catch (IOException e) {
				// ignore file not found exceptions for now
			}
		}
		throw new FileNotFoundException(GLSLSourceString.toName(path));
	}

	@Override
	public InputStream resolve(String path) throws IOException {
		for (PathResolver delegate : delegates ) {
			try {
				
				InputStream in = delegate.resolve(path);
				if (in != null) return in;
			} catch (IOException e) {
				// ignore file not found exceptions for now
			}
		}
		throw new FileNotFoundException(GLSLSourceString.toName(path));
	}
	
	
	private void throwNotSupportedException() {
		throw new Error("not supported by " + MetaPathResolver.class.getName());
	}

}
