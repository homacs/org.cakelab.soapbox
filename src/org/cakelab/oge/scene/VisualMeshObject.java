package org.cakelab.oge.scene;

import org.cakelab.soapbox.model.Mesh;

public class VisualMeshObject extends VisualObject {

	private Mesh mesh;
	public VisualMeshObject(Mesh mesh, Material material) {
		this(mesh, material, 0, 0, 0);
	}
	public VisualMeshObject(Mesh mesh, Material material, float x, float y, float z) {
		super(material, x, y, z);
		this.mesh = mesh;
	}
	public Mesh getMesh() {
		return mesh;
	}

	

}
