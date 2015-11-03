package org.cakelab.soapbox.testscene;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.blender.BlenderRaw;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.SBM_KTX.Torus;
import org.cakelab.soapbox.testscene.SBM_KTX.TorusRenderer;
import org.cakelab.soapbox.testscene.blenderRaw.BlenderCube;
import org.cakelab.soapbox.testscene.blenderRaw.BlenderObject;
import org.cakelab.soapbox.testscene.blenderRaw.BlenderRenderer;
import org.cakelab.soapbox.testscene.blenderRaw.resources.Resources;
import org.cakelab.soapbox.testscene.cube.Cube;
import org.cakelab.soapbox.testscene.cube.CubeRenderer;

public class TestRoom extends Scene {
	public TestRoom() throws GLException, IOException {
		
		Registry.registerRenderer(Cube.class, new CubeRenderer());
		Registry.registerRenderer(Torus.class, new TorusRenderer());
		BlenderRenderer blenderRenderer = new BlenderRenderer();
		Registry.registerRenderer(BlenderObject.class, blenderRenderer);
		
		for (int i = 0; i < 24; i++) {
			add(new Cube(i));
		}

		add(new Torus(5f, 0f, 0f));

		BlenderRaw rawCubeMesh = new BlenderRaw(BlenderRaw.Format.TRIANGLES, Resources.asInputStream(Resources.CUBE_TRIANGLES));
		TriangleMesh cubeMesh = rawCubeMesh.getTriangleMesh();
		blenderRenderer.registerMesh(cubeMesh);

		add(new BlenderCube(10f, 0f, 0f, cubeMesh));
	}
}
