package org.cakelab.soapbox.model;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.cakelab.oge.module.ModuleData;
import org.cakelab.oge.utils.BufferUtilsHelper;
import org.lwjgl.opengl.GL11;

public class Mesh {
	
	protected float[] data;
	protected int vectorSize;
	protected int uvOffset;
	protected int normalsOffset;
	protected int verticesPerPolygon;
	protected int glDrawingMethod;
	private ModuleData moduleData;

	
	/**
	 * Defines which side of a polygon is it's front.
	 * 
	 * @author homac
	 *
	 */
	public static enum FrontFaceVertexOrder {
		CounterClockwise,
		Clockwise;
		
		public static final FrontFaceVertexOrder STANDARD = CounterClockwise;
	}

	public Mesh(int glDrawingMethod, FrontFaceVertexOrder frontFace, int vectorSize,
			float[] dataIn, int uvOffset, int normalsOffset, int arrayLen) {
		// TODO array length as parameter??
		this.setGlDrawingMethod(glDrawingMethod);
		this.verticesPerPolygon = calcVerticesPerPolygone(glDrawingMethod);
		assert(vectorSize >= 3);
		this.vectorSize = vectorSize;
		this.uvOffset = uvOffset <= 0 ? -1 : uvOffset;
		this.normalsOffset = normalsOffset <= 0 ? -1 : normalsOffset;
		
		if (frontFace == FrontFaceVertexOrder.STANDARD) {
			data = Arrays.copyOf(dataIn, arrayLen);
		} else {
			data = swapVertexOrder(Arrays.copyOf(dataIn, arrayLen));
		}
	}

	/** whether the mesh contains normal vectors */
	public boolean hasNormals() {
		return normalsOffset > 0;
	}

	/** Offset of normal vector coordinates in a vertex vector. A value less or 
	 * equal to zero means the mesh contains no normal vectors. */
	public int getNormalsOffset() {
		return normalsOffset;
	}
	
	/** whether the mesh contains UV coordinates */
	public boolean hasUVCoordinates() {
		return uvOffset > 0;
	}

	/** Offset of UV coordinates in a vertex vector. A value less or 
	 * equal to zero means the mesh contains no uv coordinates. */
	public int getUVOffset() {
		return uvOffset;
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
		int count = 0;
		int polygons = 0;
		for (int i = 0; i < data.length; i+=vectorSize) {
			if (count % this.verticesPerPolygon == 0) System.out.printf("### Polygon %02d ###\n", polygons++);
			System.out.printf("\t%02d: ", count++);
			dumpVertex(data, i);
			System.out.println();
		}
	}

	private void dumpVertex(float[] data, int offset) {
		String format = "%.2f";
		System.out.printf(format, data[offset++]);
		for (; offset % vectorSize != 0; offset++) {
			System.out.printf(", " + format, data[offset]);
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

	public void setRenderData(ModuleData renderData) {
		this.moduleData = renderData;
	}

	public ModuleData getRenderData() {
		return moduleData;
	}


}
