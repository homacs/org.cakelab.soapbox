package org.cakelab.soapbox.model;


public class TriangleMesh extends Mesh {


	/**
	 * 
	 * @param frontFace
	 * @param vectorSize num components of a vector of a vertex (min. 3 for 3d)
	 * @param triangles
	 */
	public TriangleMesh(FrontFaceVertexOrder frontFace, int vectorSize, float[] triangles) {
		super(frontFace, 3, vectorSize, triangles);
	}

}
