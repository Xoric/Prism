package xoric.prism.scene.lwjgl.textures;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import xoric.prism.scene.lwjgl.cleanup.TrashCan;
import xoric.prism.scene.textures.ITexture;
import xoric.prism.scene.textures.ITextureBinder;

public class TextureBinderLWJGL implements ITextureBinder
{
	public static Texture createFromFile(File file) throws IOException
	{
		FileInputStream f = new FileInputStream(file);
		BufferedInputStream inputStream = new BufferedInputStream(f);

		return createFromStream(inputStream);
	}

	public static Texture createFromStream(InputStream inputStream) throws IOException
	{
		BufferedImage image = ImageIO.read(inputStream);
		int width = image.getWidth();
		int height = image.getHeight();

		final AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -height);

		final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);

		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		IntBuffer textureBuffer = BufferUtils.createIntBuffer(pixels.length);
		textureBuffer.put(pixels);
		textureBuffer.rewind();

		return createFromIntBuffer(textureBuffer, width, height);
	}

	public static Texture createFromIntBuffer(IntBuffer textureBuffer, int width, int height) throws IOException
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(buffer);
		int programID = buffer.get(0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, programID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
				textureBuffer);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		Texture t = new Texture(programID, width, height);
		TrashCan.addResource(t);
		return t;
	}

	@Override
	public ITexture bindTexture(BufferedImage image) throws IOException
	{
		int width = image.getWidth();
		int height = image.getHeight();

		final AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -height);

		final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);

		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		IntBuffer textureBuffer = BufferUtils.createIntBuffer(pixels.length);
		textureBuffer.put(pixels);
		textureBuffer.rewind();

		return createFromIntBuffer(textureBuffer, width, height);
	}
}
