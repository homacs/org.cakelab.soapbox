package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL15.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.BufferUtilsHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public class BufferObject<T extends Buffer> {

	public enum Usage {
		STATIC_DRAW(GL15.GL_STATIC_DRAW);
		
		public final int v;
		
		Usage(int glValue) {
			this.v = glValue;
		}
	}
	
	public static enum Target {
		ARRAY_BUFFER(GL_ARRAY_BUFFER);
		public final int v;
		
		Target(int glValue) {
			this.v = glValue;
		}
	}

	private int bufferObjectId;
	private int vaoBufferIndex;
	private int glElemType;
	private int strideSize;
	private int elemSize;

	
	
	
	public <B extends Buffer> BufferObject(Target target, B data, int strideSize, Usage usage) throws GLException {
		this.vaoBufferIndex = target.v;
		this.strideSize = strideSize;
		this.bufferObjectId = glGenBuffers();
		bind();
		
		if (data instanceof FloatBuffer) {
			glElemType = GL11.GL_FLOAT;
			elemSize = BufferUtilsHelper.SIZEOF_FLOAT;
			glBufferData(target.v, (FloatBuffer)data, usage.v);
		} else if (data instanceof DoubleBuffer) {
			glElemType = GL11.GL_DOUBLE;
			elemSize = BufferUtilsHelper.SIZEOF_DOUBLE;
			glBufferData(target.v, (DoubleBuffer)data, usage.v);
		} else if (data instanceof IntBuffer) {
			glElemType = GL11.GL_INT;
			elemSize = BufferUtilsHelper.SIZEOF_INTEGER;
			glBufferData(target.v, (IntBuffer)data, usage.v);
		} else if (data instanceof ByteBuffer) {
			glElemType = GL11.GL_BYTE;
			elemSize = BufferUtilsHelper.SIZEOF_BYTE;
			glBufferData(target.v, (ByteBuffer)data, usage.v);
		} else throw new GLException("Unsupported type. Use the manual constructor.");
	}

	
	
	public void bind() {
		glBindBuffer(vaoBufferIndex, bufferObjectId);
	}

	public int getElemType() {
		return glElemType;
	}

	public int getStrideSize() {
		return strideSize;
	}

	public void delete() {
		glDeleteBuffers(bufferObjectId);
	}

	public int getElemSize() {
		return elemSize;
	}

}
