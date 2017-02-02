package org.cakelab.oge.opengl;


import java.nio.Buffer;
import java.nio.FloatBuffer;

import org.cakelab.oge.opengl.BufferObject.Target;
import org.cakelab.oge.opengl.BufferObject.Usage;
import org.cakelab.oge.shader.GLException;
import org.cakelab.soapbox.model.Mesh;

public class MeshVertexArray extends SingleVertexArrayObject {

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
	 * @param attributeIndex The attribute index to be used for the mesh data.
	 * @param glUsageHint
	 * @param glStaticDraw 
	 * @throws GLException 
	 */
	public MeshVertexArray(Mesh mesh, int attributeIndex, Usage usage) throws GLException {
		super();
		bind();
		BufferObject<? extends Buffer> buffer = null;
		switch (usage) {
		case STATIC_DRAW:
			buffer = new BufferObjectStatic<FloatBuffer>(Target.ARRAY_BUFFER, mesh);
			break;
		default:
			throw new GLException("unsupported buffer type " + usage.toString());
		}
		super.set(buffer);
		// TODO consider storing length of the vertex vector
		int len = mesh.hasUVCoordinates()?mesh.getUVOffset():mesh.hasNormals()?mesh.getNormalsOffset():mesh.getStrideSize();
		declareVertexAttribute(attributeIndex, 0, len);
	}

}
