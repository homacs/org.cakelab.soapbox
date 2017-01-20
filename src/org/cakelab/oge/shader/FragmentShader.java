package org.cakelab.oge.shader;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.cakelab.oge.shader.glsl.GLSLSourceSet;

public class FragmentShader extends Shader {

	public static final int TYPE = GL_FRAGMENT_SHADER;

	public FragmentShader(File glslSource) throws GLCompilerException, IOException {
		super(TYPE, glslSource);
	}

	public FragmentShader(String name, File glslSource) throws GLCompilerException, IOException {
		super(TYPE, name, glslSource);
	}

	public FragmentShader(String name, GLSLSourceSet glslSource) {
		super(TYPE, name, glslSource);
	}

	public FragmentShader(String name, InputStream glslSource) throws GLCompilerException, IOException {
		super(TYPE, name, glslSource);
	}

	public FragmentShader(String name, String glslSource) throws GLCompilerException {
		super(TYPE, name, glslSource);
	}

	public FragmentShader(String name) {
		super(TYPE, name);
	}
	
	
}
