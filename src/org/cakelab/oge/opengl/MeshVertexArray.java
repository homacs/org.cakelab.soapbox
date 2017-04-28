package org.cakelab.oge.opengl;


import java.nio.Buffer;
import java.nio.FloatBuffer;

import org.cakelab.oge.opengl.BufferObject.Usage;
import org.cakelab.oge.shader.GLException;
import org.cakelab.soapbox.model.Mesh;

public class MeshVertexArray extends SingleBufferVertexArray {
	
	public MeshVertexArray(Mesh mesh, int attributeIndex, Usage usage) throws GLException {
		super();
		bind();
		ArrayBuffer<? extends Buffer> buffer = null;
		switch (usage) {
		case STATIC_DRAW:
			buffer = new StaticArrayBuffer<FloatBuffer>(mesh);
			break;
		default:
			throw new GLException("unsupported buffer type " + usage.toString());
		}
		super.set(buffer);
		// TODO consider storing length of the vertex vector
		int len = mesh.hasUVCoordinates()?mesh.getUVOffset():mesh.hasNormals()?mesh.getNormalsOffset():mesh.getStrideSize();
		declareAttribute(attributeIndex, 0, len);
	}

}
