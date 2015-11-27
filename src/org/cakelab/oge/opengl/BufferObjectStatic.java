package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import org.cakelab.soapbox.model.Mesh;
import org.lwjgl.opengl.GL15;

public class BufferObjectStatic {

	private int bufferObjectId;
	private int vaoBufferIndex;
	private int glElemType;
	private int strideSize;
	private int elemSize;

	public BufferObjectStatic(VertexArrayObject vao, int vaoBufferIndex, Mesh mesh) {
		this.vaoBufferIndex = vaoBufferIndex;
		this.glElemType = mesh.getElemType();
		elemSize = mesh.getElemSize();
		strideSize = mesh.getStrideSize();
		bufferObjectId = glGenBuffers();
		bind();
		glBufferData(vaoBufferIndex, mesh.getFloatBuffer(), GL15.GL_STATIC_DRAW);
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
