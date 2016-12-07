package optimization;

import java.util.Scanner;

class Player
{
	public static void main(String[] a)
	{
		Scanner i = new Scanner(System.in);
		int W = i.nextInt(), H = i.nextInt(), X = i.nextInt(), Y = i.nextInt();
		for (; ; )
		{
			String r = "";
			if (H < Y)
			{
				r += "N";
				Y--;
			}
			if (H > Y)
			{
				r += "S";
				Y++;
			}
			if (W < X)
			{
				r += "W";
				X--;
			}
			if (W > X)
			{
				r += "E";
				X++;
			}
			System.out.println(r);
		}
	}
}