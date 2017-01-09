package org.cakelab.oge.utils;

import org.cakelab.soapbox.HeadCamera;
import org.joml.Matrix4f;

public class HeadCameraMatrices extends CameraMatrices {


	public HeadCameraMatrices(HeadCamera pose) {
		super(pose);
	}


	public Matrix4f getOrientationTransform() {
		update();
		return orientationTransform;
	}
	
	public Matrix4f getViewTransform() {
		update();
		return viewTransform;
	}

}
