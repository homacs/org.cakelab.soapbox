package org.cakelab.soapbox.testscene.blenderRaw;

import org.cakelab.oge.VisualObject;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;

public abstract class BlenderObject extends VisualObject {

	private TriangleMesh triangleMesh;

	public BlenderObject(TriangleMesh triangleMesh) {
		this.triangleMesh = triangleMesh;
	}

public BlenderObject(TriangleMesh cubeMesh, float x, float y, float z) {
		super(x,y,z);
		this.triangleMesh = cubeMesh;
	}

	public Mesh getMesh() {
		return triangleMesh;
	}
}
