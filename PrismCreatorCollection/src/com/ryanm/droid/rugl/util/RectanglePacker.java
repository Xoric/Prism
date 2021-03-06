/*
 * Copyright (c) 2007, Ryan McNally All rights reserved. Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met: Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name of
 * the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without
 * specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
 * Modifications: replaced the internal Rectangle class with another implementation.
 */

// source: http://www.java2s.com/Code/Java/2D-Graphics-GUI/Triestopackrectanglesastightlyaspossible.htm

package com.ryanm.droid.rugl.util;

import java.awt.Rectangle;
import java.util.List;

import xoric.prism.data.types.Rect;

/**
 * Tries to pack rectangles as tightly as possible. An implementation of the algorithm described at
 * http://www.blackpawn.com/texts/lightmaps/default.html
 * @author ryanm
 * @param <P>
 *            The type of items to be held
 */
public class RectanglePacker<P>
{

	/**
	 * Determines the outcome of a rectangle-fitting test
	 * @author ryanm
	 */
	private static enum Fit
	{
		/**
		 * Indicates that the rectangle did not fit
		 */
		FAIL,
		/**
		 * Indicates that the rectangle fitted perfectly
		 */
		PERFECT,
		/**
		 * Indicates that the rectangle fitted with room to spare
		 */
		FIT
	};

	private Node root;

	/**
	 * The border to leave around rectangles
	 */
	private int border = 0;

	/**
	 * Builds a new {@link RectanglePacker}
	 * @param width
	 *            The width of the space available to pack into
	 * @param height
	 *            The height of the space available to pack into
	 * @param border
	 *            The border to preserve between packed items
	 */
	public RectanglePacker(int width, int height, int border)
	{
		root = new Node(new Rect(0, 0, width, height));
		this.border = border;
	}

	/**
	 * Builds a list of all {@link Rectangle}s in the tree, for debugging purposes
	 * @param rectangles
	 *            The list to add the tree's {@link Rectangle}s to
	 */
	public void inspectRectangles(List<Rect> rectangles)
	{
		root.getRectangles(rectangles);
	}

	/**
	 * Finds the {@link Rectangle} where an item is stored
	 * @param item
	 *            The item to search for
	 * @return The {@link Rectangle} where that item resides, or null if not found
	 */
	public Rect findRectangle(P item)
	{
		return root.findRectange(item);
	}

	/**
	 * Clears the packer of all items
	 */
	public void clear()
	{
		root = new Node(root.rect);
	}

	/**
	 * Attempts to pack an item of the supplied dimensions
	 * @param width
	 *            The width of the item
	 * @param height
	 *            The height of the item
	 * @param o
	 *            The item to pack
	 * @return The packed location, or null if it will not fit.
	 */
	public Rect insert(int width, int height, P o)
	{
		Node n = root.insert(width + 2 * border, height + 2 * border, o);

		if (n != null)
		{
			Rect r = new Rect(n.rect.getX() + border, n.rect.getY() + border, n.rect.getWidth() - 2 * border, n.rect.getHeight() - 2
					* border);
			return r;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Removes an item from the tree, consolidating the space if possible. The space can easily become fragmented, so don't rely on this to
	 * work as cleverly as you would like.
	 * @param o
	 *            the item to remove
	 * @return <code>true</code> if the item was found, false otherwise
	 */
	public boolean remove(P o)
	{
		return root.remove(o);
	}

	/**
	 * Gets the width of this packer
	 * @return the width of this packer
	 */
	public int getWidth()
	{
		return root.rect.getWidth();
	}

	/**
	 * Gets the height of this packer
	 * @return The height of this packer
	 */
	public int getHeight()
	{
		return root.rect.getHeight();
	}

	private class Node
	{
		private Rect rect;

		private P occupier = null;

		private Node left = null;

		private Node right = null;

		private Node(Rect r)
		{
			this.rect = r;
		}

		private Rect findRectange(P item)
		{
			if (isLeaf())
			{
				if (item == occupier)
				{
					return rect;
				}
				else
				{
					return null;
				}
			}
			else
			{
				Rect l = left.findRectange(item);

				if (l != null)
				{
					return l;
				}
				else
				{
					return right.findRectange(item);
				}
			}
		}

		private Node insert(int width, int height, P o)
		{
			if (!isLeaf())
			{
				Node r = left.insert(width, height, o);

				if (r == null)
				{
					r = right.insert(width, height, o);
				}

				return r;
			}
			else
			{
				if (occupier != null)
				{
					return null;
				}

				Fit fit = fits(width, height);

				switch (fit)
				{
					case FAIL:
						return null;
					case PERFECT:
						occupier = o;
						return this;
					case FIT:
						split(width, height);
						break;
				}

				return left.insert(width, height, o);
			}
		}

		private boolean isLeaf()
		{
			return left == null;
		}

		/**
		 * Determines if this node contains an item, even many levels below
		 * @return <code>true</code> if this node or any of it's descendants holds an item
		 */
		private boolean isOccupied()
		{
			return occupier != null || !isLeaf();
		}

		/**
		 * Removes an item, and consolidates the tree if possible
		 * @param o
		 *            the item to remove
		 * @return <code>true</code> if the item was found, <code>false</code> otherwise
		 */
		private boolean remove(P o)
		{
			if (isLeaf())
			{
				if (occupier == o)
				{
					occupier = null;

					return true;
				}
				return false;
			}
			else
			{
				boolean found = left.remove(o);
				if (!found)
				{
					found = right.remove(o);
				}

				if (found)
				{
					if (!left.isOccupied() && !right.isOccupied())
					{
						left = null;
						right = null;
					}
				}

				return found;
			}
		}

		private void split(int width, int height)
		{
			int dw = rect.getWidth() - width;
			int dh = rect.getHeight() - height;

			assert dw >= 0;
			assert dh >= 0;

			Rect r, l;
			if (dw > dh)
			{
				l = new Rect(rect.getX(), rect.getY(), width, rect.getHeight());

				r = new Rect(l.getX() + width, rect.getY(), rect.getWidth() - width, rect.getHeight());
			}
			else
			{
				l = new Rect(rect.getX(), rect.getY(), rect.getWidth(), height);

				r = new Rect(rect.getX(), l.getY() + height, rect.getWidth(), rect.getHeight() - height);
			}

			left = new Node(l);
			right = new Node(r);
		}

		private Fit fits(int width, int height)
		{
			if (width <= rect.getWidth() && height <= rect.getHeight())
			{
				if (width == rect.getWidth() && height == rect.getHeight())
				{
					return Fit.PERFECT;
				}
				else
				{
					return Fit.FIT;
				}
			}

			return Fit.FAIL;
		}

		private void getRectangles(List<Rect> rectangles)
		{
			rectangles.add(rect);

			if (!isLeaf())
			{
				left.getRectangles(rectangles);
				right.getRectangles(rectangles);
			}
		}
	}
}
