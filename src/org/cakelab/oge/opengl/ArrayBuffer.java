package org.cakelab.oge.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.BufferUtilsHelper;
import org.lwjgl.opengl.GL11;

public class ArrayBuffer<T extends Buffer> extends BufferObject {

	private int glElemType;
	private int strideSize;
	private int elemSize;
	
	public ArrayBuffer(T data, int strideSize, Usage usage) throws GLException {
		super(Target.ARRAY_BUFFER, usage);
		this.strideSize = strideSize;

		if (data instanceof FloatBuffer) {
			glElemType = GL11.GL_FLOAT;
			elemSize = BufferUtilsHelper.SIZEOF_FLOAT;
			data((FloatBuffer)data);
		} else if (data instanceof DoubleBuffer) {
			glElemType = GL11.GL_DOUBLE;
			elemSize = BufferUtilsHelper.SIZEOF_DOUBLE;
			data((DoubleBuffer)data);
		} else if (data instanceof IntBuffer) {
			glElemType = GL11.GL_INT;
			elemSize = BufferUtilsHelper.SIZEOF_INTEGER;
			data((IntBuffer)data);
		} else if (data instanceof ByteBuffer) {
			glElemType = GL11.GL_BYTE;
			elemSize = BufferUtilsHelper.SIZEOF_BYTE;
			data((ByteBuffer)data);
		} else throw new GLException("Unsupported type.");
	}
	
	public ArrayBuffer(int size, int strideSize, Usage usage) {
		super(Target.ARRAY_BUFFER, size, usage);
		this.strideSize = strideSize;
	}

	public int getElemType() {
		return glElemType;
	}

	public int getStrideSize() {
		return strideSize;
	}

	public int getElemSize() {
		return elemSize;
	}

}
