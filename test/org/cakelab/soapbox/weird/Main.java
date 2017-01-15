package org.cakelab.soapbox.weird;

import org.cakelab.soapbox.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Main extends Player {

	public static void main(String[] args) {
		new Main().run();
	}
	
	void run() {
		long something = 0;
		Vector3f lastPos = new Vector3f().set(getPosition());
		do {
			moveAlong(0,0,1);
			assert !lastPos.equals(getPosition());
			something++;
		} while (something != 0);
		
		System.out.println(something + getPosition().toString());

	}

	

	public void moveAlong(float x, float y, float z) {
//		Vector3f direction = tmpVect.set(x, y, z);
		Vector3f direction = new Vector3f().set(x,y,z);

		// Quaternionf orientation = getOrientation().getRotation(tmpQuat);
		Quaternionf orientation = getOrientation().getRotation(new Quaternionf());
		orientation.transform(direction);
		Vector3f pos = direction.add(getPosition());
		setPosition(pos);
	}
}
