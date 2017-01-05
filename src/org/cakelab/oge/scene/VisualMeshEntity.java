package org.cakelab.oge.scene;

import org.cakelab.soapbox.model.Mesh;

public class VisualMeshEntity extends VisualEntity {

	protected Mesh mesh;
	
	public VisualMeshEntity(Mesh mesh, Material material) {
		this(mesh, material, 0, 0, 0);
	}
	public VisualMeshEntity(Mesh mesh, Material material, float x, float y, float z) {
		super(material, x, y, z);
		this.mesh = mesh;
	}
	protected VisualMeshEntity(Material material, float x, float y, float z) {
		super(material, x, y, z);
	}
	public Mesh getMesh() {
		return mesh;
	}

}
