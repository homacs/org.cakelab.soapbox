package org.cakelab.soapbox.testscene.cube;

import org.cakelab.oge.Registry;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;

public class CubeScene extends Scene {
	public CubeScene () throws GLException {
		CubeRenderer cubeRenderer = new CubeRenderer();
		Registry.registerRenderer(Cube.class, cubeRenderer);
		
		for (int i = 0; i < 24; i++) {
			add(new Cube(i));
		}

	}
}
