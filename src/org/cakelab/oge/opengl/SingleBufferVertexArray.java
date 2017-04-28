package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.Buffer;

public class SingleBufferVertexArray extends VertexArray {
	private static final int NOT_INITIALIZED = -1;
	
	
	int bufferIndex = NOT_INITIALIZED;
	
	public SingleBufferVertexArray() {
		super();
	}

	public void set(ArrayBuffer<? extends Buffer> buffer) {
		if (bufferIndex == NOT_INITIALIZED) bufferIndex = addBuffer(buffer);
		else replaceBuffer(bufferIndex, buffer);
	}

	/**
	 * @param attrIndex  id of the attribute
	 * @param startIndex index of the element in the array (not its offset)
	 * @param size       number of elements of the array
	 */
	public void declareAttribute(int attrIndex, int startIndex, int size) {
		ArrayBuffer<? extends Buffer> bufferObject = getBuffer(bufferIndex);
		glVertexAttribPointer(attrIndex, size, bufferObject.getElemType(), false, bufferObject.getStrideSize(), startIndex * bufferObject.getElemSize());
        glEnableVertexAttribArray(attrIndex);
	}

	public ArrayBuffer<? extends Buffer> getBuffer() {
		return getBuffer(bufferIndex);
	}
	
}
