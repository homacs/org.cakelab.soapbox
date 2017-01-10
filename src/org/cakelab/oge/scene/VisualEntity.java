package org.cakelab.oge.scene;

import org.cakelab.oge.math.EntityMatrices;

public abstract class VisualEntity extends Entity {
	private ModuleData renderData;

	private EntityMatrices matrices;

	private Material material;

	public VisualEntity(Material material) {
		this(material,0,0,0);
	}
	
	public VisualEntity(Material material, float x, float y, float z) {
		super(x,y,z);
		this.material = material;
		matrices = new EntityMatrices(this);
	}

	public Material getMaterial() {
		return material;
	}

	/**
	 * Retrieve render engine specific data.
	 */
	public ModuleData getRenderData() {
		return renderData;
	}

	/**
	 * Set render engine specific data.
	 */
	public void setRenderData(ModuleData renderData) {
		this.renderData = renderData ;
	}

	public EntityMatrices getMatrices() {
		return matrices;
	}

}
