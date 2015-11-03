package org.cakelab.soapbox.testscene.blenderRaw;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.blender.BlenderRaw;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.blenderRaw.resources.Resources;

public class BlenderScene extends Scene {

	public BlenderScene () throws GLException, IOException {
		
		BlenderRenderer renderer = new BlenderRenderer();
		Registry.registerRenderer(BlenderObject.class, renderer);

		BlenderRaw rawCubeMesh = new BlenderRaw(BlenderRaw.Format.TRIANGLES, Resources.asInputStream(Resources.CUBE_TRIANGLES));
		TriangleMesh cubeMesh = rawCubeMesh.getTriangleMesh();
		renderer.registerMesh(cubeMesh);
		
		BlenderObject bobj = new BlenderCube(cubeMesh);
		
		add(bobj);
	}
}
