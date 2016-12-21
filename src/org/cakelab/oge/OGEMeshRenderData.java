package org.cakelab.oge;

import org.cakelab.oge.opengl.VertexArrayObject;

public class OGEMeshRenderData implements MeshRenderData {

	private VertexArrayObject vao;

	public OGEMeshRenderData(VertexArrayObject vertexArrayObject) {
		this.vao = vertexArrayObject;
	}

	@Override
	public void bind() {
		vao.bind();
	}

	@Override
	public void delete() {
		vao.delete();
	}

	public VertexArrayObject getVertexArrayObject() {
		return vao;
	}

}
