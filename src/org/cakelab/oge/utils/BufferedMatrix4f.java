package org.cakelab.oge.utils;

import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;


public class BufferedMatrix4f extends Matrix4f {

	private FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);

	public BufferedMatrix4f() {
		super();
	}

	public BufferedMatrix4f(float m00, float m01, float m02, float m03,
			float m10, float m11, float m12, float m13, float m20, float m21,
			float m22, float m23, float m30, float m31, float m32, float m33) {
		super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31,
				m32, m33);
	}

	public BufferedMatrix4f(FloatBuffer buffer) {
		super(buffer);
	}

	public BufferedMatrix4f(Matrix3f mat) {
		super(mat);
	}

	public BufferedMatrix4f(Matrix4f mat) {
		super(mat);
	}

	public FloatBuffer getFloatBuffer() {
		get(buffer);
		return buffer;
	}
	
}
