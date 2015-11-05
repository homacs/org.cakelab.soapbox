package org.cakelab.soapbox.testscene.blenderRaw;

import org.cakelab.oge.RenderAssets;
import org.cakelab.soapbox.DynamicObject;

public class BlenderCube extends BlenderObject implements DynamicObject {

	private float xOff;
	private float yOff;
	private float zOff;

	public BlenderCube(RenderAssets assets) {
		super(assets);
	}

	public BlenderCube(float x, float y, float z, RenderAssets assets) {
		super(assets);
		xOff = x;
		yOff = y;
		zOff = z;
	}

	@Override
	public void update(double currentTime) {

		float f = (float) currentTime * 0.3f;
		setRotation((float) currentTime * 21.0f, (float) currentTime * 45.0f, 0);
		setX(xOff + (float) Math.sin(2.1f * f) * 1.0f);
		setY(yOff + (float) Math.cos(1.7f * f) * 1.0f);
		setZ(zOff + (float) Math.sin(1.3f * f) * (float) Math.cos(1.5f * f) * 1.0f);
	}

}
