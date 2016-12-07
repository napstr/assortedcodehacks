package hackerrank.algorithms.implementation.manasaandstones;

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
			in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\manasaandstones\\test"));
		} catch (FileNotFoundException e)
		{
			in = new Scanner(System.in);
		}
		int t = in.nextInt();

		for (int i = 0; i < t; i++)
		{

			int n = in.nextInt();
			int a = in.nextInt();
			int b = in.nextInt();
			List<Integer> check = new ArrayList<>();

			if (a < b)
			{
				int c = a;
				a = b;
				b = c;
			}

			String r = "";
			for (int j = 0; j < n; j++)
			{
				int asd = (j * a + (n - j - 1) * b);
				if (!check.contains(asd))
				{
					check.add(asd);
					r += " " + asd;
				}
			}
			r = r.substring(1);
			System.out.println(r);
		}
	}
}
