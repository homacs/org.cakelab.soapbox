package org.cakelab.oge.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
/**
 * This was an attempt to hide the whole uniform block handling code
 * behind an interface. This class still needs a whole lot more work!
 */
public class GLUniformBlock {
	
	private String name;
	private String[] memberNames;
	private int num_members = -1;
	private IntBuffer uniformIndices;
	/**
	 * The offset of the uniform within the block.
	 */
	private IntBuffer uniformOffsets;
	/** 
	 * The number of bytes between consecutive elements of an array. 
	 * If the uniform is not an array, this will be zero.
	 */
	private IntBuffer arrayStrides;
	/**
	 * The number of bytes between the first element of each column of a 
	 * column-major matrix or row of a row-major matrix. If the uniform is
	 * not a matrix, this will be zero.
	 */
	private IntBuffer matrixStrides;
	/**
	 * The data type of the uniform as an enum.
	 */
	private IntBuffer uniformTypes;
	/**
	 * The size of arrays, in units of whatever GL_UNIFORM_TYPE gives
	 * you. If the uniform is not an array, this will always be one.
	 */
	private IntBuffer uniformSizes;
	private ByteBuffer buffer;
	
	private int uniformBufferObject;
	private String[] fqnNames;
	/**
	 * The index of the block that the uniforms are members of.
	 */
	private int uniformBlockIndex;
	private int uniformBlockBindingIndex;
	/**
	 * Each element of the output array will either be one if the uniform is a
	 * row-major matrix, or zero if it is a column-major matrix or not a matrix
	 * at all.
	 */
	private IntBuffer matrixRowMajor;

	public GLUniformBlock(int program, int bindingIndex, String uniformBlockName, String[] uniformMemberNames) {
		name = uniformBlockName;
		memberNames = uniformMemberNames;
		
		num_members = memberNames.length;
		fqnNames = getFullQualifiedNames();
		
		uniformIndices = BufferUtils.createIntBuffer(num_members);
		uniformTypes = BufferUtils.createIntBuffer(num_members);
		uniformSizes = BufferUtils.createIntBuffer(num_members);
		uniformOffsets = BufferUtils.createIntBuffer(num_members);
		arrayStrides = BufferUtils.createIntBuffer(num_members);
		matrixStrides = BufferUtils.createIntBuffer(num_members);
		matrixRowMajor = BufferUtils.createIntBuffer(num_members);

		glGetUniformIndices(program, fqnNames, uniformIndices);
		glGetActiveUniformsiv(program, uniformIndices, GL_UNIFORM_TYPE, uniformTypes);
		glGetActiveUniformsiv(program, uniformIndices, GL_UNIFORM_SIZE, uniformSizes);
		glGetActiveUniformsiv(program, uniformIndices, GL_UNIFORM_OFFSET, uniformOffsets);
		glGetActiveUniformsiv(program, uniformIndices, GL_UNIFORM_ARRAY_STRIDE, arrayStrides);
		glGetActiveUniformsiv(program, uniformIndices, GL_UNIFORM_MATRIX_STRIDE, matrixStrides);
		glGetActiveUniformsiv(program, uniformIndices, GL_UNIFORM_IS_ROW_MAJOR, matrixRowMajor);
		
		// TODO: determine the size of the buffer based on its layout and alignment information (not done in the book)
		int buffer_size = 4096;
		buffer = BufferUtils.createByteBuffer(buffer_size); // 4k (max 64k)

		// create a buffer object
		uniformBufferObject = glGenBuffers();
		glBindBuffer(GL_UNIFORM_BUFFER, uniformBufferObject);
		// TODO: buffer object usage type can vary!
		glBufferData(GL_UNIFORM_BUFFER, buffer_size, GL_DYNAMIC_DRAW);
		
		// first retrieve the uniform block index
		uniformBlockIndex = glGetUniformBlockIndex(program, name);

		// TODO: provide a version where the index is specified in glsl code
		// then assign a binding point to our uniform block
		uniformBlockBindingIndex = bindingIndex;
		glUniformBlockBinding(program, uniformBlockIndex, uniformBlockBindingIndex);
		
		// bind buffer to the binding point to make the data in the buffer
		// appear in the uniform block
		glBindBufferBase(GL_UNIFORM_BUFFER, uniformBlockBindingIndex, uniformBufferObject);

	}

	
	public void set(String member, float value) {
		set(index(member), value);
	}
	
