package org.cakelab.oge.opengl;


import java.nio.Buffer;

import org.cakelab.oge.shader.GLException;
import org.cakelab.soapbox.model.Mesh;


public class StaticArrayBuffer<T extends Buffer> extends ArrayBuffer<T> {

	@SuppressWarnings("unchecked")
	public StaticArrayBuffer(Mesh mesh) throws GLException {
		super(((T)mesh.getFloatBuffer()), mesh.getStrideSize(), Usage.STATIC_DRAW);
	}

}
