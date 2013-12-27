package xoric.prism.develop.ui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import xoric.prism.common.Common;
import xoric.prism.data.AttachmentHeader;
import xoric.prism.exceptions.PrismMetaFileException;
import xoric.prism.meta.AttachmentLoader;
import xoric.prism.meta.MetaFile;
import xoric.prism.swing.PrismFrame;

public class MetaFileViewer extends PrismFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel nameLabel;
	private JList<String> list;

	public MetaFileViewer()
	{
		super("MetaFileViewer", 600, 400, true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		nameLabel = new JLabel("New label");
		contentPane.add(nameLabel, BorderLayout.NORTH);

		list = new JList<String>();
		contentPane.add(list, BorderLayout.CENTER);

		//		JFileChooser j = new JFileChooser();
		//		contentPane.add(j, BorderLayout.WEST);
		DirTree d = new DirTree();
		//		contentPane.add(d, BorderLayout.WEST);
		d.setVisible(true);
	}

	public void openMetaFile(File file) throws PrismMetaFileException
	{
		MetaFile f = new MetaFile(file);
		f.load();
		nameLabel.setText(file.getName());

		AttachmentLoader a = f.getAttachmentLoader();
		int n = a.getAttachmentCount();

		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < n; ++i)
		{
			AttachmentHeader h = a.get(i);

			StringBuffer sb = new StringBuffer();

			sb.append("[" + i + "] ");
			sb.append(h.getName().toString());
			sb.append(" - " + Common.getFileSize(h.getContentSize()));

			if (h.isCompressed())
				sb.append(" *compressed*");

			model.addElement(sb.toString());
		}
		list.setModel(model);
	}

	public static void main(String[] args)
	{
		try
		{
			MetaFileViewer v = new MetaFileViewer();
			v.openMetaFile(new File("E:/Prism/data/shader/default.sh"));
			v.setVisible(true);
		}
		catch (Exception e0)
		{
			e0.printStackTrace();
		}
	}
}
