package org.cakelab.oge.utils.blender;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.cakelab.appbase.fs.FileSystem;
import org.cakelab.soapbox.model.Mesh;
import org.cakelab.soapbox.model.TriangleMesh;


/**
 * Reads blender raw mesh csv format
 * @author homac
 *
 */
public class BlenderRaw {
	public static enum Format {
		TRIANGLES,
		QUADS
	}

	private TriangleMesh mesh;
	private Format format;
	
	public BlenderRaw(Format format, InputStream in) throws IOException {
		
		this.format = format;
		switch(format) {
		case TRIANGLES:
			readTriangles(in);
			break;
		case QUADS:
			throw new Error("not implemented");
		}
		
	}
	/**
	 * Reads triangles and converts according to the requested {@link Mesh.FrontFaceVertexOrder}.
	 * @param in
	 * @throws IOException
	 */
	private void readTriangles(InputStream in) throws IOException {

		String text = FileSystem.readText(in, Charset.forName("ASCII"));
		String[] lines = text.split("\n");
		
		float[] data = new float[lines.length * 9];

		int i = 0;
		for (String line : lines) {
			String[] columns = line.split("\\ ");
			
			data[i++] = Float.valueOf(columns[0]);
			data[i++] = Float.valueOf(columns[1]);
			data[i++] = Float.valueOf(columns[2]);
			
			data[i++] = Float.valueOf(columns[3]);
			data[i++] = Float.valueOf(columns[4]);
			data[i++] = Float.valueOf(columns[5]);
			
			data[i++] = Float.valueOf(columns[6]);
			data[i++] = Float.valueOf(columns[7]);
			data[i++] = Float.valueOf(columns[8]);
			
		}
		
		mesh = new TriangleMesh(Mesh.FrontFaceVertexOrder.CounterClockwise, 3, data);
	}

	public Mesh getMesh() {
		return mesh;
	}

	public TriangleMesh getTriangleMesh() {
		if (format.equals(Format.TRIANGLES)) {
			return mesh;
		} else {
			throw new Error("not implemented");
		}
	}
	
	
}
