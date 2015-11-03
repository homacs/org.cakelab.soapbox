package org.cakelab.soapbox;


import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;

import java.io.IOException;

import org.cakelab.oge.GraphicContext;
import org.cakelab.oge.Registry;
import org.cakelab.oge.Renderer;
import org.cakelab.oge.Scene;
import org.cakelab.oge.VisualObject;
import org.cakelab.oge.app.ApplicationBase;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.BufferedMatrix4f;
import org.cakelab.soapbox.testscene.coords.CoordPlaneScene;
import org.lwjgl.glfw.GLFW;

public class SoapBox extends ApplicationBase {

	private float aspectRatio;
	private BufferedMatrix4f projection = new BufferedMatrix4f();
	private Player player;
	private Scene scene;

	public SoapBox() {
		super("Soapbox");
		info.flags.fullscreen = true;
		info.flags.vsync = false;
		player = new Player();
	}
	
	@Override
	protected void init() {
		super.init();
		
	}


	@Override
	protected void startup() throws GLException, IOException {
		setVirtualCursor(true);
//		scene = new TestRoom();
		scene = new CoordPlaneScene();
		// TODO: not for transparent objects
		glEnable(GL_CULL_FACE);
		// front face counter clockwise
		glFrontFace(GL_CCW);

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		// one fake call for initialisation
		onResize(info.getWindowWidth(),info.getWindowHeight());
	}

	@Override
	protected void shutdown() {
		Registry.delete();
	}

	@Override
	protected synchronized void render(double currentTime, GraphicContext context) {
		
		player.update(currentTime);
		
		for (DynamicObject vobj : scene.getDynamicObjects()) {
			vobj.update(currentTime);
		}
		
		context.setActiveCamera(player.camera);
		context.setProjectionTransform(projection);

		for (VisualObject vobj : scene.getVisualObjects()) {
			Renderer renderer = Registry.getRenderer(vobj.getClass());
			if (renderer == null) {
				throw new Error("no renderer registered for " + vobj.getClass().getCanonicalName());
			}
			renderer.prepare(context, currentTime);
			renderer.render(context, currentTime, vobj);
		}
	}

	@Override
	protected synchronized void onKey(int key, int scancode, int action, int mods)
			throws Throwable {
	    if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_RELEASE)
	    {
	    	float move = 10f * ((mods == GLFW.GLFW_MOD_SHIFT) ? 2.0f : 1.0f);
	    	if (action == GLFW.GLFW_RELEASE) move = -move;
	        switch (key)
	        {
	            case 'W': player.addTranslationVelocity(0f, 0f, -move);
	                break;
	            case 'S': player.addTranslationVelocity(0f, 0f, move);
	                break;
	            case 'A': player.addTranslationVelocity(-move, 0f, 0f);
	                break;
	            case 'D': player.addTranslationVelocity(move, 0f, 0f);
	                break;
	            case 'R': player.addTranslationVelocity(0f, move, 0f);
	            	break;
	            case 'F': player.addTranslationVelocity(0f, -move, 0f);
	                break;
	            case 'Q': player.addRotationVelocity(0f, 0f, move);
	            	break;
	            case 'E': player.addRotationVelocity(0f, 0f, -move);
	            	break;
	            default:
	                break;
	        }
	    }

	}

	@Override
	protected synchronized void onMouseMove(double xpos, double ypos, double xmov, double ymov) {
		double f = 0.05;
		player.addRotation((float)(ymov * -f), (float)(xmov * -f), 0);
		// TODO: disabled until fixed
//		player.addPitchAbsolute((float)(ymov * -f));
//		player.addYawAbsolute((float)(xmov * -f));
	}

	@Override
	protected synchronized void onResize(int w, int h) {
		super.onResize(w, h);
		aspectRatio = (float) w / (float) h;
		projection.setPerspective(50.0f, aspectRatio, 0.1f, 1000.0f);
	}

	public static void main(String[] args) {
		SoapBox app = new SoapBox();
		app.run();
	}

}
