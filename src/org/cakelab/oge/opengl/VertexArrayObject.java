package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.Buffer;
import java.util.ArrayList;

public class VertexArrayObject {
	private int vaoId;
	
	protected ArrayList<BufferObject<? extends Buffer>> bufferObjects = new ArrayList<BufferObject<? extends Buffer>>();

	public VertexArrayObject() {
		vaoId = glGenVertexArrays();
	}

	public void set(BufferObject<? extends Buffer> buffer) {
		bufferObjects.add(buffer);
	}

	public void bind() {
		glBindVertexArray(vaoId);
		
	}

	public void delete(boolean deleteBufferToo) {
		for (BufferObject<? extends Buffer> buffer : bufferObjects) buffer.delete();
		glDeleteVertexArrays(vaoId);
	}
	
}
