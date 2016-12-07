package algorithmiccomplexity;

import java.util.*;

class Solution
{

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);

		SortedMap<Integer, Integer> data = new TreeMap<>();

		int j = in.nextInt();
		for (int i = 0; i < j; i++)
		{
			int n = in.nextInt();
			int t = in.nextInt();
			data.put(n, t);
		}

		List<List<Double>> xs = new ArrayList<>();

		//0 - O(1)
		xs.add(0, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / 1.0;
			if (tmp > 0.0)
			{
				xs.get(0).add(tmp);
			}
		}

		//1 - O(log n)
		xs.add(1, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / Math.log10(n);
			if (tmp > 0.0)
			{
				xs.get(1).add(tmp);
			}
		}

		//2 - O(n)
		xs.add(2, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / (double) n;
			if (tmp > 0.0)
			{
				xs.get(2).add(tmp);
			}
		}

		//3 - O(n log n)
		xs.add(3, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / (n * Math.log10(n));
			if (tmp > 0.0)
			{
				xs.get(3).add(tmp);
			}
		}

		//4 - O(n^2)
		xs.add(4, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / (double) (n * n);
			if (tmp > 0.0)
			{
				xs.get(4).add(tmp);
			}
		}

		//5 - O(n^2 log n)
		xs.add(5, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / (n * n * Math.log10(n));
			if (tmp > 0.0)
			{
				xs.get(5).add(tmp);
			}
		}

		//6 - O(n^3)
		xs.add(6, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / (double) (n * n * n);
			if (tmp > 0.0)
			{
				xs.get(6).add(tmp);
			}
		}

		//7 - O(2^n)
		xs.add(7, new ArrayList<>());
		for (Integer n : data.keySet())
		{
			Integer t = data.get(n);
			Double tmp = t / (Math.pow(2, n));
			if (tmp > 0.0)
			{
				xs.get(7).add(tmp);
			}
		}

		//sort out the worst 5% and best 5%
		//the O(n^3) would not work without this, got the tip from the forums
		for (List<Double> asd : xs)
		{
			Collections.sort(asd);
			int fivepercent = asd.size() / 20;

			for (int i = 0; i < fivepercent; i++)
			{
				asd.remove(asd.size() - (i + 1));
			}
			for (int i = 0; i < fivepercent; i++)
			{
				asd.remove(i + 1);
			}
		}

		//now find that set of x that variates the least
		//what we actually do is, we find the difference between the data and the average, and then we calculate
		//the _percentage_ it is off
		Map<Integer, Double> streuung = new TreeMap<>();
		for (int k = 0; k < 8; k++)
		{
			List<Double> x = xs.get(k);
			double average = 0;
			for (Double aX : x)
			{
				average += aX;
			}
			average = average / x.size();

			Double varianz = 0.0;

			for (Double aX : x)
			{
				double diff = average - aX;
				if (diff < 0)
					diff *= -1;
				varianz += diff / average;
			}
			varianz /= x.size();
			streuung.put(k, varianz);
		}

		Double bestVarianz = Double.MAX_VALUE;
		int complexity = -1;

		for (int k = 0; k < 8; k++)
		{
			Double v = streuung.get(k);
			if (v < bestVarianz)
			{
				bestVarianz = v;
				complexity = k;
			}
		}

		String result = "";
		switch (complexity)
		{
		case 0:
			result = "O(1)";
			break;
		case 1:
			result = "O(log n)";
			break;
		case 2:
			result = "O(n)";
			break;
		case 3:
			result = "O(n log n)";
			break;
		case 4:
			result = "O(n^2)";
			break;
		case 5:
			result = "O(n^2 log n)";
			break;
		case 6:
			result = "O(n^3)";
			break;
		case 7:
			result = "O(2^n)";
			break;
		case -1:
			result = "This should not happen lol";
			break;
		}

		System.out.println(result);
	}
}