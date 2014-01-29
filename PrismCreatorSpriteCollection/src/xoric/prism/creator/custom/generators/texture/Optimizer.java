package xoric.prism.creator.custom.generators.texture;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.Point;

class Optimizer
{
	public static final int chapterCount = 2;

	private final List<ObjectImages> objects;
	private final ICreatorFrame frame;
	private final int cores;

	public Optimizer(List<ObjectImages> objects, ICreatorFrame frame)
	{
		this.objects = objects;
		this.frame = frame;
		this.cores = Runtime.getRuntime().availableProcessors();
	}

	private Point findHighestDimensions()
	{
		Point max = new Point();

		for (ObjectImages o : objects)
			o.findHighestDimension(max);

		return max;
	}

	private static int calcNextPowerOfTwo(int v)
	{
		int m = 1;
		while (v > m)
			m = m << 1;

		return m;
	}

	public IOptimizerResults start() throws InterruptedException
	{
		// guess the required size of the texture
		IPoint_r minSize = findHighestDimensions();
		Point guessedSize = guessSize(minSize);

		// find squares that fit all sprites and guess further candidates
		frame.increaseChapter(); // (1)
		List<IOptimizerResults> squares = findSquares(guessedSize);
		List<Point> candidates = generateCandidates(squares, minSize);

		// test all candidates
		frame.increaseChapter(); // (2)
		IOptimizerResults bestSolution = testCandidates(candidates);

		return bestSolution;
	}

	private Point guessSize(IPoint_r minSize)
	{
		int z = minSize.getX();
		if (minSize.getY() > z)
			z = minSize.getY();

		z = calcNextPowerOfTwo(z);

		return new Point(z, z);
	}

	private List<IOptimizerResults> findSquares(Point size) throws InterruptedException
	{
		OptimizerThread[] threads = new OptimizerThread[cores];
		List<IOptimizerResults> solutions = new ArrayList<IOptimizerResults>();
		int n = 1;

		do
		{
			frame.setAction("Guessing texture size (trial " + n + " to " + (n + cores - 1) + ")");

			for (int i = 0; i < cores; ++i)
			{
				threads[i] = new OptimizerThread(objects, size);
				threads[i].start();

				size.x *= 2;
				size.y *= 2;
			}

			for (int i = 0; i < cores; ++i)
			{
				threads[i].join();

				if (threads[i].wasSuccessful() && solutions.size() < 4)
					solutions.add(threads[i]);
			}
		}
		while (solutions.size() < 4);

		return solutions;
	}

	private List<Point> generateCandidates(List<IOptimizerResults> squares, IPoint_r minSize)
	{
		List<Point> candidates = new ArrayList<Point>();

		for (IOptimizerResults s : squares)
		{
			int w = s.getOriginalSize().getX();
			int h = s.getOriginalSize().getY();

			generateCandidates(candidates, minSize, w / 2, h);
			generateCandidates(candidates, minSize, w, h / 2);
		}
		return candidates;
	}

	private void generateCandidates(List<Point> candidates, IPoint_r minSize, int w, int h)
	{
		if (w == h || w <= minSize.getX() || h <= minSize.getY())
			return;

		for (Point p : candidates)
			if (p.x == w && p.y == h)
				return;

		candidates.add(new Point(w, h));

		generateCandidates(candidates, minSize, w / 2, h);
		generateCandidates(candidates, minSize, w / 2, h / 2);
		generateCandidates(candidates, minSize, w, h / 2);
	}

	private IOptimizerResults testCandidates(List<Point> candidates) throws InterruptedException
	{
		frame.setProgressMax(candidates.size() - 1);
		IOptimizerResults bestSolution = null;
		OptimizerThread[] threads = new OptimizerThread[cores];
		int i = 0;

		do
		{
			frame.setAction("Optimizing texture size (" + (i + 1) + " of " + candidates.size() + ")");

			int j = 0;
			while (i < candidates.size() && j < threads.length)
			{
				threads[j] = new OptimizerThread(objects, candidates.get(i));
				threads[j].start();
				++j;
				++i;
			}

			for (int k = 0; k < j; ++k)
			{
				threads[k].join();
				IOptimizerResults r = threads[k];
				frame.setProgress(i - j + k);

				if (bestSolution == null || r.isBetterThan(bestSolution))
					bestSolution = r;
			}
		}
		while (i < candidates.size());

		return bestSolution;
	}
}
