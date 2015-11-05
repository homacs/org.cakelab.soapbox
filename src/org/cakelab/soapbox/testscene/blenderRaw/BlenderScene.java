package org.cakelab.soapbox.testscene.blenderRaw;

import java.io.IOException;

import org.cakelab.oge.Registry;
import org.cakelab.oge.RenderAssets;
import org.cakelab.oge.Scene;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.texture.ImageIOTexture;
import org.cakelab.oge.texture.Texture;
import org.cakelab.oge.utils.blender.BlenderRaw;
import org.cakelab.soapbox.model.TriangleMesh;
import org.cakelab.soapbox.testscene.blenderRaw.resources.BlenderResources;
import org.lwjgl.opengl.GL11;

public class BlenderScene extends Scene {

	public BlenderScene () throws GLException, IOException {
		
		BlenderRenderer renderer = new BlenderRenderer();
		Registry.registerRenderer(BlenderObject.class, renderer);

		BlenderRaw rawCubeMesh = new BlenderRaw(BlenderRaw.Format.TRIANGLES, BlenderResources.asInputStream(BlenderResources.CUBE_TRIANGLES));
		TriangleMesh cubeMesh = rawCubeMesh.getTriangleMesh();
		RenderAssets cubeAssets = new RenderAssets(cubeMesh);
		
		int pixelFormat = GL11.GL_RGBA;
		boolean flipped = true;
		boolean forceAlpha = false;
		Texture cubeTexture = new ImageIOTexture(BlenderResources.asImage(BlenderResources.CUBE_TEXTURE), 
				pixelFormat, flipped, forceAlpha, null, GL11.GL_NEAREST, GL11.GL_NEAREST);
		
		BlenderObject bobj = new BlenderCube(cubeAssets);
		
		add(bobj);
	}
}
