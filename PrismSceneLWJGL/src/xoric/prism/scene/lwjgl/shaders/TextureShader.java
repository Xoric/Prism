package xoric.prism.scene.lwjgl.shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import xoric.prism.scene.art.ITexture;
import xoric.prism.scene.shaders.ITextureShader;

public class TextureShader extends ShaderBase implements ITextureShader
{
	private int textureLocation;

	public TextureShader(int program)
	{
		super(program);
	}

	@Override
	public void setTexture(ITexture texture)
	{
		setTexture(texture.getTextureID());
	}

	@Override
	public void setTexture(int textureID)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL20.glUniform1i(textureLocation, 0); // use texture unit 0
	}

	@Override
	public void initialize()
	{
		super.initialize();
		textureLocation = GL20.glGetUniformLocation(super.programID, "tex");
	}

	// ShaderLWJGL:
	@Override
	protected String getShaderName()
	{
		return "texture shader";
	}
}
