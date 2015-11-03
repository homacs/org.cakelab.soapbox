package org.cakelab.soapbox.testscene.SBM_KTX;

import org.cakelab.oge.VisualObject;
import org.cakelab.soapbox.DynamicObject;

public class Torus extends VisualObject implements DynamicObject {



	public int tex_index;
	private double nextTime;


	public Torus() {
		tex_index = 0;
	}
	
	
	public Torus(float x, float y, float z) {
		this();
		setX(x);
		setY(y);
		setZ(z);
	}


	@Override
	public void update(double currentTime) {
		if (currentTime > nextTime) {
			tex_index = tex_index == 0 ? 1 : 0;
			nextTime = currentTime + 2;
		}
		
		setRotation(0f, (float)currentTime * 19.3f, (float)currentTime * 21.1f);
	}

}
