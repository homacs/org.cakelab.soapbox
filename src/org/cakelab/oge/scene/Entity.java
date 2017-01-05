package org.cakelab.oge.scene;

import org.joml.Vector3f;

/**
 * Any visual or non-visual object in the scene.
 * 
 * @author homac
 *
 */
public class Entity extends Pose {
	/** 
	 * Scaling in x y and z direction.
	 */
	private Vector3f scale = new Vector3f(1,1,1);
	
	public Entity() {
		super();
	}
	
	public Entity(float x, float y, float z) {
		super(x,y,z);
	}

	public Entity(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x,y,z, pitch, yaw, roll);
	}
	
	public Entity(float x, float y, float z, Vector3f dirForward, Vector3f dirUp) {
		super(x,y,z, dirForward, dirUp);
	}


	
	public void set(Entity that) {
		super.set(that);
		this.scale.set(that.scale);
	}

	public void setScale(float x, float y, float z) {
		scale.set(x,y,z);
	}
	public Vector3f getScale() {
		return scale;
	}

	
}