	public void set(int memberIndex, float value) {
		assert(this.uniformTypes.get(memberIndex) == GL_FLOAT);
		int offset = uniformOffsets.get(memberIndex);
		buffer.putFloat(offset , value);
	}


	public void setVector(int memberIndex, float[] vec) {
		int type = uniformTypes.get(memberIndex);
		assert(vec.length==4 ? type == GL_FLOAT_VEC4 : vec.length==4 ? type == GL_FLOAT_VEC3 :vec.length==2 ? type == GL_FLOAT_VEC2 : false);
		buffer.position(uniformOffsets.get(memberIndex));
		for (float v : vec) {
			buffer.putFloat(v);
		}
	}

	public void setArray(int memberIndex, float[] arr) {
		int type = uniformTypes.get(memberIndex);
		assert(arr.length==4 ? type == GL_FLOAT_VEC4 : arr.length==4 ? type == GL_FLOAT_VEC3 :arr.length==2 ? type == GL_FLOAT_VEC2 : false);
		buffer.position(uniformOffsets.get(memberIndex));
		int offset = uniformOffsets.get(memberIndex);
		int stride = arrayStrides.get(memberIndex);
		for (float v : arr) {
			buffer.putFloat(offset, v);
			offset += stride;
		}
	}

	public void setMatrix4x4(int index, float[] matrix) {
		int startOffset = uniformOffsets.get(index);
		int strides = matrixStrides.get(index);
		boolean rowMajor = matrixRowMajor.get(index) == 1;
		for (int i = 0; i < 4; i++)
		{
			int offset = startOffset + strides * i;
			for (int j = 0; j < 4; j++)
			{
				// TODO: test row major
				// layout loop
				int element = rowMajor ? j * 4 + i : i * 4 + j;
				buffer.putFloat(offset, matrix[element]);
				// calc offset for next float
				offset += BufferUtilsHelper.SIZEOF_FLOAT;
			}
		}

	}
	
	public int index(String member) {
		for (int i = 0; i < num_members; i++) {
			if (member.equals(this.memberNames[i])) {
				return i;
			}
		}
		return -1;
	}


	/**
	 * Submits the complete data structure to the buffer object
	 */
	public void submit() {
		// TODO: do smart submit considering whether a member was modified
		buffer.rewind();
		glBufferSubData(GL_UNIFORM_BUFFER, 0, buffer);
	}

	public void delete() {
		glDeleteBuffers(uniformBufferObject);
		name = null;
		memberNames = null;
		fqnNames = null;
		
		uniformIndices = null;
		uniformTypes = null;
		uniformSizes = null;
		uniformOffsets = null;
		arrayStrides = null;
		matrixStrides = null;
		matrixRowMajor = null;

	}

	private String[] getFullQualifiedNames() {
		String[] names = new String[num_members];
		for(int i = 0; i < num_members; i++) {
			names[i] = this.name + '.' + this.memberNames[i]; 
		}
		return names;
	}

	public static void main(String[] args) {
		int program = 0;
		GLUniformBlock b = new GLUniformBlock(program, 0, "TransformBlock", new String[]{
			"scale",
			"translation",
			"rotation",
			"projection_matrix"
		});
		
		b.set(0, 1.5f);
		b.setVector(1, new float[]{-0.3f, 0.3f, 0.0f});
		b.setArray(2, new float[]{ 30.0f, 40.0f, 60.0f });
		b.setMatrix4x4(3, new float[]
			{
				1.0f, 2.0f, 3.0f, 4.0f,
				9.0f, 8.0f, 7.0f, 6.0f,
				2.0f, 4.0f, 6.0f, 8.0f,
				1.0f, 3.0f, 5.0f, 7.0f
			});
		b.submit();
		
		b.delete();
		
	}



}
