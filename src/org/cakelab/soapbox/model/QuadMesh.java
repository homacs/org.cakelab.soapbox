package org.cakelab.soapbox.model;

import org.lwjgl.opengl.GL11;


public class QuadMesh extends Mesh {


	/**
	 * @param frontFace
	 * @param vectorSize num components of a vector of a vertex (min. 3 for 3d)
	 * @param triangles The vertices of the triangles.
	 */
	public QuadMesh(FrontFaceVertexOrder frontFace, int vectorSize, float[] coords, int uvOffset, int normalsOffset) {
		super(GL11.GL_QUADS, frontFace, vectorSize, coords, uvOffset, normalsOffset, coords.length);
	}

	public QuadMesh(FrontFaceVertexOrder frontFace, int vectorSize, float[] coords) {
		super(GL11.GL_QUADS, frontFace, vectorSize, coords, 0, 0, coords.length);
	}

	public TriangleMesh toTriangleMesh() {
		int numpolies = getNumVertices()/4;
		int numtriangles = numpolies*2;
		
		float[] triangles = new float[numtriangles*3*vectorSize];
		int i = 0;
		for (int p = 0; p < numpolies; p++) {
			int firstVector = p * 4;
			i = convQuadToTriangles(data, firstVector*vectorSize, triangles, i, vectorSize);
		}
		return new TriangleMesh(FrontFaceVertexOrder.STANDARD, vectorSize, triangles, uvOffset, normalsOffset);
	}
	
	/**
	 * Splits one quad into two triangles.
	 * (vertex order will be unchanged)
	 * @param source
	 * @param srcPos
	 * @param target
	 * @param targetPos
	 * @param vectorSize
	 * @return srcPos + 6*vertexSize
	 */
	public static int convQuadToTriangles(float[] source, int srcPos, float[] target, int targetPos, int vectorSize) {
		// first triangle
		System.arraycopy(source, srcPos +0*vectorSize, target, targetPos, vectorSize);
		targetPos += vectorSize;
		System.arraycopy(source, srcPos +1*vectorSize, target, targetPos, vectorSize);
		targetPos += vectorSize;
		System.arraycopy(source, srcPos +2*vectorSize, target, targetPos, vectorSize);
		targetPos += vectorSize;
		
		// second triangle
		System.arraycopy(source, srcPos +2*vectorSize, target, targetPos, vectorSize);
		targetPos += vectorSize;
		System.arraycopy(source, srcPos +3*vectorSize, target, targetPos, vectorSize);
		targetPos += vectorSize;
		System.arraycopy(source, srcPos +0*vectorSize, target, targetPos, vectorSize);
		targetPos += vectorSize;
		return targetPos;
	}
}
