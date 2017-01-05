package org.cakelab.oge.scene;

import java.util.ArrayList;


public class Scene {

	/**
	 * All visual objects.
	 */
	ArrayList<VisualEntity> objects = new ArrayList<VisualEntity>();
	/**
	 * Subset of objects which are dynamic objects.
	 */
	ArrayList<DynamicEntity> dynamicObjects = new ArrayList<DynamicEntity>();
	ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
	
	
	
	public void add(VisualEntity vobj) {
		objects.add(vobj);
		if (vobj instanceof DynamicEntity) {
			dynamicObjects.add((DynamicEntity) vobj);
		}
	}
	
	public ArrayList<VisualEntity> getVisualObjects() {
		return objects;
	}

	public ArrayList<DynamicEntity> getDynamicObjects() {
		return dynamicObjects;
	}

	public void addLightSource(LightSource lamp) {
		lightSources.add(lamp);
	}

	public ArrayList<LightSource> getLightSources() {
		return lightSources;
	}
	

}
