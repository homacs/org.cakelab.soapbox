package org.cakelab.oge.shader;

import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.cakelab.oge.shader.glsl.GLSLSourceSet;

public class VertexShader extends Shader {
	public static final int TYPE = GL_VERTEX_SHADER;

	public VertexShader(File glslSource) throws GLCompilerException, IOException {
		super(TYPE, glslSource);
	}

	public VertexShader(String name, File glslSource) throws GLCompilerException, IOException {
		super(TYPE, name, glslSource);
	}

	public VertexShader(String name, GLSLSourceSet source) {
		super(TYPE, name, source);
	}

	public VertexShader(String name, InputStream glslSource) throws GLCompilerException, IOException {
		super(TYPE, name, glslSource);
	}

	public VertexShader(String name, String glslSource) throws GLCompilerException {
		super(TYPE, name, glslSource);
	}

	public VertexShader(String name) {
		super(TYPE, name);
	}
	

}
