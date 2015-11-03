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
	private VertexShader vertexShader;
	private FragmentShader fragmentShader;
	private int programId;

	
	public Program(String name) {
		this.programName = name;
		this.setProgramId(glCreateProgram());
	}

	
	public Program(String name, VertexShader vertexShader,
			FragmentShader fragmentShader) throws GLLinkerException 
	{
		this(name);
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		link();
	}
	
	public void delete() {
		if (getProgramId() != INVALID_PROGRAM_ID) {
			GL20.glDeleteProgram(getProgramId());
			setProgramId(INVALID_PROGRAM_ID);
		}
	}
	
	public void link() throws GLLinkerException {
		
		attach(vertexShader);
		attach(fragmentShader);
		glLinkProgram(getProgramId());
		glValidateProgram(getProgramId());
		
		IntBuffer status= BufferUtils.createIntBuffer(1);
		glGetProgram(getProgramId(), GL_LINK_STATUS, status);
		if (status.get() != GL_TRUE) {

			IntBuffer bufSize = BufferUtils.createIntBuffer(1);
			glGetProgram(getProgramId(), GL_INFO_LOG_LENGTH, bufSize);
			ByteBuffer infoLog = BufferUtils.createByteBuffer(Character.SIZE/8 * (bufSize.get(0) + 1));
			glGetProgramInfoLog(getProgramId(), bufSize, infoLog);
			String error = new String("Linker error in program " + programName + ":\n" + MemoryUtil.memDecodeUTF8(infoLog, bufSize.get(0)));
			throw new GLLinkerException(error);
		}

	}
	
	private void attach(Shader shader) {
		if (shader != null) glAttachShader(getProgramId(), shader.shaderId);
	}


	public int getUniformLocation(String uniformName) {
		return glGetUniformLocation(getProgramId(), uniformName);
	}


	public int getProgramId() {
		return programId;
	}


	public void setProgramId(int programId) {
		this.programId = programId;
	}


}
