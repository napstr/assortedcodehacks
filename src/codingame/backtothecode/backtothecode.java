package backtothecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Player
{

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		int opponentCount = in.nextInt(); // Opponent count

		List<Koords> path = new ArrayList<>();
		path.add(new Koords((int) (Math.random() * 36), (int) (Math.random() * 21)));

		// game loop
		// action: "x y" to move or "BACK rounds" to go back in time
		while (true)
		{
			int gameRound = in.nextInt();
			int x = in.nextInt(); // Your x position
			int y = in.nextInt(); // Your y position
			Koords current = new Koords(x, y);
			int backInTimeLeft = in.nextInt(); // Remaining back in time
			for (int i = 0; i < opponentCount; i++)
			{
				int opponentX = in.nextInt(); // X position of the opponent
				int opponentY = in.nextInt(); // Y position of the opponent
				int opponentBackInTimeLeft = in.nextInt(); // Remaining back in time of the opponent
			}
			for (int i = 0; i < 20; i++)
			{
				String line = in.next(); // One line of the map ('.' = free, '0' = you, otherwise the id of the opponent)
			}

			if (path.get(0).equals(current))
			{
				path.remove(0);
				path.add(new Koords((int) (Math.random() * 36), (int) (Math.random() * 21)));
			}

			System.out.println(path.get(0));
		}
	}
}

class Koords
{
	int x;
	int y;

	public Koords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Koords copy()
	{
		return new Koords(x, y);
	}

	@Override public boolean equals(Object o)
	{
		if (getClass() != o.getClass())
			return false;
		Koords k = (Koords) o;
		return (this.x == k.x && this.y == k.y);
	}

	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override public String toString()
	{
		return x + " " + y;
	}
}