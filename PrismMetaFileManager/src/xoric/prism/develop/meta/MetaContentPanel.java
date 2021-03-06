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
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaTimeStamp;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.IPath_r;

class MetaContentPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel label2;
	private final JList<String> list0;
	private final JList<String> list;

	public MetaContentPanel()
	{
		label2 = new JLabel();
		list0 = new JList<String>();
		list = new JList<String>();

		JPanel p = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(60, 10, 0, 10), 0, 0);
		p.add(label2, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 0, 10), 0, 0);
		p.add(list0, c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 30,
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
			list0.removeAll();
			list.removeAll();
		}
	}

	private static String extractTargetFilename(IPath_r resPath) throws PrismException
	{
		File textFile = resPath.getFile("meta.txt");
		MetaList_in metaList = MetaFileCreator.loadLocalMetaList(textFile);
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.DEVELOP);
		MetaLine_in targetLine = mb.claimLine(MetaKey.TARGET);
		targetLine.ensureMinima(0, 0, 1);
		String targetFilename = targetLine.getHeap().texts.get(0).toString().toLowerCase();

		return targetFilename;
	}

	private void showMetaFile(IPath_r path, String filename) throws PrismException
	{
		MetaFile mf = new MetaFile(path, filename);
		mf.load();

		int version = mf.getLocalFileVersion();
		MetaTimeStamp t = mf.getTimeStamp();

		StringBuffer sb = new StringBuffer();
		sb.append("<html><b>" + filename + "</b><br>");
		sb.append("version: " + version + "<br>");
		sb.append("timeStamp: " + t + "<br>");
		sb.append("</html>");
		label2.setText(sb.toString());

		// MetaBlocks
		int n = mf.getMetaList().getBlockCount();

		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < n; ++i)
		{
			MetaBlock_in mb = mf.getMetaList().getMetaBlock(i);

			sb.setLength(0);
			sb.append("[" + i + "] ");
			sb.append(mb.getMetaType().toString());
			sb.append(" (" + mb.getLineCount() + ")");

			model.addElement(sb.toString());
		}
		list0.setModel(model);

		// attachments
		AttachmentLoader a = mf.getAttachmentLoader();
		n = a.getAttachmentCount();

		model = new DefaultListModel<String>();
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
