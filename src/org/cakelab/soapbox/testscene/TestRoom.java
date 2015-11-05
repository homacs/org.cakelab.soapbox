package org.cakelab.soapbox.testscene;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.RenderAssets;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.blender.BlenderRaw;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.SBM_KTX.Torus;
import org.cakelab.soapbox.testscene.SBM_KTX.TorusRenderer;
import org.cakelab.soapbox.testscene.blenderRaw.BlenderCube;
import org.cakelab.soapbox.testscene.blenderRaw.BlenderObject;
import org.cakelab.soapbox.testscene.blenderRaw.BlenderRenderer;
import org.cakelab.soapbox.testscene.blenderRaw.resources.BlenderResources;
import org.cakelab.soapbox.testscene.coords.CoordPlane;
import org.cakelab.soapbox.testscene.coords.CoordPlaneRenderer;
import org.cakelab.soapbox.testscene.cube.Cube;
import org.cakelab.soapbox.testscene.cube.CubeRenderer;
import org.cakelab.soapbox.testscene.hud.HudObject;
import org.cakelab.soapbox.testscene.hud.HudRenderer;

public class TestRoom extends Scene {
	public TestRoom() throws GLException, IOException {
		
		Registry.registerRenderer(Cube.class, new CubeRenderer());
		Registry.registerRenderer(Torus.class, new TorusRenderer());
		BlenderRenderer blenderRenderer = new BlenderRenderer();
		Registry.registerRenderer(BlenderObject.class, blenderRenderer);
		Registry.registerRenderer(CoordPlane.class, new CoordPlaneRenderer());
		Registry.registerRenderer(HudObject.class, new HudRenderer());

		
		add(new CoordPlane());

		add(new HudObject());
		
		
		for (int i = 0; i < 24; i++) {
			add(new Cube(i));
		}

		add(new Torus(5f, 0f, 0f));

		BlenderRaw rawCubeMesh = new BlenderRaw(BlenderRaw.Format.TRIANGLES, BlenderResources.asInputStream(BlenderResources.CUBE_TRIANGLES));
		TriangleMesh cubeMesh = rawCubeMesh.getTriangleMesh();
		RenderAssets cubeAssets = new RenderAssets(cubeMesh);
		add(new BlenderCube(10f, 0f, 0f, cubeAssets));
		
	}
}
