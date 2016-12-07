package zombies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Save humans, destroy zombies!
 */
class Player
{

	static final double SHOOTING_RANGE = 1999;
	static final double ZOMBIE_WALKING = 400;
	static final double ASH_WALKING = 1000;

	static List<Human> humans = new ArrayList<>();
	static List<Zombie> zombies = new ArrayList<>();
	static Koords ash;
	static int gameTurn = 0;

	static Sound sound = new Sound();

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);

		// game loop
		gameLoop:
		while (true)
		{
			gameTurn++;
			boolean havingFun = false;
			//reading data
			readData(in);

			//most immediate action necessary
			List<Danger> dangers = immediateDanger();

			if (dangers.size() == 0)
			{
				havingFun = true;
			}

			Target t = null;
			dangerLoop:
			for (Danger d : dangers)
			{
				Target asd = canSave(d);
				if (asd != null)
				{
					t = asd;
					break dangerLoop;
				}
			}

			//we have a target, which means, we need to get in range of SHOOTING_RANGE units of it
			//TODO alternatively grabbing its attention
			if (t != null)
			{
				if (t.excessTurns < 2) //TODO may get <1 if we dont run into the opposite direction
				{
					Koords asd = next(t.koords, ash, (int) SHOOTING_RANGE);
					System.out.println(asd + sound.s(Sound.S.DANGER));
					continue gameLoop;
				} else
				{
					//TODO no urgency, lets have some fun
					//important: wherever we go, the target should stay reachable in the following turns
					havingFun = true;
				}
			}

			//having fun
			//TODO bait zombies, grab attention
			if (havingFun)
			{
				//find the closest zombie in the next turn and go in its direction
				double closestDist = Double.MAX_VALUE;
				Zombie closestZ = zombies.get(0);
				for (Zombie z : zombies)
				{
					double dist = distance(ash, z.next);
					if (dist < closestDist)
					{
						closestDist = dist;
						closestZ = z;
					}
				}

				System.out.println(next(ash, closestZ.next, (int) ASH_WALKING) + sound.s(Sound.S.FUN));
				continue gameLoop;
			}

			//TODO one zombie (or maybe a few on one spot) left? go straight for face, or maybe a seek and destroy mode when no immediate danger is present
			if (zombies.size() < 2)
			{
				System.out.println(zombies.get(0).next + sound.s(Sound.S.HUNTING));
				continue gameLoop;
			}

			//last resort: go to the middle of the map
			Koords result = new Koords(8000, 4500);
			System.out.println(result + sound.s(Sound.S.CHILL)); // Your destination coordinates

		}
	}

	public static Target canSave(Danger d)
	{
		//can we save it? if yes, when is the earliest we can save it?
		//as a soft alternative, can we grab its attention?

		Koords zombieAt = d.z.current;
		double closestDist = Double.MAX_VALUE;
		int excessTurns = 0;
		for (int i = 1; i < d.rounds; i++)
		{
			zombieAt = zombieNext(zombieAt, d.h.koords);

			double dist = distance(zombieAt, ash);

			//go for a kill
			int turnsItTakesToGetThere = turnsToCrossTheDist(dist);
			int excess = i - turnsItTakesToGetThere;

			if (excess >= 0)
			{
				if (dist < closestDist)
				{
					closestDist = dist;
					excessTurns = excess;
				}
			}

			//go for attention
			int zToH = (int) Math.floor(distance(zombieAt, d.h.koords));
			Koords attentionGrab = next(zombieAt, ash, zToH - 2);

			double distAttention = distance(ash, attentionGrab);
			int turnsToGrabAttention = turnsToCrossTheDist(distAttention);
			int attentionExcess = i - turnsToGrabAttention;
			if (attentionExcess >= 0)
			{
				if (dist < closestDist)
				{
					closestDist = dist;
					excessTurns = attentionExcess;
				}
			}

		}
		if (closestDist < Double.MAX_VALUE)
		{
			System.err.println("yes we can");
			return new Target(excessTurns, zombieAt);
		} else
		{
			System.err.println("No we can't =(");
			return null;
		}

	}

	//takes the paths of the zombies and checks how many excessTurns they need to reach a human
	public static List<Danger> immediateDanger()
	{
		List<Danger> result = new ArrayList<>();
		for (Zombie z : zombies)
		{
			double closestDist = Double.MAX_VALUE;
			Human closestHuman = humans.get(0);
			for (Human h : humans)
			{
				double dist = distance(z.current, h.koords);
				if (dist < closestDist)
				{
					closestDist = dist;
					closestHuman = h;
				}
			}

			//only a danger situation is ash is not closer to the zombie
			if (distance(z.current, ash) >= closestDist)
				result.add(new Danger(closestHuman, z, (int) Math.ceil(closestDist / ZOMBIE_WALKING)
						+ 1));//adding +1 helps to simulate that zombies eat their targets at the end of the turn
		}

		Collections.sort(result);
		return result;
	}

	public static int turnsToCrossTheDist(double dist)
	{
		return (int) Math.ceil((dist - SHOOTING_RANGE) / ASH_WALKING);
	}

	public static double distance(Koords a, Koords b)
	{

		int xdif = a.x - b.x;
		if (xdif < 0)
			xdif *= -1;

		int ydif = a.y - b.y;
		if (ydif < 0)
			ydif *= -1;

		return Math.sqrt(xdif * xdif + ydif * ydif);
	}

	//returns a point on the map by the following criteria:
	//a unit moving from m to t by the length of dist
	//round the result down as stated in the rules
	private static Koords next(Koords m, Koords t, int dist)
	{
		//reaching it ?
		if (distance(m, t) <= dist)
			return t;

		double a = t.x - m.x + 0.0;
		double b = t.y - m.y + 0.0;

		//moving straight upward/downward
		if (a == 0)
		{
			//up
			if (b < 0)
				return new Koords(m.x, m.y - dist);
				//down
			else
				return new Koords(m.x, m.y + dist);
		}

		//moving straight left/right
		if (b == 0)
		{
			//left
			if (a < 0)
				return new Koords(m.x - dist, m.y);
				//right
			else
				return new Koords(m.x + dist, m.y);

		}

		//doing the above thing should prevent this from being 0
		double x = b / a;
		if (x < 0)
			x *= -1;

		double distX = Math.sqrt((dist * dist) / ((x * x) + 1));
		double distY = distX * x;

		if (a < 0)
			distX *= -1;
		if (b < 0)
			distY *= -1;

		Koords result = new Koords(m.x + (int) Math.floor(distX), m.y + (int) Math.floor(distY));

		//wonky safeguard
		if (result.x < 0)
			result.x = 0;
		if (result.y < 0)
			result.y = 0;
		return result;
	}

	public static Koords zombieNext(Koords z, Koords h)
	{
		return next(z, h, (int) ZOMBIE_WALKING);
	}

	public static void readData(Scanner in)
	{
		int xA = in.nextInt();
		int yA = in.nextInt();
		ash = new Koords(xA, yA);
		int humanCount = in.nextInt();
		humans.clear();
		for (int i = 0; i < humanCount; i++)
		{
			int humanId = in.nextInt();
			int humanX = in.nextInt();
			int humanY = in.nextInt();
			humans.add(new Human(humanId, humanX, humanY));
		}
		int zombieCount = in.nextInt();
		zombies.clear();
		for (int i = 0; i < zombieCount; i++)
		{
			int zombieId = in.nextInt();
			int zombieX = in.nextInt();
			int zombieY = in.nextInt();
			int zombieXNext = in.nextInt();
			int zombieYNext = in.nextInt();
			zombies.add(new Zombie(zombieId, zombieX, zombieY, zombieXNext, zombieYNext));
		}
	}
}

