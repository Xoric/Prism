package xoric.prism.creator.drawer.image;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class VariationSelector extends JPanel implements IVariationSelector
{
	private static final long serialVersionUID = 1L;

	private JComboBox<String> combo;

	public VariationSelector()
	{
		combo = new JComboBox<String>();
		combo.addItem("Variation 1");
		combo.addItem("Variation 2");

		// TODO 
		// 	control.requestDeleteAnimation(animationIndex, variation);
	}

	@Override
	public int getCurrentVariation()
	{
		return combo.getSelectedIndex();
	}
}
