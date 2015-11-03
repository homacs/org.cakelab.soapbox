package org.cakelab.soapbox.testscene.blenderRaw;

import org.cakelab.soapbox.DynamicObject;
import org.cakelab.soapbox.model.TriangleMesh;

public class BlenderCube extends BlenderObject implements DynamicObject {

	private float xOff;
	private float yOff;
	private float zOff;

	public BlenderCube(TriangleMesh cubeMesh) {
		super(cubeMesh);
	}

	public BlenderCube(float x, float y, float z, TriangleMesh cubeMesh) {
		super(cubeMesh);
		xOff = x;
		yOff = y;
		zOff = z;
	}

	@Override
	public void update(double currentTime) {

		float f = (float) currentTime * 0.3f;
		// TODO: disabled until fixed
//		setYaw((float) currentTime * 45.0f);
//		setPitch((float) currentTime * 21.0f);
		
		setX(xOff + (float) Math.sin(2.1f * f) * 1.0f);
		setY(yOff + (float) Math.cos(1.7f * f) * 1.0f);
		setZ(zOff + (float) Math.sin(1.3f * f) * (float) Math.cos(1.5f * f) * 1.0f);
	}
}
