package org.cakelab.oge;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import org.cakelab.oge.opengl.VertexArrayObject;
import org.cakelab.soapbox.model.Mesh;

public class RenderAssets {
	// TODO reconsider render assets and render data
	private Mesh mesh;

	public RenderAssets(Mesh mesh) {
		this(mesh, true);
	}

	public RenderAssets(Mesh mesh, boolean doGlSetup) {
		this.mesh = mesh;
		if (doGlSetup) {
			OGEMeshRenderData renderData = new OGEMeshRenderData(new VertexArrayObject(mesh, 0, GL_STATIC_DRAW));
			mesh.setRenderData(renderData);
		}
	}
	
	
	public void bind() {
		mesh.getRenderData().bind();
	}

	public int getDrawingMethod() {
		return mesh.getGlDrawingMethod();
	}

	public int getNumVertices() {
		return mesh.getNumVertices();
	}

	public void delete() {
		mesh.getRenderData().delete();
	}

	public Mesh getMesh() {
		return mesh;
	}

	
	
}
