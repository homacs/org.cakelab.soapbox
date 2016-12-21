package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;

import org.cakelab.soapbox.model.Mesh;

public class VertexArrayObject {
	// TODO [2] vertex array objects and vertex attributes
	
	private int vaoId;
	private ArrayList<BufferObjectStatic> bufferObjects = new ArrayList<BufferObjectStatic>();
	private BufferObjectStatic arrayBuffer;

	public VertexArrayObject() {
		vaoId = glGenVertexArrays();
	}

	/**
	 * 
	 * This method creates a vertex array object and assigns a vertex 
	 * buffer object with the given mesh as data.
	 * 
	 * <p>{@code glUsageHint} is a hint to the GL implementation as to how a buffer object's data store will be accessed. This enables the GL implementation to make
	 * more intelligent decisions that may significantly impact buffer object performance. It does not, however, constrain the actual usage of the data store.
	 * {@code usage} can be broken down into two parts: first, the frequency of access (modification and usage), and second, the nature of that access. The
	 * frequency of access may be one of these:
	 * <ul>
	 * <li><em>STREAM</em> - The data store contents will be modified once and used at most a few times.</li>
	 * <li><em>STATIC</em> - The data store contents will be modified once and used many times.</li>
	 * <li><em>DYNAMIC</em> - The data store contents will be modified repeatedly and used many times.</li>
	 * </ul>
	 * The nature of access may be one of these:
	 * <ul>
	 * <li><em>DRAW</em> - The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.</li>
	 * <li><em>READ</em> - The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.</li>
	 * <li><em>COPY</em> - The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.</li>
	 * </ul></p>
	 *
	 * @param mesh data to be submitted to the vertex buffer.
	 * @param glAttribIndex_position The attribute index to be used for the mesh data.
	 * @param glUsageHint
	 * @param glStaticDraw 
	 */
	public VertexArrayObject(Mesh mesh, int glAttribIndex_position, int glUsageHint) {
		this();
		bind();
		arrayBuffer = new BufferObjectStatic(this, GL_ARRAY_BUFFER, mesh);
		declareVertexAttribute(glAttribIndex_position, 0, 3);
	}

	public void addBufferObject(BufferObjectStatic bufferObject) {
		bufferObjects.add(bufferObject);
	}

	public void bind() {
		glBindVertexArray(vaoId);
	}

	public void delete() {
		arrayBuffer.delete();
		glDeleteVertexArrays(vaoId);
	}

	/**
	 * 
	 * @param glAttribIndex
	 * @param startIndex
	 * @param size
	 */
	public void declareVertexAttribute(int glAttribIndex, int startIndex, int size) {
		glVertexAttribPointer(glAttribIndex, size, arrayBuffer.getElemType(), false, arrayBuffer.getStrideSize(), startIndex * arrayBuffer.getElemSize());
        glEnableVertexAttribArray(glAttribIndex);
	}
	
}
