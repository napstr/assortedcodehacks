package hackerrank.algorithms.implementation.matrixlayerrotation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Solution
{

	public static void main(String[] args)
	{
		Scanner in;
		try
		{
			in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\matrixlayerrotation\\test"));
		} catch (FileNotFoundException e)
		{
			in = new Scanner(System.in);
		}
		int m = in.nextInt();
		int n = in.nextInt();
		int r = in.nextInt();

		int[][] matrix = new int[m][];
		int[][] result = new int[m][];
		for (int i = 0; i < m; i++)
		{
			matrix[i] = new int[n];
			result[i] = new int[n];
			for (int j = 0; j < n; j++)
			{
				int asd = in.nextInt();
				matrix[i][j] = asd;
				result[i][j] = asd;
			}
		}

		int shortest = n > m ? m : n;
		for (int i = 0; i < shortest / 2; i++)
		{

			List<Koords> sequence = new ArrayList<>();

			for (int y = 0 + i; y < m - 1 - i; y++)
			{
				sequence.add(new Koords(y, 0 + i));
			}
			for (int x = 0 + i; x < n - 1 - i; x++)
			{
				sequence.add(new Koords(m - 1 - i, x));
			}
			for (int y = m - 1 - i; y > 0 + i; y--)
			{
				sequence.add(new Koords(y, n - 1 - i));
			}
			for (int x = n - 1 - i; x > 0 + i; x--)
			{
				sequence.add(new Koords(0 + i, x));
			}

			for (Koords k : sequence)
				System.err.println(k + " " + matrix[k.y][k.x]);

			int size = 2 * (m + n - 2) - 8 * i;
			int rotate = r % size;

			for (int j = 0; j < sequence.size(); j++)
			{
				int shifted = j + rotate;
				if (shifted >= sequence.size())
					shifted -= sequence.size();
				Koords old = sequence.get(j);
				Koords neu = sequence.get(shifted);
				result[neu.y][neu.x] = matrix[old.y][old.x];
			}
		}

		for (int[] line : result)
		{
			String out = "";
			for (int i : line)
				out += " " + i;
			System.out.println(out.substring(1));
		}
	}
}

class Koords
{
	int y;
	int x;

	public Koords(int y, int x)
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
		Koords k = (Koords) o;
		if (this.y == k.y && this.x == k.x)
			return true;
		return false;
	}

	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + y;
		result = prime * result + x;
		return result;
	}

	@Override public String toString()
	{
		return "(" + y + "|" + x + ")";
	}
}