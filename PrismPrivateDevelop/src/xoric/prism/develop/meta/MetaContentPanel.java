package xoric.prism.develop.meta;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.data.meta.AttachmentHeader;
import xoric.prism.data.meta.AttachmentLoader;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.meta.TimeStamp;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.IPath_r;

class MetaContentPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel label2;
	private JList<String> list;

	public MetaContentPanel()
	{
		label2 = new JLabel();
		list = new JList<String>();

		JPanel p = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(60, 10, 00, 10), 0, 0);
		p.add(label2, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(30, 10, 30,
				10), 0, 0);
		p.add(list, c);

		add(p);
	}

	public void showContent(IPath_r resPath)
	{
		String targetFilename = null;
		try
		{
			targetFilename = extractTargetFilename(resPath);
			showMetaFile(Prism.global.getDataPath(), targetFilename);
		}
		catch (PrismException e)
		{
			label2.setText(e.code.toString());
			list.removeAll();
		}

	}

	private static String extractTargetFilename(IPath_r resPath) throws PrismException
	{
		File textFile = resPath.getFile("meta.txt");
		MetaList metaList = MetaFileCreator.loadLocalMetaList(textFile);
		MetaBlock mb = metaList.claimMetaBlock(MetaType.DEVELOP);
		MetaLine targetLine = mb.claimLine(MetaKey.TARGET);
		targetLine.ensureMinima(0, 0, 1);
		String targetFilename = targetLine.getHeap().texts.get(0).toString().toLowerCase();

		return targetFilename;
	}

	private void showMetaFile(IPath_r path, String filename) throws PrismException
	{
		MetaFile f = new MetaFile(path, filename);
		f.load();

		int version = f.getLocalFileVersion();
		TimeStamp t = f.getTimeStamp();

		StringBuffer sb = new StringBuffer();
		sb.append("<html><b>" + filename + "</b><br>");
		sb.append("version: " + version + "<br>");
		sb.append("timeStamp: " + t + "<br>");
		sb.append("</html>");
		label2.setText(sb.toString());

		AttachmentLoader a = f.getAttachmentLoader();
		int n = a.getAttachmentCount();

		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < n; ++i)
		{
			AttachmentHeader h = a.get(i);

			sb.setLength(0);
			sb.append("[" + i);

			if (h.isCompressed())
				sb.append("*");

			sb.append("] ");

			sb.append(h.getName().toString());
			sb.append(" - " + Common.getFileSize(h.getContentSize()));

			model.addElement(sb.toString());
		}
		list.setModel(model);
	}

}
