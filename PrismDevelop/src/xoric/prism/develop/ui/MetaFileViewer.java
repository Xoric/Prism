package xoric.prism.develop.ui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import xoric.prism.data.exceptions2.PrismException2;
import xoric.prism.data.meta.AttachmentHeader;
import xoric.prism.data.meta.AttachmentLoader;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.TimeStamp;
import xoric.prism.data.tools.Common;
import xoric.prism.data.types.Path;
import xoric.prism.swing.PrismFrame;

public class MetaFileViewer extends PrismFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lLabel;
	private JList<String> list;

	public MetaFileViewer()
	{
		super("MetaFileViewer", 600, 400, true);

		final int BORDER = 15;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(BORDER, BORDER, BORDER, BORDER));
		setContentPane(contentPane);

		BorderLayout b = new BorderLayout(0, 0);
		b.setVgap(BORDER);
		contentPane.setLayout(b);

		lLabel = new JLabel("Text");
		contentPane.add(BorderLayout.NORTH, lLabel);

		list = new JList<String>();
		contentPane.add(BorderLayout.CENTER, list);

		//		JFileChooser j = new JFileChooser();
		//		contentPane.add(j, BorderLayout.WEST);
		DirTree d = new DirTree();
		//		contentPane.add(d, BorderLayout.WEST);
		d.setVisible(true);
	}

	public void openMetaFile(Path path, String filename) throws PrismException2
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
		lLabel.setText(sb.toString());

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

	public static void main(String[] args)
	{
		try
		{
			MetaFileViewer v = new MetaFileViewer();

			Path path = new Path("E:/Prism/data");
			v.openMetaFile(path, "shader/default.sh");
			v.setVisible(true);
		}
		catch (PrismException2 e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}
}