class Target
{
	int excessTurns;
	Koords koords;

	public Target(int t, Koords k)
	{
		this.excessTurns = t;
		this.koords = k;
	}
}

class Danger implements Comparable<Danger>
{
	Human h;
	Zombie z;
	int rounds;

	public Danger(Human h, Zombie z, int r)
	{
		this.h = h;
		this.z = z;
		this.rounds = r;
	}

	@Override public int compareTo(Danger o)
	{
		return this.rounds - o.rounds;
	}

	@Override public String toString()
	{
		return "Danger: Zombie " + z.id + " at " + z.current + " killing human " + h.id + " at " + h.koords + " in " + rounds + " turns";
	}
}

class Zombie
{
	int id;
	Koords current;
	Koords next;

	public Zombie(int id, Koords c, Koords n)
	{
		this.id = id;
		this.current = c;
		this.next = n;
	}

	public Zombie(int id, int xC, int yC, int xN, int yN)
	{
		this.id = id;
		this.current = new Koords(xC, yC);
		this.next = new Koords(xN, yN);
	}
}

class Human
{
	int id;
	Koords koords;

	public Human(int id, Koords k)
	{
		this.koords = k;
		this.id = id;
	}

	public Human(int id, int x, int y)
	{
		this.id = id;
		this.koords = new Koords(x, y);
	}
}

