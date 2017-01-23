package org.cakelab.lwjgl;

import static org.lwjgl.system.JNI.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.FunctionProvider;
import org.lwjgl.system.SharedLibrary;
/**
 * <p>
 * This class provides access to native functions, which are available in lwjgl 
 * (respective glx) but not exposed to the API programmer.
 * </p>
 * 
 * I really don't know why everybody today is trying to hide shit as if everybody
 * else would be able to destroy anything when making mistakes! If you don't like
 * to answer bug reports or rookie questions, then just don't do it!
 * @author homac
 */
public class LWJGL_EXT {
	/** 
	 * GLFW native functions.
	 */
	public static class glfw {
		private static final SharedLibrary library = GLFW.getLibrary();
		
	    public static final long glfwGetX11Display = getFunctionAddress("glfwGetX11Display");
	    /**
	     * @return The Display used by GLFW, or NULL if an error occurred.
	     */
	    public static long glfwGetX11Display() {
			return invokeP(glfwGetX11Display);
	    }

	    /** check whether a given function is available */
	    public static boolean isFunctionAvailable(long functionAddress) {
	    	return functionAddress != 0;
	    }
	    /** check whether the required native library is loaded */
	    public static boolean isLibraryAvailable() {
	    	return library != null;
	    }

	    static long getFunctionAddress(String functionName) {
			return library == null ? 0 : library.getFunctionAddress(functionName);
		}
	    
	}

	/** 
	 * GLX native functions.
	 */
	public static class glx {
		private static final FunctionProvider library = GL.getFunctionProvider();
		
	    public static final long glXSwapIntervalEXT = getFunctionAddress("glXSwapIntervalEXT");
		public static void glXSwapIntervalEXT(long display, long drawable, int v) {
			long __functionAddress = glx.glXSwapIntervalEXT;
			invokePPV(__functionAddress, display, drawable, v);
		}

		/** check whether a given function is available*/
	    public static boolean isFunctionAvailable(long functionAddress) {
	    	return functionAddress != 0;
	    }
	    /** check whether the required native library is loaded */
	    public static boolean isLibraryAvailable() {
	    	return library != null;
	    }
	    
	    public static long getFunctionAddress(String functionName) {
			return library == null ? 0 : library.getFunctionAddress(functionName);
		}
	}
	

}
