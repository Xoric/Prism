package xoric.prism.client.ui;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.ui.button.ButtonAction;
import xoric.prism.client.ui.button.IActionExecuter;
import xoric.prism.client.ui.button.UIButton;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.heap.Heap_in;
import xoric.prism.data.heap.Heap_out;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaLine_in;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.FloatRect;
import xoric.prism.data.types.IFloatPoint_r;
import xoric.prism.data.types.IFloatRect_r;
import xoric.prism.data.types.IMetaChild;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.scene.IRendererUI;
import xoric.prism.scene.materials.Materials;

public class UIWindow extends UIFrame implements IUIButtonHost, IMetaChild
{
	private static final Text DEFAULT_TITLE = new Text("WINDOW");

	private IActionExecuter actionExecuter;

	private final FloatRect screenRect;
	//	private IFloatPoint_r screenSize;
	private static final float SAFETY = 30.0f;

	private UIButton closeButton;
	//	private FloatPoint cornerPos;
	//	private FloatPoint cornerOnset;
	private FloatRect cornerRect;
	private boolean isResizing;
	private final List<UIComponent> components;

	public UIWindow(IFloatPoint_r screenSize)
	{
		super(UIIdentifier.WINDOW);

		components = new ArrayList<UIComponent>();

		screenRect = new FloatRect();

		//		this.screenSize = screenSize;
		setText(DEFAULT_TITLE);

		//		UIButton b = new UIButton(this);
		//		b.setText(new Text("MY BUTTON"));
		//		b.setPosition(20.0f, 40.0f);
		//		b.setWidth(120.0f);
		//
		//		addComponent(b);

		//		makeResizable(true);
		//		makeClosable(true);
	}

	public void makeResizable(boolean b)
	{
		if (b && cornerRect == null)
		{
			cornerRect = new FloatRect();
			cornerRect.setSize(Materials.frames.getMeta().getObject(3).getSize());

			rearrangeCorner();
		}
		else if (!b && cornerRect != null)
		{
			cornerRect = null;
		}
	}

	public boolean isResizing()
	{
		return isResizing;
	}

	public void setScreenSize(IFloatPoint_r screenSize)
	{
		screenRect.setSize(screenSize);
		//		this.screenSize = screenSize;
	}

	public List<UIComponent> getComponents()
	{
		return components;
	}

	public void makeClosable(boolean b)
	{
		if (b && closeButton == null)
		{
			closeButton = new UIButton(this);
			closeButton.setClosingWindow(true);
			closeButton.setXRuler(-26.0f, 1.0f);
			closeButton.setYRuler(0.5f, 0.0f);
			closeButton.setWidthRuler(26.0f, 0.0f);
			closeButton.setText(new Text("X"));
			//			addComponent(closeButton);
			registerChild(closeButton);
		}
		else if (!b && closeButton != null)
		{
			unregisterChild(closeButton);
			//			components.remove(closeButton);
			closeButton = null;
		}
	}

	public void addComponent(UIComponent c)
	{
		registerChild(c);
		components.add(c);
		c.rearrange(rect);
	}

	public void setActionExecuter(IActionExecuter actionExecuter)
	{
		this.actionExecuter = actionExecuter;
	}

	@Override
	protected IActiveUI mouseDownConfirmed(IFloatPoint_r mouse)
	{
		if (closeButton != null)
		{
			IActiveUI a = closeButton.mouseDown(mouse);
			if (a != null)
				return a;
		}

		for (UIComponent c : components)
		{
			IActiveUI a = c.mouseDown(mouse);
			if (a != null)
				return a;
		}

		isResizing = false;

		if (cornerRect != null)
		{
			if (cornerRect.contains(mouse))
			{
				float z = (mouse.getX() - cornerRect.getX()) / (cornerRect.getWidth());
				isResizing = (mouse.getY() > z * cornerRect.getY() + (1.0f - z) * cornerRect.getBottom());
			}
		}
		return this;
	}

	private void validateComponentIndex(int componentIndex, String importType) throws PrismException
	{
		if (componentIndex < 0 || componentIndex > components.size())
		{
			IText_r title = getText();
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("importing " + importType + " from ui component failed");
			e.code.addInfo("component index", componentIndex);
			e.code.addInfo("component count", components.size());
			e.code.addInfo("window title", title == null ? "null" : title.toString());
			throw e;
		}
	}

	private void rejectInput(UIComponent c, int componentIndex, String importType) throws PrismException
	{
		IText_r title = getText();
		PrismException e = new PrismException();
		e.user.setText(UserErrorText.INTERNAL_PROBLEM);
		e.code.setText("component does not support import type " + importType);
		e.code.addInfo("component index", componentIndex);
		e.code.addInfo("component type", c.getClass().toString());
		e.code.addInfo("window title", title == null ? "null" : title.toString());
		throw e;
	}

	@Override
	public int importIntFrom(int componentIndex) throws PrismException
	{
		validateComponentIndex(componentIndex, "int");
		UIComponent c = components.get(componentIndex);
		int i = c.importInt();
		if (i < 0)
			rejectInput(c, componentIndex, "int");

		return i;
	}

	@Override
	public Text importTextFrom(int componentIndex) throws PrismException
	{
		validateComponentIndex(componentIndex, "Text");
		UIComponent c = components.get(componentIndex);
		Text t = c.importText();
		if (t == null)
			rejectInput(c, componentIndex, "Text");

		return t;
	}

