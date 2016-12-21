package org.cakelab.oge.scene;

import java.util.ArrayList;


public class Scene {

	/**
	 * All visual objects.
	 */
	ArrayList<VisualObject> objects = new ArrayList<VisualObject>();
	/**
	 * Subset of objects which are dynamic objects.
	 */
	ArrayList<DynamicObject> dynamicObjects = new ArrayList<DynamicObject>();
	ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
	
	
	
	public void add(VisualObject vobj) {
		objects.add(vobj);
		if (vobj instanceof DynamicObject) {
			dynamicObjects.add((DynamicObject) vobj);
		}
	}
	
	public ArrayList<VisualObject> getVisualObjects() {
		return objects;
	}

	public ArrayList<DynamicObject> getDynamicObjects() {
		return dynamicObjects;
	}

	public void addLightSource(LightSource lamp) {
		lightSources.add(lamp);
	}

	public ArrayList<LightSource> getLightSources() {
		return lightSources;
	}
	

}
