package org.cakelab.soapbox.testscene.SBM_KTX;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;

public class TorusScene extends Scene {
	public TorusScene () throws GLException, IOException {
		Registry.registerRenderer(Torus.class, new TorusRenderer());
		add(new Torus());
	}
}
