package org.cakelab.oge.utils;

import static org.lwjgl.opengl.GL15.nglBufferData;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

/**
 * This class mainly provides a cache for native I/O buffers.
 */
public class GLAPIHelper {
	static class APICache {
		FloatBuffer scalarf = BufferUtils.createFloatBuffer(1);
		FloatBuffer vec4f = BufferUtils.createFloatBuffer(4);
		static ThreadLocal<APICache> tls = new ThreadLocal<APICache>();
		public static FloatBuffer getVec4f() {
			return getCache().vec4f;
		}
		private static APICache getCache() {
			APICache cache = tls.get();
			if (cache== null) {
				cache = new APICache();
				tls.set(cache);
			}
			return cache;
		}
		public static FloatBuffer getScalarF() {
			return getCache().scalarf;
		}
	}

	
	
	public static void glClearBuffer4f(int bits, int drawbuffer, float r, float g, float b, float a) {
		FloatBuffer vec4f = APICache.getVec4f();
		vec4f.put(0, r);
		vec4f.put(1, g);
		vec4f.put(2, b);
		vec4f.put(3, a);
		glClearBufferfv(bits, 0, vec4f);
	}

	public static void glClearBuffer1f(int bits, int drawbuffer, float depth) {
		final FloatBuffer scalarf = APICache.getScalarF();
		scalarf.put(0, depth);
		scalarf.rewind();
		glClearBufferfv(bits, 0, scalarf);
	}

	public static void glBufferData(int target, int size, ByteBuffer address, int usage) {
		nglBufferData(target, size, MemoryUtil.memAddress(address), usage);
	}



}
