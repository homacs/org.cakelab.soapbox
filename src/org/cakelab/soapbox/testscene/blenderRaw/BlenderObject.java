package org.cakelab.soapbox.testscene.blenderRaw;

import org.cakelab.oge.VisualObject;
import org.cakelab.oge.texture.Texture;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;

public abstract class BlenderObject extends VisualObject {

	private TriangleMesh triangleMesh;
	private Texture texture;

	public BlenderObject(TriangleMesh triangleMesh, Texture texture) {
		this.triangleMesh = triangleMesh;
		this.texture = texture;
	}

	public BlenderObject(TriangleMesh cubeMesh, Texture texture, float x, float y, float z) {
		super(x,y,z);
		this.triangleMesh = cubeMesh;
		this.texture = texture;
	}

	public Mesh getMesh() {
		return triangleMesh;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
