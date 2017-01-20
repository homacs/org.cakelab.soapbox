package org.cakelab.oge.shader.glsl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Lazy implementation of a single source string.
 * 
 * @author homac
 *
 */
public class GLSLSourceString extends GLSLSourceSet {
	public GLSLSourceString(String name, String code) {
		include(name, code);
	}

	public GLSLSourceString(String name, InputStream glslSource) throws IOException {
		include(name, glslSource);
	}

	public GLSLSourceString(File glslSource) throws IOException {
		include(glslSource);
	}
}
