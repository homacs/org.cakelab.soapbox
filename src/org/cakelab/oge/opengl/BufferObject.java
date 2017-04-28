package org.cakelab.oge.opengl;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL40.*;
import static org.lwjgl.opengl.GL42.*;
import static org.lwjgl.opengl.GL43.*;
import static org.lwjgl.opengl.GL44.*;
import static org.lwjgl.opengl.GL45.*;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL;


public class BufferObject {
	public static enum Usage {
		STREAM_DRAW(GL_STREAM_DRAW),
		STREAM_READ(GL_STREAM_READ),
		STREAM_COPY(GL_STREAM_COPY),
		STATIC_DRAW(GL_STATIC_DRAW),
		STATIC_READ(GL_STATIC_READ),
		STATIC_COPY(GL_STATIC_COPY),
		DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
		DYNAMIC_READ(GL_DYNAMIC_READ),
		DYNAMIC_COPY(GL_DYNAMIC_COPY);
		
		public final int v;
		
		Usage(int v) {
			this.v = v;
		}
	}
	
	public static enum Access {
		READ_ONLY(GL_READ_ONLY),
		WRITE_ONLY(GL_WRITE_ONLY),
		READ_WRITE(GL_READ_WRITE),
		;
		
		public final int v;
		Access(int v) {
			this.v = v;
		}
	}
	
	
	public static enum Target {
		// GL15
		ARRAY_BUFFER(GL_ARRAY_BUFFER),
		ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER),
		// GL21
		PIXEL_PACK_BUFFER(GL_PIXEL_PACK_BUFFER),
		PIXEL_UNPACK_BUFFER(GL_PIXEL_UNPACK_BUFFER),
		// GL30
		TRANSFORM_FEEDBACK_BUFFER(GL_TRANSFORM_FEEDBACK_BUFFER),
		UNIFORM_BUFFER(GL_UNIFORM_BUFFER),
		TEXTURE_BUFFER(GL_TEXTURE_BUFFER),
		COPY_READ_BUFFER(GL_COPY_READ_BUFFER),
		COPY_WRITE_BUFFER(GL_COPY_WRITE_BUFFER),
		// GL40
		DRAW_INDIRECT_BUFFER(GL_DRAW_INDIRECT_BUFFER),
		// GL42
		ATOMIC_COUNTER_BUFFER(GL_ATOMIC_COUNTER_BUFFER),
		// GL43
		DISPATCH_INDIRECT_BUFFER(GL_DISPATCH_INDIRECT_BUFFER),
		SHADER_STORAGE_BUFFER(GL_SHADER_STORAGE_BUFFER),
		// GL44
		QUERY_BUFFER(GL_QUERY_BUFFER);
		
		

		
		public final int v;
		
		Target(int glValue) {
			this.v = glValue;
		}
	}

	protected int id;
	protected int target;
	protected Usage usage;
	private boolean mapNamed;
	
	
	
	/** create a buffer and bind it to given target, */
	public BufferObject(Target target, Usage usage) {
		this(target.v, usage);
	}

	/** create a buffer and bind it to given target, 
	 * @see #BufferObject(Target, Usage)*/
	public BufferObject(int target, Usage usage) {
		mapNamed = (0 != GL.getCapabilities().glMapNamedBuffer);
		this.target = target;
		this.usage = usage;
		id = glGenBuffers();
		bind();
	}

	/** Create a buffer object for the given target and order 
	 * memory of the given size from the graphics driver.
	 * @param target Target this buffer will be bound to.
	 * @param size [byte]
	 * @param usage what can i say ..
	 */
	public BufferObject(Target target, int size, Usage usage) {
		this(target.v, usage);
		alloc(size);
	}
	
	/**
	 * bind the buffer to the opengl context. Needed when accessing its memory.
	 */
	public void bind() {
		glBindBuffer(target, id);
	}
	
	/** 
	 * delete this buffer .. and say good bye.
	 */
	public void delete() {
		glDeleteBuffers(id);
	}
	
	/** 
	 * Allocates memory for the buffer object 
	 * with the given size.
	 * 
	 * @param size [byte]
	 */
	protected void alloc(int size) {
		glBufferData(target, size, usage.v);
	}

	/**
	 * Allocates memory and submits data.
	 * @param data
	 */
	protected void data(FloatBuffer data) {
		glBufferData(target, data, usage.v);
	}
	
	/**
	 * Allocates memory and submits data.
	 * @param data
	 */
	protected void data(DoubleBuffer data) {
		glBufferData(target, data, usage.v);
	}
	
	/**
	 * Allocates memory and submits data.
	 * @param data
	 */
	protected void data(IntBuffer data) {
		glBufferData(target, data, usage.v);
	}
	
	/**
	 * Allocates memory and submits data.
	 * @param data
	 */
	protected void data(ByteBuffer data) {
		glBufferData(target, data, usage.v);
	}
	
	/**
	 * Maps this buffer's data in client memory space (possibly expensive!) 
	 * for {@link Access#READ_ONLY},{@link Access#WRITE_ONLY}
	 * or {@link Access#READ_WRITE} access.
	 * 
	 * @param access Type of data access.
	 * @return byte buffer providing access to the native data.
	 */
	public ByteBuffer map(Access access) {
		if (mapNamed) {
			return glMapNamedBuffer(id, access.v);
		} else {
			bind();
			return glMapBuffer(target, access.v);
		}
	}
	
	public void unmap() {
		if (mapNamed) {
			glUnmapNamedBuffer(id);
		} else {
			glUnmapBuffer(target);
		}
	}
	
	
}
