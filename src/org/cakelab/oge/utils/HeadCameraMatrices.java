package org.cakelab.oge.utils;

import org.cakelab.oge.HeadCamera;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HeadCameraMatrices extends CameraMatrices {

	private HeadCamera camera;


	
	private Quaternionf qRotate = new Quaternionf();
	private Vector3f eye = new Vector3f(0, 0, 1);
	private Vector3f up = new Vector3f(0, 1, 0);
	

	public HeadCameraMatrices(HeadCamera pose) {
		super(pose);
		this.camera = pose;
	}


	public Matrix4f getOrientationTransform() {
		applyModifications();
		return orientationTransform;
	}
	
	public Matrix4f getViewTransform() {
		applyModifications();
		return viewTransform;
	}


	@Override
	public Quaternionf getRotationQuaternion() {
		if (pose.isPoseModified(lastUpdate)) {
		
			// roll axis (Z)
			eye.set(0, 0, 1);
			// yaw axis (Y)
			up.set(0, 1, 0);
			
			qRotate.identity().rotateZ(camera.getRoll());
			qRotate.transform(eye);
			qRotate.transform(up);
	
			qRotate.identity().rotateX(-camera.getPitch());
			qRotate.transform(eye);
			qRotate.transform(up);
			
			qRotate.identity().rotateY(camera.getYaw());
			qRotate.transform(eye);
			qRotate.transform(up);
			
			qRotate.identity().lookRotate(eye, up).invert();
		}
		
		return qRotate;
	}

}
