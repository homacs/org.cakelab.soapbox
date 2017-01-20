package org.cakelab.oge.shader;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

public class Program {

	public static final int INVALID_PROGRAM_ID = -1;
	private String programName;
	private int programId;

	
	public Program(String name) {
		this.programName = name;
		this.setProgramId(glCreateProgram());
	}

	
	public Program(String name, Shader ... shaders) throws GLLinkerException 
	{
		this(name);
		for (Shader shader : shaders) attach(shader);
		link();
	}
	
	public void delete() {
		if (getProgramId() != INVALID_PROGRAM_ID) {
			GL20.glDeleteProgram(getProgramId());
			setProgramId(INVALID_PROGRAM_ID);
		}
	}
	
	public void link() throws GLLinkerException {
		
		glLinkProgram(programId);
		
		int status = glGetProgrami(programId, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			String message = glGetProgramInfoLog(programId);
			System.err.println(message);

			IntBuffer bufSize = BufferUtils.createIntBuffer(1);
			glGetProgramiv(programId, GL_INFO_LOG_LENGTH, bufSize);
			int len = bufSize.get(0);
			
			ByteBuffer infoLog = BufferUtils.createByteBuffer(Character.SIZE/8 * (len + 1));
			glGetProgramInfoLog(programId, bufSize, infoLog);
			
			String error = new String("Linker error in program " + programName + ":\n" + MemoryUtil.memUTF8(infoLog, bufSize.get(0)));
			throw new GLLinkerException(error);
		}
		
		glValidateProgram(programId);

	}
	
	public void attach(Shader shader) {
		if (shader != null) glAttachShader(getProgramId(), shader.shaderId);
	}


	public int getUniformLocation(String uniformName) throws GLException {
		int location = glGetUniformLocation(getProgramId(), uniformName);
		if (location == -1) {
			throw new GLException("uniform attribute "+ uniformName + " not found in program " + programName);
		}
		return location;
	}


	public int getProgramId() {
		return programId;
	}


	public void setProgramId(int programId) {
		this.programId = programId;
	}


}
