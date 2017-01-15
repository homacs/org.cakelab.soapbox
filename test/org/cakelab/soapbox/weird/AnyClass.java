package org.cakelab.soapbox.weird;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class AnyClass {
	private Quaternionf q;
	private Vector3f v = new Vector3f(0,0,0);
	public AnyClass() {
		q = new Quaternionf().rotate((float) Math.PI, 0, 0).conjugate();
	}
	
	public Vector3fc getPosition() {
		return v;
	}
	
	public Quaternionf getRotation(Quaternionf result) {
		return result.set(q).conjugate();
	}
	
	public void setPosition(Vector3fc v) {
		this.v.set(v);
	}

}
