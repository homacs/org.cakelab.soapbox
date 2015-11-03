package org.cakelab.soapbox.testscene.coords;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;

public class CoordPlaneScene extends Scene {

	public CoordPlaneScene() throws GLException, IOException {
		Registry.registerRenderer(CoordPlane.class, new CoordPlaneRenderer());
		add(new CoordPlane());
	}
	
}
