package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL43.*;

import java.nio.Buffer;
import java.util.ArrayList;

public class VertexArray {
	private int vaoId;
	
	protected ArrayList<ArrayBuffer<? extends Buffer>> bufferObjects = new ArrayList<ArrayBuffer<? extends Buffer>>();

	public VertexArray() {
		vaoId = glGenVertexArrays();
	}

	public int addBuffer(ArrayBuffer<? extends Buffer> buffer) {
		bufferObjects.add(buffer);
		return bufferObjects.size()-1;
	}

	public void replaceBuffer(int i, ArrayBuffer<? extends Buffer> buffer) {
		bufferObjects.set(i, buffer);
	}
	
	public ArrayBuffer<? extends Buffer> getBuffer(int bufferId) {
		return bufferObjects.get(bufferId);
	}
	
	/**
	 * Lowest level to declare the format of an attribute.
	 * 
	 * 
	 * @param attribIndex Application defined index to identify the 
	 *                    attribute, must be identical with layout modifier, such as: layout(location=attrIndex)
	 * @param size        Number of elements per vertex [1-4]
	 * @param type        Type of elements such as GL_FLOAT
	 * @param normalized  Whether values are already normalized [-1,1] or [0-1]
	 * @param offset      Offset of the first element in the buffer (and each subsequent stride)
	 */
	public void setAttributeFormat(int attribIndex, int size, int type, boolean normalized, int offset) {
		glVertexAttribFormat(attribIndex, size, type, false, offset);
	}

	/**
	 * Associates a buffer object with a given attribute.
	 * @param attribIndex Application defined index to identify the 
	 *                    attribute, must be identical with layout modifier, such as: layout(location=attrIndex)
	 * @param bufferIndex Index of the buffer received from {@link #add(ArrayBuffer)}
	 */
	public void setAttributeBinding(int attrIndex, int bufferIndex) {
		glVertexAttribBinding(attrIndex, bufferIndex);
	}
	
	/**
	 * 
	 * @param bufferIndex Index of the buffer received from {@link #add(ArrayBuffer)}
	 * @param offset      Start of the data.
	 * @param stride      Size [bytes] of each data chunk to be fetched for each vertex.
	 */
	public void setBufferBinding(int bufferIndex, int offset, int stride) {
		glBindVertexBuffer(bufferIndex, bufferObjects.get(bufferIndex).id, offset, stride);
	}
	
	
	public void bind() {
		glBindVertexArray(vaoId);
	}

	public void delete(boolean deleteBufferToo) {
		for (ArrayBuffer<? extends Buffer> buffer : bufferObjects) buffer.delete();
		glDeleteVertexArrays(vaoId);
	}
	
}
