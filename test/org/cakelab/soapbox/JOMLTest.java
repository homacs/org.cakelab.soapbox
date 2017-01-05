package org.cakelab.soapbox;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class JOMLTest {

	public static void main(String[] args) {
		Vector3f v = new Vector3f(1,0,0);
		Quaternionf r = new Quaternionf()
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
				.rotateAxis(45, 1, 1, 1)
		;
//		r.rotateAxis(90, 5, 0, 0);
		System.out.println(v);
		r.transform(v);
		System.out.println(v);
		
	}

}
