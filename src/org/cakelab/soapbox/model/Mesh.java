package org.cakelab.soapbox.model;

import java.nio.FloatBuffer;

import org.cakelab.oge.utils.BufferUtilsHelper;

public class Mesh {
	
	private float[] data;
	private int vectorSize;
	private int verticesPerPolygon;

	
	/**
	 * Defines which side of a polygon is it's front.
	 * 
	 * @author homac
	 *
	 */
	public static enum FrontFaceVertexOrder {
		CounterClockwise,
		Clockwise
	}
	
	public Mesh(FrontFaceVertexOrder frontFace, int verticesPerPolygon, int vectorSize,
			float[] dataIn) {
		this.verticesPerPolygon = verticesPerPolygon;
		assert(vectorSize >= 3);
		this.vectorSize = vectorSize;
		switch(frontFace) {
		case CounterClockwise:
			data = dataIn;
			break;
		case Clockwise:
			data = swapVertices(dataIn);
			break;
		}
	}

	public int getNumElements() {
		return data.length;
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

	private void dumpVertex(float[] data, int offset) {
		System.out.printf("%f", data[offset++]);
		for (; offset < vectorSize; offset++) {
			System.out.printf(",%f", data[offset++]);
		}
	}


	protected float[] swapVertices(float[] dataIn) {
		float[] dataOut = new float[dataIn.length];
		int num_vertices = dataIn.length / vectorSize;
		for (int p = 0; p < num_vertices; p += vectorSize) {
			for (int vIn = 0, vOut = verticesPerPolygon-1; vIn < verticesPerPolygon; vIn++, vOut--) {
				System.arraycopy(
					dataIn, (p*vectorSize)+(vIn*vectorSize),
					dataOut, (p*vectorSize)+(vOut*vectorSize),
					vectorSize
				);
			}
		}
		return dataOut;
	}

}
