package org.cakelab.soapbox;

import org.joml.Vector3f;

public class RotTestBase {
	static double floatAccuracy = 0.000001;

	protected static boolean equals(Vector3f v1, Vector3f v2) {
		return equals(v1.x,v2.x) && equals(v1.y, v2.y) && equals(v1.z, v2.z);
	}

	protected static boolean equals(float f1, float f2) {
		return round(f1) == round(f2);
	}

	protected static float round(float f) {
		long l = Math.round(f/floatAccuracy);
		return l;
	}

}
