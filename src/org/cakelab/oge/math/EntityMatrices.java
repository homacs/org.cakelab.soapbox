package org.cakelab.oge.math;

import org.cakelab.oge.app.GlobalClock;
import org.cakelab.oge.scene.Entity;
import org.cakelab.oge.scene.Pose;
import org.cakelab.oge.scene.VisualEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntityMatrices {

	private VisualEntity entity;
	private Matrix4f worldTransform = new Matrix4f();
	private Matrix4f modelTransform = null;
	private Quaternionf tempQuat = new Quaternionf();

	private double lastUpdate = GlobalClock.TIME_INVALID;
	private Vector3f tempVect = new Vector3f();
	
	public EntityMatrices(VisualEntity entity) {
		this.entity = entity;
	}

	public Matrix4f getWorldTransform() {
		applyModifications();
		return worldTransform;
	}

	private void applyModifications() {
		if (entity.isWorldPoseModified(lastUpdate)) {

			Pose reference = entity.getReferenceSystem(); 
			if (reference == null) {
				
				Quaternionf qRotate = entity.getOrientation().getRotation(tempQuat);
				worldTransform
					.identity()
					.translate(entity.getPosition())
					.rotate(qRotate)
					.scale(entity.getScale())
				;
			} else {
				// with reference system:
				// 1. rotate locally
				// 2. move to position from 0,0
				// 3. rotate according to reference system
				// 4. translate according to reference system
				// 5. repeat at 3 for reference systems of reference systems
				// To do this in this exact order, we just need to apply the 
				// reverse rotations and translations
				Vector3f inversePosition = tempVect ;

				worldTransform.identity();
				
				// now apply inverse rotations and translations in the order of reference
				// systems starting with the entity itself.
				for (Pose p = entity; p != null; p = p.getReferenceSystem()) {
					if (p instanceof Entity) {
						Vector3f inverseScale = tempVect;
						inverseScale.set(((Entity)p).getScale());
						inverseScale.x = 1f/inverseScale.x;
						inverseScale.y = 1f/inverseScale.y;
						inverseScale.z = 1f/inverseScale.z;
						worldTransform.scale(inverseScale);
					}
					
					inversePosition.set(p.getPosition()).negate();
					worldTransform
						.rotate(p.getOrientation().getReverseRotation())
						.translate(inversePosition)
						;
				}
				worldTransform.invert();

			}
			if (modelTransform != null) {
				worldTransform.mul(modelTransform);
			}
			lastUpdate = GlobalClock.getFrameTime();
		}
	}

	/**
	 * 
	 * This allows a transformation of a common model before application 
	 * of a model world transformation. This is for example useful if the
	 * local center of a model is different to that required in your application.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param yaw
	 * @param pitch
	 * @param roll
	 */
	public void setModelTransform(float x, float y, float z, float yaw, float pitch,
			float roll) {
		modelTransform = new Matrix4f().rotate(tempQuat.identity().rotate(pitch, yaw, roll)).translate(x,y,z);
	}
	


}
