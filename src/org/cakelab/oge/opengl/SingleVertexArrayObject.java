package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.Buffer;

public class SingleVertexArrayObject extends VertexArrayObject {

	public SingleVertexArrayObject() {
		super();
	}

	public void set(BufferObject<? extends Buffer> buffer) {
		if (bufferObjects.size() == 0) bufferObjects.add(buffer);
		else bufferObjects.set(0, buffer);
	}

	/**
	 * 
	 * @param glAttribIndex
	 * @param startIndex
	 * @param size
	 */
	public void declareVertexAttribute(int glAttribIndex, int startIndex, int size) {
		BufferObject<? extends Buffer> bufferObject = bufferObjects.get(0);
		glVertexAttribPointer(glAttribIndex, size, bufferObject.getElemType(), false, bufferObject.getStrideSize(), startIndex * bufferObject.getElemSize());
        glEnableVertexAttribArray(glAttribIndex);
	}
	
}