	@Override
	public void draw(IRendererUI renderer) throws PrismException
	{
		super.draw(renderer);

		if (cornerRect != null)
		{
			Materials.framesDrawer.setup(0, 3, 0);
			Materials.framesDrawer.drawSingle(cornerRect.getTopLeft());
		}

		for (UIComponent c : components)
			c.draw(renderer);

		if (closeButton != null)
			closeButton.draw(renderer);
	}

	@Override
	public void rearrange(IFloatRect_r parentRect)
	{
		screenRect.setSize(parentRect.getSize());
		super.rearrange(parentRect);

		rearrangeCorner();
	}

	private void rearrangeCorner()
	{
		if (cornerRect != null)
			cornerRect.setBottomRight(rect.getBottomRight());
	}

	public void resize(float dx, float dy)
	{
		rect.addSize(dx, dy);

		if (rect.getWidth() < 100.0f)
			rect.setWidth(100.0f);
		else if (rect.getWidth() > screenRect.getWidth())
			rect.setWidth(screenRect.getWidth());

		if (rect.getHeight() < 35.0f)
			rect.setHeight(35.0f);
		else if (rect.getHeight() > screenRect.getHeight())
			rect.setHeight(screenRect.getHeight());

		rearrangeCorner();
		rearrangeChildrenOnly();
	}

	@Override
	public void moveBy(float dx, float dy)
	{
		// limit left
		float x = rect.getX() + dx;
		float m = SAFETY - rect.getWidth();
		float d = m - x;
		//
		if (d > 0.0f)
		{
			dx += d;
		}
		else
		{
			// limit right
			m = screenRect.getWidth() - SAFETY;
			d = m - x;
			if (d < 0.0f)
				dx += d;
		}

		// limit top
		float y = rect.getY() + dy;
		m = SAFETY - rect.getHeight();
		d = m - y;
		if (d > 0.0f)
		{
			dy += d;
		}
		else
		{
			// limit bottom
			m = screenRect.getHeight() - SAFETY;
			d = m - y;
			if (d < 0.0f)
				dy += d;
		}

		// move own stuff
		if (cornerRect != null)
			cornerRect.addPosition(dx, dy);

		// call parent
		super.moveBy(dx, dy);
	}

	public boolean isResizable()
	{
		return cornerRect != null;
	}

	public boolean isClosable()
	{
		return closeButton != null;
	}

	//	@Override
	//	public void pack(OutputStream stream) throws IOException, PrismException
	//	{
	//		super.pack(stream);
	//
	//		IntPacker.pack_s(stream, closeButton == null ? 0 : 1);
	//		IntPacker.pack_s(stream, cornerRect == null ? 0 : 1);
	//
	//		final int n = components.size();
	//		IntPacker.pack_s(stream, n);
	//
	//		for (int i = 0; i < n; ++i)
	//		{
	//			UIComponent c = components.get(i);
	//			UIFactory.appendComponent(stream, c);
	//		}
	//	}
	//
	//	@Override
	//	public void unpack(InputStream stream) throws IOException, PrismException
	//	{
	//		components.clear();
	//
	//		super.unpack(stream);
	//
	//		makeClosable(IntPacker.unpack_s(stream) > 0);
	//		makeResizable(IntPacker.unpack_s(stream) > 0);
	//
	//		final int n = IntPacker.unpack_s(stream);
	//
	//		for (int i = 0; i < n; ++i)
	//		{
	//			UIComponent c = UIFactory.extractComponent(stream, this);
	//			components.add(c);
	//		}
	//	}

	@Override
	public void appendTo(Heap_out h)
	{
		super.appendTo(h);

		// flags
		h.bools.add(closeButton != null);
		h.bools.add(cornerRect != null);

		// components
		//		h.ints.add(components.size());
		//		for (UIComponent c : components)
		//			UIFactory.appendComponent(h, c);
	}

	@Override
	public void extractFrom(Heap_in h) throws PrismException
	{
		super.extractFrom(h);

		// flags
		makeClosable(h.nextBool());
		makeResizable(h.nextBool());

		// components
		//		int n = h.nextInt();
		//		for (int i = 0; i < n; ++i)
		//		{
		//			UIComponent c = UIFactory.extractComponent(h, this);
		//			components.add(c);
		//		}
	}

	public UIComponent findComponent(IFloatPoint_r mouseOnWindow)
	{
		for (int i = components.size() - 1; i >= 0; --i)
		{
			UIComponent c = components.get(i);

			if (c != closeButton)
				if (c.rect.contains(mouseOnWindow))
					return c;
		}
		return null;
	}

	public void removeComponent(UIComponent c)
	{
		components.remove(c);
	}

	@Override
	public void load(MetaList_in metaList) throws PrismException
	{
		MetaBlock_in mb = metaList.claimMetaBlock(MetaType.WINDOW);
		load(mb);
	}

	public void load(MetaBlock_in mb) throws PrismException
	{
		for (MetaLine_in ml : mb.getMetaLines())
		{
			// extract identifier
			Heap_in h = ml.getHeap();
			int v = h.nextInt();
			UIIdentifier id = UIIdentifier.valueOf(v);

			if (id == UIIdentifier.WINDOW)
			{
				this.extractFrom(h);
			}
			else
			{
				UIComponent c = UIFactory.loadComponent(id, h, screenRect.getSize(), this);
				this.addComponent(c);
			}
		}
		//		rearrange(screenRect);
	}

	@Override
	public void executeAction(ButtonAction a) throws PrismException
	{
		actionExecuter.execute(this, a);
	}
}