class Koords
{
	int y;
	int x;

	public Koords(int x, int y)
	{
		this.y = y;
		this.x = x;
	}

	public Koords copy()
	{
		return new Koords(x, y);
	}

	@Override public boolean equals(Object o)
	{
		if (o == null)
			return false;
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
		return x + " " + y;
	}

}

class Sound
{

	enum S
	{
		DANGER, CHILL, FUN, HUNTING
	}

	List<String> danger = new ArrayList<>();
	List<String> chilling = new ArrayList<>();
	List<String> fun = new ArrayList<>();
	List<String> hunting = new ArrayList<>();

	public Sound()
	{
		danger.add("OH CRAP OH CRAP");
		danger.add("OH CRAP OH CRAP");
		danger.add("FUCK FUCK FUCK");
		danger.add("FUCK FUCK FUCK");
		danger.add("SHIT SHIT SHIT");
		danger.add("SHIT SHIT SHIT");
		danger.add("DUMB NPC");
		danger.add("DUMB NPC");
		danger.add("DONT U DIE");
		danger.add("DONT U DIE");

		chilling.add("GRABBING A SMOKE");
		chilling.add("GRABBING A SMOKE");
		chilling.add("HERE THEY COME");
		chilling.add("HERE THEY COME");
		chilling.add("THE LAST STAND");
		chilling.add("THE LAST STAND");
		chilling.add("COMBOS ARE OVERRATED");
		chilling.add("COMBOS ARE OVERRATED");

		fun.add("MUCH FUN");
		fun.add("MUCH FUN");
		fun.add("PEW PEW");
		fun.add("PEW PEW");
		fun.add("SUCH GORE");
		fun.add("SUCH GORE");
		fun.add("RATATATAT");
		fun.add("RATATATAT");

		hunting.add("LAST ONE");
		hunting.add("LAST ONE");
		hunting.add("SEEK AND DESTROY");
		hunting.add("SEEK AND DESTROY");
		hunting.add("BEEP BEEP");
		hunting.add("BEEP BEEP");

	}

	public String s(S s)
	{
		String result = " ";
		List<String> list = new ArrayList<>();
		list.add("FOOBAR");
		switch (s)
		{
		case DANGER:
			list = danger;
			break;
		case CHILL:
			list = chilling;
			break;
		case FUN:
			list = fun;
			break;
		case HUNTING:
			list = hunting;
			break;
		}

		String tmp = list.remove(0);
		result += tmp;
		list.add(tmp);
		return result;
	}
}