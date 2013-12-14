package xoric.prism.client.bootstrap;

import java.io.File;

import xoric.prism.client.main.PrismClient;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ModuleID;
import xoric.prism.exceptions.PrismMetaFileException;
import xoric.prism.meta.MetaFile;
import xoric.prism.scene.IScene;
import xoric.prism.scene.lwjgl.PrismSceneLWJGL;

public class PrismClientBootstrap
{
	private static IScene scene;
	private static PrismClient client;

	public static void main(String[] args)
	{
		// register as default module
		ErrorCode.defaultModuleID = ModuleID.CLIENT;

		// create scene and client
		scene = new PrismSceneLWJGL();
		client = new PrismClient(scene);

		/*
		MetaFile f = new MetaFile(new File("E:/Temp/Prism/test.tmp"));
		MetaBlock b = new MetaBlock();
		MetaLine l = new MetaLine();
		l.getHeap().ints.add(13);
		b.addMetaLine(l);
		f.getMetaList().addMetaBlock(b);
		try
		{
			f.write();
		}
		catch (PrismMetaFileException e)
		{
			e.printStackTrace();
		}
		*/
		MetaFile f = new MetaFile(new File("E:/Temp/Prism/test.tmp"));
		try
		{
			f.load();
		}
		catch (PrismMetaFileException e)
		{
			e.printStackTrace();
		}

		// start client
		client.start();
	}
}