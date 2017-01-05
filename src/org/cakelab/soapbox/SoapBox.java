package org.cakelab.soapbox;


import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glViewport;

import org.cakelab.oge.Registry;
import org.cakelab.oge.app.ApplicationBase;
import org.cakelab.oge.app.ApplicationContext;
import org.cakelab.oge.scene.DynamicEntity;
import org.cakelab.oge.scene.Scene;
import org.cakelab.oge.scene.VisualEntity;
import org.cakelab.oge.shader.GLException;
import org.cakelab.oge.utils.BufferedMatrix4f;
import org.cakelab.oge.utils.GLAPIHelper;
import org.cakelab.oge.utils.SingleProgramRendererBase;
import org.cakelab.soapbox.testscene.TestRoom;
import org.lwjgl.glfw.GLFW;

public class SoapBox extends ApplicationBase {

	private float aspectRatio;
	private BufferedMatrix4f projection = new BufferedMatrix4f();
	private Player player;
	private Scene scene;

	public SoapBox() throws GLException {
		super("Soapbox");
		info.flags.fullscreen = true;
		info.flags.vsync = false;
		player = new Player();
	}
	
	@Override
	protected void startup() throws Throwable {
		setVirtualCursor(true);
		scene = new TestRoom();
		
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
	protected synchronized void process(double currentTime, ApplicationContext context) {
		glViewport(0, 0, context.getWindowWidth(), context.getWindowHeight());
		GLAPIHelper.glClearBuffer4f(GL_COLOR, 0, 0.5f, 0.5f, 0.5f, 1.0f);
		GLAPIHelper.glClearBuffer1f(GL_DEPTH, 0, 1f);

		player.update(currentTime);
		
		for (DynamicEntity vobj : scene.getDynamicObjects()) {
			vobj.update(currentTime);
		}
		
		context.setActiveCamera(player.getCamera());
		context.setProjectionTransform(projection);

		for (VisualEntity vobj : scene.getVisualObjects()) {
			SingleProgramRendererBase renderer = Registry.getRenderer(vobj.getClass());
			if (renderer == null) {
				throw new Error("no renderer registered for " + vobj.getClass().getCanonicalName());
			}
			// TODO: sort objects by renderer?
			renderer.prepare(context, currentTime);
			renderer.render(context, currentTime, vobj);
		}
	}

	@Override
	protected synchronized void onKey(int key, int scancode, int action, int mods)
			throws Throwable {
	    if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_RELEASE)
	    {
	    	float move = 10f;
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
	            case 'Q': player.addRotationVelocity(0f, 0f, move*5);
	            	break;
	            case 'E': player.addRotationVelocity(0f, 0f, -move*5);
	            	break;
	            case GLFW.GLFW_KEY_LEFT_SHIFT:
	            case GLFW.GLFW_KEY_RIGHT_SHIFT:
	            	player.setVelocityMultiplyier(((action == GLFW.GLFW_PRESS) ? 2.0f : 1.0f));
	            	break;
	            default:
	                break;
	        }
	    }

	}

	@Override
	protected synchronized void onMouseMove(double xpos, double ypos, double xmov, double ymov) {
		double f = 0.03;
		player.addRotation((float)(ymov * f), (float)(xmov * -f), 0);
	}

	@Override
	protected synchronized void onResize(int w, int h) throws Throwable {
		super.onResize(w, h);
		aspectRatio = (float) w / (float) h;
		projection.setPerspective(50.0f, aspectRatio, 0.1f, 1000.0f);
	}

	public static void main(String[] args) throws GLException {
		SoapBox app;
		app = new SoapBox();
		app.run();
	}

}
