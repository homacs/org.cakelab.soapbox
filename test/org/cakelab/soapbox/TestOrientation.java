package org.cakelab.soapbox;

import org.cakelab.oge.math.Orientation;
import org.cakelab.oge.math.OrientationImpl;
import org.cakelab.oge.math.OrientationOld;
import org.cakelab.oge.math.OrientationReverse;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TestOrientation extends RotTestBase {

	private static Vector3f v = new Vector3f();

	
	public static void main(String[] args) {
		
		testAssert();
		
		testGetLocalXAxis();
		
		testGetRotation();
		
		testAddLocalEulerZXY();
		
		System.out.println("tests finished.");
	}

	
	private static Orientation newOrientation() {
		return new OrientationReverse();
	}
	
	private static Orientation newOrientation(Vector3f forward, Vector3f up) {
		return new OrientationReverse(forward, up);
	}


	
	private static void testAddLocalEulerZXY() {
		// test x turn
		Orientation o = newOrientation();
		o.addLocalEulerZXY((float) Math.toRadians(90), 0, 0);
		assert (
			   equals(o.getLocalXAxis(v), new Vector3f(1,0,0))
			&& equals(o.getLocalYAxis(v), new Vector3f(0,0,1))
			&& equals(o.getLocalZAxis(v), new Vector3f(0,-1,0))
		);
		
		// test y turn
		o = newOrientation();
		o.addLocalEulerZXY(0, (float) Math.toRadians(90), 0);
		assert (
			   equals(o.getLocalXAxis(v), new Vector3f(0,0,-1))
			&& equals(o.getLocalYAxis(v), new Vector3f(0,1,0))
			&& equals(o.getLocalZAxis(v), new Vector3f(1,0,0))
		);
		
		// test z turn
		o = newOrientation();
		o.addLocalEulerZXY(0, 0, (float) Math.toRadians(90));
		assert (
			   equals(o.getLocalXAxis(v), new Vector3f(0,1,0))
			&& equals(o.getLocalYAxis(v), new Vector3f(-1,0,0))
			&& equals(o.getLocalZAxis(v), new Vector3f(0,0,1))
		);
		
		o = newOrientation();
		o.addLocalEulerZXY((float) Math.toRadians(90), 0, 0);
		o.addLocalEulerZXY(0, 0, (float) Math.toRadians(90));
		Vector3f xAxis = o.getLocalXAxis(new Vector3f());
		Vector3f yAxis = o.getLocalYAxis(new Vector3f());
		Vector3f zAxis = o.getLocalZAxis(new Vector3f());
		assert (
				   equals(o.getLocalXAxis(v), new Vector3f(0,0,1))
				&& equals(o.getLocalYAxis(v), new Vector3f(-1,0,0))
				&& equals(o.getLocalZAxis(v), new Vector3f(0,-1,0))
			);
		
	}

	private static void testAssert() {
		try {
			assert false;
		} catch (Throwable t) {
			// assertions are enabled, we are fine
			return;
		}
		
		throw new Error("Enable assertions first (-ea)");
	}

	private static void testGetRotation() {
		
		
		testGetRotation_noRotation();
		testGetRotation_xRotation();
		testGetRotation_yRotation();
		testGetRotation_zRotation();
		
		testGetRotation_xyzRotation(90,0,0);
		testGetRotation_xyzRotation(90,90,0);
		testGetRotation_xyzRotation(90,0,90);

		
	}

	private static void testGetLocalXAxis() {
		Orientation o = newOrientation();
		assert equals(o.getLocalXAxis(v),new Vector3f(1,0,0));
	}

	private static void testGetRotation_xRotation() {
		//
		// SINGLE ROTATION X --> forward(0,-1,0), up (0,0,1)
		//
		Orientation o = newOrientation(new Vector3f(0,-1,0), new Vector3f(0,0,1));
		Quaternionf rotation = o.getRotation(new Quaternionf());

		Vector3f x = new Vector3f(1,0,0);
		Vector3f y = new Vector3f(0,1,0);
		Vector3f z = new Vector3f(0,0,1);
		
		rotation.transform(x);
		rotation.transform(y);
		rotation.transform(z);
		
		assert equals(x, new Vector3f(1,0,0)) 
			&& equals(y, new Vector3f(0,0,1)) 
			&& equals(z, new Vector3f(0,-1,0)) 
		;
	}


	private static void testGetRotation_yRotation() {
		Orientation o = newOrientation(new Vector3f(1,0,0), new Vector3f(0,1,0));
		Quaternionf rotation = o.getRotation(new Quaternionf());

		Vector3f x = new Vector3f(1,0,0);
		Vector3f y = new Vector3f(0,1,0);
		Vector3f z = new Vector3f(0,0,1);
		
		rotation.transform(x);
		rotation.transform(y);
		rotation.transform(z);
		
		assert equals(x, new Vector3f(0,0,-1)) 
			&& equals(y, new Vector3f(0,1,0)) 
			&& equals(z, new Vector3f(1,0,0)) 
		;
	}

	private static void testGetRotation_zRotation() {
		Orientation o = newOrientation(new Vector3f(0,0,1), new Vector3f(-1,0,0));
		Quaternionf rotation = o.getRotation(new Quaternionf());

		Vector3f x = new Vector3f(1,0,0);
		Vector3f y = new Vector3f(0,1,0);
		Vector3f z = new Vector3f(0,0,1);
		
		rotation.transform(x);
		rotation.transform(y);
		rotation.transform(z);
		
		assert equals(x, new Vector3f(0,1,0)) 
			&& equals(y, new Vector3f(-1,0,0)) 
			&& equals(z, new Vector3f(0,0,1)) 
		;
	}

	private static void testGetRotation_xyzRotation(float xAngle, float yAngle, float zAngle) {
		Vector3f forward = Orientation.DEFAULT.getForward(new Vector3f());
		Vector3f up = Orientation.DEFAULT.getUp(new Vector3f());
		
		Quaternionf rotation = new Quaternionf().rotateXYZ(
				(float) Math.toRadians(xAngle), 
				(float) Math.toRadians(yAngle), 
				(float) Math.toRadians(zAngle));
		rotation.transform(forward);
		rotation.transform(up);
		Orientation o = newOrientation(forward, up);
		rotation = o.getRotation(new Quaternionf());

		Vector3f x = new Vector3f(1,0,0);
		Vector3f y = new Vector3f(0,1,0);
		Vector3f z = new Vector3f(0,0,1);
		
		rotation.transform(x);
		rotation.transform(y);
		rotation.transform(z);
		
		assert equals(x, o.getLocalXAxis(v)) 
			&& equals(y, o.getLocalYAxis(v)) 
			&& equals(z, o.getLocalZAxis(v))
		;
	}

	private static void testGetRotation_noRotation() {
		Orientation o = newOrientation();
		Quaternionf rotation = o.getRotation(new Quaternionf());
		
		Vector3f x = new Vector3f(1,0,0);
		Vector3f y = new Vector3f(0,1,0);
		Vector3f z = new Vector3f(0,0,1);

		rotation.transform(x);
		rotation.transform(y);
		rotation.transform(z);
		
		assert x.equals(new Vector3f(1,0,0)) 
			&& y.equals(new Vector3f(0,1,0)) 
			&& z.equals(new Vector3f(0,0,1)) 
		;
	}

}
