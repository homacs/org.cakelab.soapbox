package org.cakelab.oge.opengl;


import java.nio.Buffer;

import org.cakelab.oge.shader.GLException;
import org.cakelab.soapbox.model.Mesh;


public class BufferObjectStatic<T extends Buffer> extends BufferObject<T> {

	public BufferObjectStatic(Target target, Mesh mesh) throws GLException {
		super(target, mesh.getFloatBuffer(), mesh.getStrideSize(), Usage.STATIC_DRAW);
	}

}
