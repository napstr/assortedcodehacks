package network;

import java.util.*;

class Solution
{

	//	private static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c)
	//	{
	//		List<T> list = new ArrayList<>(c);
	//		java.util.Collections.sort(list);
	//		return list;
	//	}

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		int h = in.nextInt();
		List<Koords> koords = new ArrayList<>();
		Map<Long, Long> amounts = new HashMap<>();
		long minY = Long.MAX_VALUE;
		long maxY = Long.MIN_VALUE;
		long minX = Long.MAX_VALUE;
		long maxX = Long.MIN_VALUE;

		for (int i = 0; i < h; i++)
		{
			long x = in.nextInt();
			long y = in.nextInt();
			koords.add(new Koords(y, x));

			if (y < minY)
				minY = y;
			if (y > maxY)
				maxY = y;
			if (x < minX)
				minX = x;
			if (x > maxX)
				maxX = x;

			if (amounts.containsKey(y))
			{
				amounts.put(y, amounts.get(y) + 1);
			} else
			{
				amounts.put(y, 1L);
			}
		}

		long xRange = maxX - minX;

		long below = 0;
		long passing = amounts.get(minY);
		long above = koords.size() - passing;

		List<Long> indices = new ArrayList<>(amounts.keySet());
		Collections.sort(indices);

		long previousY = minY;
		for (long y : indices)
		{
			below += passing;
			passing = amounts.get(y);
			above -= passing;

			//turning point
			//were looking either for this Y or previous one, for the sake of not having to deal with this code further and the question
			//under what circumstances its which one of them just check them both and pick the smaller one
			if (below > above + passing)
			{
				long a = calcLength(koords, previousY, xRange);
				long b = calcLength(koords, y, xRange);

				if (a < b)
					System.out.println(a);
				else
					System.out.println(b);

				break;
			}
			previousY = y;
		}
	}

	private static long calcLength(List<Koords> houses, long y, long xRange)
	{

		long length = xRange;

		for (Koords k : houses)
		{
			long dis = k.y - y;
			if (dis < 0)
				dis *= -1;
			length += dis;
		}

		return length;
	}
}

class Koords
{
	long y;
	long x;

	public Koords(long y, long x)
	{
		this.y = y;
		this.x = x;
	}

	public Koords copy()
	{
		return new Koords(y, x);
	}

	@Override public boolean equals(Object o)
	{
		if (getClass() != o.getClass())
			return false;
		Koords k = (Koords) o;
		return (this.y == k.y && this.x == k.x);
	}

	@Override public int hashCode()
	{
		final int prime = 31;
		long result = 1;
		result = prime * result + y;
		result = prime * result + x;
		return (int) result;
	}

	@Override public String toString()
	{
		return "(" + y + "|" + x + ")";
	}
}