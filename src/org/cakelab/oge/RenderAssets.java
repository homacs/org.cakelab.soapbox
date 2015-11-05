package org.cakelab.oge;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import org.cakelab.oge.opengl.VertexArrayObject;
import org.cakelab.soapbox.model.TriangleMesh;

public class RenderAssets {

	public VertexArrayObject vao;
	private int numVertices;
	private int drawingMethod;

	public RenderAssets(TriangleMesh mesh) {
		setDrawingMethod(GL_TRIANGLES);
		setNumVertices(mesh.getNumVertices());
		vao = new VertexArrayObject(mesh, 0, GL_STATIC_DRAW);
	}

	public void bind() {
		vao.bind();
	}

	public int getDrawingMethod() {
		return drawingMethod;
	}

	public void setDrawingMethod(int drawingMethod) {
		this.drawingMethod = drawingMethod;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public void setNumVertices(int numVertices) {
		this.numVertices = numVertices;
	}

	public void delete() {
		vao.delete();
	}

	
	
}
