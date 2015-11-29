package org.cakelab.soapbox.model;

import org.lwjgl.opengl.GL11;


public class TriangleMesh extends Mesh {

	/**
	 * @param frontFace
	 * @param vectorSize num components of a vector (of course min. 3 for 3d)
	 * @param triangles The vertices of the triangles.
	 */
	public TriangleMesh(FrontFaceVertexOrder frontFace, int vectorSize, float[] triangles) {
		this(frontFace, vectorSize, triangles, triangles.length);
	}

	public TriangleMesh(FrontFaceVertexOrder frontFace, int vectorSize, float[] triangles, int arrayLen) {
		super(GL11.GL_TRIANGLES, frontFace, vectorSize, triangles, arrayLen);
	}

}
