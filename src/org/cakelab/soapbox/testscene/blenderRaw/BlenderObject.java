package org.cakelab.soapbox.testscene.blenderRaw;

import org.cakelab.oge.RenderAssets;
import org.cakelab.oge.VisualObject;

public abstract class BlenderObject extends VisualObject {

	private RenderAssets assets;

	public BlenderObject(RenderAssets assets) {
		this.assets = assets;
	}

	public BlenderObject(RenderAssets assets, float x, float y, float z) {
		super(x,y,z);
		this.assets = assets;
	}

	public RenderAssets getRenderAssets() {
		return assets;
	}
}
