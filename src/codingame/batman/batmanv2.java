//import java.util.Scanner;
//
///**
// * Created by napster on 09.08.2015.
// * for the sake of keeping this simple variables and function are going to be kept static and the main may get big
// */
//class Player
//{
//
//	public static void main(String args[])
//	{
//		Scanner in = new Scanner(System.in);
//		int width = in.nextInt(); // width of the building.
//		int height = in.nextInt(); // height of the building.
//		int maxTurns = in.nextInt(); // maximum number of turns before game over.
//		int currentX = in.nextInt();
//		int currentY = in.nextInt();
//		Koords current = new Koords(currentX, currentY);
//
//		//first read of indicator needs to be done to get rid of the UNKNOWN value
//		String indicator = in.next();
//
//		//these hold the previous position of batman
//		Koords last = new Koords(current.x, current.y);
//
//		//first jump can be almost anywhere, so were using random
//		current = randomJump();
//		System.out.println(current.x + " " + current.y);
//
//		//printMap();
//
//		// true game loop starts now
//		while (true)
//		{
//			indicator = in.next(); // Current distance to the bomb compared to previous distance (COLDER, WARMER, SAME or UNKNOWN)
//
//			//jump to the next spot
//			last = new Koords(current.x, current.y);
//			current = randomJump();
//
//
//			System.out.println(current.x + " " + current.y);
//		}
//	}
//
//	/**
//	 * @param a point A
//	 * @param b point B
//	 * @return distance between point A and point B
//	 */
//	public static double distance(Koords a, Koords b)
//	{
//		int diffX = a.x - b.x;
//		int diffY = a.y - b.y;
//		return Math.sqrt((diffX * diffX) + (diffY * diffY));
//	}
//}
//
//class Koords
//{
//	int y;
//	int x;
//
//	public Koords(int y, int x)
//	{
//		this.y = y;
//		this.x = x;
//	}
//
//	public Koords copy()
//	{
//		return new Koords(y, x);
//	}
//
//	@Override public boolean equals(Object o)
//	{
//		if (getClass() != o.getClass())
//			return false;
//		Koords k = (Koords) o;
//		return (this.y == k.y && this.x == k.x);
//	}
//
//	@Override public int hashCode()
//	{
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + y;
//		result = prime * result + x;
//		return result;
//	}
//
//	@Override public String toString()
//	{
//		return "(" + y + "|" + x + ")";
//	}
//}