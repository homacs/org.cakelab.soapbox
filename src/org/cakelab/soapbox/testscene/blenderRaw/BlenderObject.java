package org.cakelab.soapbox.testscene.blenderRaw;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import org.cakelab.oge.opengl.VertexArrayObject;
import org.cakelab.oge.scene.Material;
import org.cakelab.oge.scene.VisualMeshObject;
import org.cakelab.oge.utils.OGEMeshRenderData;
import org.cakelab.soapbox.model.Mesh;

public abstract class BlenderObject extends VisualMeshObject {


	public BlenderObject(Mesh mesh) {
		this(mesh, 0,0,0);
	}

	public BlenderObject(Mesh mesh, float x, float y, float z) {
		super(mesh, new Material(), x,y,z);
		OGEMeshRenderData renderData = new OGEMeshRenderData(new VertexArrayObject(mesh, 0, GL_STATIC_DRAW), mesh.getGlDrawingMethod(), mesh.getNumVertices());
		super.setRenderData(renderData);
	}

}
