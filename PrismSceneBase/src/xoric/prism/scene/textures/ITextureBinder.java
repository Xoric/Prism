package xoric.prism.scene.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ITextureBinder
{
	public ITexture bindTexture(BufferedImage image) throws IOException;
}
