package org.cakelab.oge.utils;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.cakelab.oge.opengl.VertexArrayObject;
import org.cakelab.oge.scene.ModuleData;

public class OGEMeshRenderData implements ModuleData {

	private VertexArrayObject vao;
	private int drawingMethod;
	private int numVertices;

	public OGEMeshRenderData(VertexArrayObject vertexArrayObject, int drawingMethod, int numVertices) {
		this.vao = vertexArrayObject;
		this.drawingMethod = drawingMethod;
		this.numVertices = numVertices;
	}

	@Override
	public void delete() {
		vao.delete();
	}

	public VertexArrayObject getVertexArrayObject() {
		return vao;
	}

	public int getDrawingMethod() {
		return drawingMethod;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public void draw() {
		bind();
		glDrawArrays(drawingMethod, 0, numVertices);
	}

	public void bind() {
		vao.bind();
	}


}
