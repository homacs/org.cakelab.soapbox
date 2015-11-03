package org.cakelab.soapbox.model;

public class Mesh {

	public static enum FrontFace {
		CounterClockwise,
		Clockwise
	}

	protected void copyVertexF(float[] target, int targetPos, float[] source, int sourcePos) {
		int i = 0;
		target[targetPos+i] = source[sourcePos+i];
		i++;
		target[targetPos+i] = source[sourcePos+i];
		i++;
		target[targetPos+i] = source[sourcePos+i];
	}
}
