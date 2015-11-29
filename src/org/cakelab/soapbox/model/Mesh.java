package org.cakelab.soapbox.model;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.cakelab.oge.utils.BufferUtilsHelper;
import org.lwjgl.opengl.GL11;

public class Mesh {
	
	protected float[] data;
	protected int vectorSize;
	protected int verticesPerPolygon;
	protected int glDrawingMethod;

	
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
	
	public Mesh(int glDrawingMethod, FrontFaceVertexOrder frontFace, int vectorSize,
			float[] dataIn, int arrayLen) {
		this.setGlDrawingMethod(glDrawingMethod);
		this.verticesPerPolygon = calcVerticesPerPolygone(glDrawingMethod);
		assert(vectorSize >= 3);
		this.vectorSize = vectorSize;
		switch(frontFace) {
		case CounterClockwise:
			data = Arrays.copyOf(dataIn, arrayLen);
			break;
		case Clockwise:
			data = swapVertexOrder(Arrays.copyOf(dataIn, arrayLen));
			break;
		}
	}

	private int calcVerticesPerPolygone(int glDrawingMethod) {
		switch(glDrawingMethod) {
		case GL11.GL_TRIANGLES:
			return 3;
		case GL11.GL_QUADS:
			return 4;
		default:
			throw new Error("not implemented");
		}
	}

	public int getNumVertices() {
		return data.length/vectorSize;
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


	protected float[] swapVertexOrder(float[] dataIn) {
		float[] dataOut = new float[dataIn.length];
		int num_vertices = dataIn.length / vectorSize;
		for (int p = 0; p < num_vertices; p += verticesPerPolygon) {
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

	/** Size of a vertex in bytes */
	public int getStrideSize() {
		return getElemSize() * vectorSize;
	}

	/** 
	 * Size of the data type of elements in bytes. 
	 */
	public int getElemSize() {
		return BufferUtilsHelper.SIZEOF_FLOAT;
	}

	public int getElemType() {
		return GL11.GL_FLOAT;
	}

	public int getGlDrawingMethod() {
		return glDrawingMethod;
	}

	public void setGlDrawingMethod(int glDrawingMethod) {
		this.glDrawingMethod = glDrawingMethod;
	}

	
	/**
	 * Copies the v's vector from this mesh to the given position in target.
	 * @param source 
	 * @param v
	 * @param target
	 * @param targetPos
	 */
	public static void copyVector(float[] source, int v, float[] target, int targetPos, int vectorSize) {
		System.arraycopy(source, v*vectorSize, target, targetPos, vectorSize);
	}


}
