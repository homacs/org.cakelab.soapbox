package org.cakelab.oge.math;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.scene.Pose;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class CameraMatrices {
	protected double lastUpdate = GlobalClock.TIME_INVALID;
	
	protected Matrix4f viewTransform = new Matrix4f();
	protected Quaternionf rotation = new Quaternionf();

	private Quaternionf tempQuat = new Quaternionf();
	private Vector3f tempVect = new Vector3f();
	
	protected Pose pose;

	public CameraMatrices(Pose pose) {
		this.pose = pose;
	}
	
	protected CameraMatrices() {
	}
	
	public Matrix4f getViewTransform() {
		update();
		return viewTransform;
	}

	public void update() {
		if (pose.isWorldPoseModified(lastUpdate)) {
			applyModifications();
			lastUpdate = GlobalClock.getFrameTime();
		}			
	}
	
	/**
	 * Method called each time the associated pose or its ancestors where modified.
	 */
	protected void applyModifications() {
		//
		// To get a view transformation we 
		// 1. move the entities away from us by the distance of our pos from center
		// 2. and then rotate them (the world) against our rotation around us.
		// This is the same as the inverse of (1) rotate by our rotation, then (2) move to our location.
		// 
		// OpenGLs default camera orientation is forward(0,0,-1) up(0,1,0)
		// The rotation of our entity orientation is relative to forward(0,0,1) up(0,1,0)
		// Thus, to get a rotation for the negative Z axis we first apply a 180 degree rotation around Y.
		//
		
		Pose reference = pose.getReferenceSystem();
		rotation.identity();
		
		if (reference == null) {
			
			rotation.mul(pose.getOrientation().getRotation(tempQuat))
				.rotateY((float) Math.PI);

			viewTransform.identity()
				.translate(pose.getPosition())
				.rotate(rotation)
				.invert()
			;
			
			rotation.invert();
		
		} else {
			// with reference system:
			// 1. rotate locally
			// 2. move to position from 0,0
			// 3. rotate according to reference system
			// 4. translate according to reference system
			// 5. repeat at 3 for reference systems of reference systems
			// To do this in this exact order, we just need to apply the 
			// reverse rotations and translations
			Vector3f inversePosition = tempVect;

			// first apply the 180 degrees rotation around as explained above
			viewTransform.rotationY((float)Math.PI);
			rotation.rotationY((float)Math.PI);
			// now apply inverse rotations and translations in the order of reference
			// systems starting with the camera itself.
			for (Pose p = pose; p != null; p = p.getReferenceSystem()) {
				inversePosition.set(p.getPosition()).negate();
				rotation.mul(p.getOrientation().getReverseRotation());
				viewTransform
					.rotate(p.getOrientation().getReverseRotation())
					.translate(inversePosition)
				;
			}
		}
	}

	public Quaternionfc getInverseRotation() {
		update();
		return rotation;
	}

}
