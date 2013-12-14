package xoric.prism.scene.jogl;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import xoric.prism.scene.IScene;

public class PrismSceneJOGL implements IScene, GLEventListener
{
	public static void main(String[] args)
	{
		PrismSceneJOGL s = new PrismSceneJOGL();
		s.start();
	}

	public void start()
	{
		GLJPanel canvas = new GLJPanel();
		JFrame frame = new JFrame();
		frame.getContentPane().add(canvas); // or frame.setContentPane(canvas);
		frame.setVisible(true);
		canvas.addGLEventListener(this);
	}

	@Override
	public void display(GLAutoDrawable arg0)
	{
		System.out.println("jogl display!");
	}

	@Override
	public void dispose(GLAutoDrawable arg0)
	{
		System.out.println("jogl dispose!");
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		System.out.println("jogl init!");

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4)
	{
		System.out.println("jogl reshape!");
	}
}