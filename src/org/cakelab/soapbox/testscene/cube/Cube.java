package org.cakelab.soapbox.testscene.cube;

import org.cakelab.oge.scene.DynamicEntity;
import org.cakelab.oge.scene.VisualEntity;


public class Cube extends VisualEntity implements DynamicEntity {

	private int i;

	public Cube(int i) {
		super(null);
		this.i = i;
	}

	@Override
	public void update(double currentTime) {

		float f = (float) i + (float) currentTime * 0.3f;
		
		setRotation((float) currentTime * 21.0f, (float) currentTime * 45.0f, 0f);
		
		setX((float) Math.sin(2.1f * f) * 2.0f);
		setY((float) Math.cos(1.7f * f) * 2.0f);
		setZ((float) Math.sin(1.3f * f) * (float) Math.cos(1.5f * f) * 2.0f);
	}

//	public void update(double currentTime) {
//		roll += .2f;
//	}
}
