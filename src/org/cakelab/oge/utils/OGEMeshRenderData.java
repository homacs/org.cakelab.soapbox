package org.cakelab.oge.utils;

import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.cakelab.oge.opengl.MeshVertexArray;
import org.cakelab.oge.scene.ModuleData;

public class OGEMeshRenderData implements ModuleData {

	private MeshVertexArray vao;
	private int drawingMethod;
	private int numVertices;

	public OGEMeshRenderData(MeshVertexArray vertexArrayObject, int drawingMethod, int numVertices) {
		this.vao = vertexArrayObject;
		this.drawingMethod = drawingMethod;
		this.numVertices = numVertices;
	}

	@Override
	public void delete() {
		vao.delete(true);
	}

	public MeshVertexArray getVertexArrayObject() {
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
