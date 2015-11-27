package org.cakelab.soapbox.model;

import org.lwjgl.opengl.GL11;


public class QuadMesh extends Mesh {


	/**
	 * @param frontFace
	 * @param vectorSize num components of a vector of a vertex (min. 3 for 3d)
	 * @param triangles The vertices of the triangles.
	 */
	public QuadMesh(FrontFaceVertexOrder frontFace, int vectorSize, float[] quads) {
		super(GL11.GL_QUADS, frontFace, vectorSize, quads);
	}

	public TriangleMesh toTriangleMesh() {
		int numpolies = getNumVertices()/4;
		int numtriangles = numpolies*2;
		
		float[] triangles = new float[numtriangles*3*vectorSize];
		int i = 0;
		for (int p = 0; p < numpolies; p++) {
			int firstVector = p * 4;
			
			// first triangle
			copyVector(firstVector +0, triangles, i);
			i += vectorSize;
			copyVector(firstVector +1, triangles, i);
			i += vectorSize;
			copyVector(firstVector +2, triangles, i);
			i += vectorSize;
			
			// second triangle
			copyVector(firstVector +2, triangles, i);
			i += vectorSize;
			copyVector(firstVector +3, triangles, i);
			i += vectorSize;
			copyVector(firstVector +0, triangles, i);
			i += vectorSize;
		}
		
		return new TriangleMesh(FrontFaceVertexOrder.CounterClockwise, vectorSize, triangles);
	}

}
