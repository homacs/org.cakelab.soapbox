package org.cakelab.soapbox;

import org.cakelab.oge.math.Orientation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TestQuaternion extends RotTestBase {
	public static void main (String[] args) {
		testAxisRotation(new Vector3f(0,-1,0), new Vector3f(0,0,1));
		testAxisRotation(new Vector3f(0,-1,0), new Vector3f(-1,0,0));
		testAxisRotation(new Vector3f(0,0,-1), new Vector3f(-1,0,0));
	}

	private static void testAxisRotation(Vector3f forward, Vector3f up) {
		Quaternionf q = new Quaternionf();
		Vector3f v = new Vector3f();

		Vector3f zAxis = forward;
		Vector3f yAxis = up;
		Vector3f xAxis = new Vector3f(up).cross(forward);

		// Rotations are the inverted rotations we want 
		// to achieve for our forward and up vectors, because it
		// rotates the coordinate system around the vectors and not
		// the vectors inside the coordinate system.
		
		
		// rotate to match forward vector
		q.rotateTo(forward, Orientation.DEFAULT_FORWARD);

		// get the rotated up vector
		Vector3f rotatedUp = new Vector3f();
		q.positiveY(rotatedUp);
		
		// add rotation to match the up vector
		q.rotateTo(up, rotatedUp);
		
		
		assert equals(q.positiveX(v), xAxis)
			&& equals(q.positiveY(v), yAxis)
			&& equals(q.positiveZ(v), zAxis)
		;
	}

}
