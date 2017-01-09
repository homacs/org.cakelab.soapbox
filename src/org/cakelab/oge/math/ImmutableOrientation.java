package org.cakelab.oge.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;


public class ImmutableOrientation extends OrientationOld implements Orientation {

	public ImmutableOrientation(Vector3fc forward, Vector3fc up) {
		super(false);
		this.forward = new Vector3f(forward);
		this.up = new Vector3f(up);
	}

	@Override
	protected void setForward(Vector3fc forward) {
		throwImmutableException();
	}

	private void throwImmutableException() {
		throw new Error("immutable orientation");
	}

	@Override
	protected void setUp(Vector3fc up) {
		throwImmutableException();
	}

	@Override
	public void set(Vector3fc forward, Vector3fc up) {
		throwImmutableException();
	}

	@Override
	public void set(Orientation that) {
		throwImmutableException();
	}

	@Override
	public void apply(Quaternionf rotation) {
		throwImmutableException();
	}

	@Override
	public void addLocalEulerZXY(float xAngle, float yAngle, float zAngle) {
		throwImmutableException();
	}

	@Override
	public void addEulerZXY(float xAngle, float yAngle, float zAngle) {
		throwImmutableException();
	}

	@Override
	public void setEulerZXY(float pitch, float yaw, float roll) {
		throwImmutableException();
	}

	@Override
	public Vector3f getForward(Vector3f v) {
		return super.getForward(v);
	}

	@Override
	public Vector3f getUp(Vector3f v) {
		return super.getUp(v);
	}
	
}
