package org.cakelab.soapbox.model;

import java.nio.FloatBuffer;

import org.cakelab.oge.utils.BufferUtilsHelper;

public class TriangleMesh extends Mesh {

	private float[] data;
	private int vectorSize;

	/**
	 * 
	 * @param frontFace
	 * @param vectorSize num components of a vector of a vertex (i.e. 3 or 4)
	 * @param triangles
	 */
	public TriangleMesh(FrontFace frontFace, int vectorSize, float[] triangles) {
		assert(vectorSize == 3 || vectorSize == 4);
		this.vectorSize = vectorSize;
		switch(frontFace) {
		case CounterClockwise:
			data = triangles;
			break;
		case Clockwise:
			data = convert(triangles);
			break;
		}
	}

	private float[] convert(float[] triangles) {
		float[] data = new float[triangles.length];
		int num_vertices = triangles.length / vectorSize;
		for (int t = 0; t < num_vertices; t += vectorSize) {
			copyVertexF(data, (t*vectorSize)+(0*vectorSize), triangles, (t*vectorSize)+(2*vectorSize));
			copyVertexF(data, (t*vectorSize)+(1*vectorSize), triangles, (t*vectorSize)+(1*vectorSize));
			copyVertexF(data, (t*vectorSize)+(2*vectorSize), triangles, (t*vectorSize)+(0*vectorSize));
		}
		return data;
	}

	public FloatBuffer getFloatBuffer() {
		return BufferUtilsHelper.createFloatBuffer(data);
	}

	public void dump() {
		for (int i = 0; i < data.length; i+=vectorSize) {
			dumpVertex(data, i);
			if (0 == i%vectorSize) {
				System.out.println();
			}
		}
	}

	private void dumpVertex(float[] data, int i) {
		System.out.printf("%f,%f,%f", data[i++], data[i++], data[i++]);
		if (vectorSize == 4) System.out.printf(",%f", data[i++]);

	}

	public int getNumElements() {
		return data.length;
	}
}
