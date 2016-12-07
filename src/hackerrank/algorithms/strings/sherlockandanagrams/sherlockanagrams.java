package hackerrank.algorithms.strings.sherlockandanagrams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Solution
{

	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int t = in.nextInt();
		double z = System.currentTimeMillis();

		List<String> ss = new ArrayList<>();

		List<String> subs = new ArrayList<>();
		for (int i = 0; i < t; i++)
		{
			subs.clear();

			String s = in.next();
			int result = 0;
			//length of substring
			for (int j = 1; j < s.length(); j++)
			{

				// substring of length j
				for (int k = 0; k <= s.length() - j; k++)
				{
					String a = s.substring(k, k + j);

					subs.add(a);
					/*
					ss.clear();

					for (int l = k + 1; l < s.length(); l++)
					{

						ss.add(a);
						char c = s.charAt(l);
						int asd = ss.size();
						for (int m = 0; m < asd; m++)
						{
							String b = ss.remove(0);
							int x = b.indexOf(c);
							if (x > -1)
							{
								b = b.substring(0, x) + b.substring(x + 1);
								if (b.length() == 0)
									result++;
								else
									ss.add(b);
							}
						}
					}*/
				}
			}

			for (int j = 0; j < subs.size() - 1; j++) {
				String a = subs.get(j);
				for (int k = j + 1; k < subs.size(); k++) {
					String b = subs.get(k);
					if (a.length() == b.length()) result += isAnagram(a.toCharArray(), b.toCharArray());
				}
			}

			System.out.println(result);
			System.err.println(System.currentTimeMillis() - z);
		}
	}

	private static int isAnagram(char[] a, char[] b) {
		Arrays.sort(a);
		Arrays.sort(b);

		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return 0;
		}
		return 1;
	}
}
