package org.cakelab.oge.shader;

import static org.lwjgl.opengl.GL32.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GeometryShader extends Shader {
	public static final int TYPE = GL_GEOMETRY_SHADER;
	
	public GeometryShader(String glslSource) throws GLCompilerException {
		super(TYPE, "vertex", glslSource);
	}

	public GeometryShader(String name, String glslSource) throws GLCompilerException {
		super(TYPE, name, glslSource);
	}

	public GeometryShader(File glslSource)
			throws GLCompilerException, IOException {
		super(TYPE, glslSource);
	}

	public GeometryShader(String name, File glslSource)
			throws GLCompilerException, IOException {
		super(TYPE, name, glslSource);
	}

	public GeometryShader(String name, InputStream glslSource) throws GLCompilerException, IOException {
		super(TYPE, name, glslSource);
	}
	
}
