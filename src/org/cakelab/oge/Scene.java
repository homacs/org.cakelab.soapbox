package org.cakelab.oge;

import java.util.ArrayList;

import org.cakelab.soapbox.DynamicObject;


public class Scene {

	ArrayList<VisualObject> objects = new ArrayList<VisualObject>();
	ArrayList<DynamicObject> dynamic = new ArrayList<DynamicObject>();
	ArrayList<Lamp> lamps = new ArrayList<Lamp>();
	
	
	
	public void add(VisualObject vobj) {
		objects.add(vobj);
		if (vobj instanceof DynamicObject) {
			dynamic.add((DynamicObject) vobj);
		}
	}
	
	public ArrayList<VisualObject> getVisualObjects() {
		return objects;
	}

	public ArrayList<DynamicObject> getDynamicObjects() {
		return dynamic;
	}

	public void addLamp(Lamp lamp) {
		lamps.add(lamp);
	}

	public ArrayList<Lamp> getLamps() {
		return lamps;
	}
	

}
