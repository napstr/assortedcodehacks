package labyrinth;

import java.io.FileNotFoundException;
import java.util.*;

class Player
{

	public static void main(String args[]) throws FileNotFoundException
	{
		Scanner in = new Scanner(System.in);

		int height = in.nextInt(); // number of rows.
		int width = in.nextInt(); // number of columns.
		//add 1 to countdown because our route object always contains start AND endpoint, making it one bigger then needed
		int countdown =
				in.nextInt() + 1; // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.

		boolean escaping = false;
		Map map = new Map();
		List<Koords> route = new ArrayList<>();

		// game loop
		while (true)
		{
			int kirkY = in.nextInt(); // row = Y where Kirk is located.
			int kirkX = in.nextInt(); // column = X where Kirk is located.
			Koords kirk = new Koords(kirkY, kirkX);
			String[] mapData = new String[height];
			System.err.println("Kirk: " + kirk);
			for (int i = 0; i < height; i++)
			{
				mapData[i] = in.next(); // C of the characters in '#.TC?' (i.e. one line of the ASCII maze).
				System.err.println(mapData[i]);
			}

			map.updateMap(mapData);

			if (route.size() < 2)
			{

				//have we reached C? have we found a fast enough escape route? then we should start escaping
				if (!escaping && reachedControl(kirk, map) && escapePossible(map, countdown))
				{
					System.err.println("Reached C");
					escaping = true;
				}

				if (escaping)
				{
					System.err.println("Escaping!");
					//do we already have a route?
					route = shortestRoute(kirk, map.t, map);
				}

				//does a route to C exist and will escaping be possible?
				else if (escapePossible(map, countdown))
				{
					System.err.println("Escape possible, going straight for C");
					route = shortestRoute(kirk, map.c, map);
				}

				//explore!
				else
				{
					System.err.println("Exploring");
					route = explore(kirk, map);
				}
			}

			System.out.println(gimmeDirection(route.get(0), route.get(1))); // Kirk's next move (UP DOWN LEFT or RIGHT).
			route.remove(0);
		}
	}

	private static String gimmeDirection(Koords from, Koords to)
	{
		if (from.x < to.x)
			return "RIGHT";
		else if (from.y < to.y)
			return "DOWN";
		else if (from.x > to.x)
			return "LEFT";
		else if (from.y > to.y)
			return "UP";
		return "OMGWTFBBQ";
	}

	private static boolean reachedControl(Koords kirk, Map map)
	{
		return map.c != null && map.c.equals(kirk);
	}

	private static boolean escapePossible(Map map, int maxLength)
	{
		//c found?
		if (map.c == null)
			return false;

		List<Koords> route = shortestRoute(map.c, map.t, map);

		return route != null && route.size() <= maxLength;
	}

	private static int reveals(Koords k, Map map)
	{
		int result = 0;

		for (int y = k.y - 2; y <= k.y + 2; y++)
		{
			for (int x = k.x - 2; x <= k.x + 2; x++)
			{
				Koords asd = new Koords(y, x);
				if (map.isValidKoords(asd))
				{
					if (map.map[y][x] == Map.Field.UNKNOWN)
						result++;
				}
			}
		}
		return result;
	}

	//a very simple list to remember koords with unrevealed areas which weren't the best choice at the moment but may be used in case
	//kirk gets into a dead end
	private static List<Koords> backTrack = new ArrayList<>();

	private static List<Koords> explore(Koords kirk, Map map)
	{
		List<Koords> possible = map.neighboursForPath(kirk);

		//which one reveals more unknown area?
		int revealed = Integer.MIN_VALUE;
		Koords result = null;
		for (Koords k : possible)
		{
			int r = reveals(k, map);

			if (r > 0 && !backTrack.contains(k))
				backTrack.add(k);

			if (r > revealed)
			{
				revealed = r;
				result = k;
			}
		}

		//if the choices here don't reveal anything check the backtrack for better options
		//pop last one from the backtrack
		if (revealed <= 0)
		{
			System.err.println("Using backtrack which has " + backTrack.size() + " options left");
			if (backTrack.size() == 0)
			{
				System.err.println("populating backtrack");
				populateBacktrack(kirk, map);
			}

			result = popFromBacktrack(kirk, map);
			return shortestRoute(kirk, result, map);
		}

		backTrack.remove(result);

		List<Koords> route = new ArrayList<>();
		route.add(kirk);
		route.add(result);
		return route;
	}

	//get the closest? or the one that reveals the most?
	private static Koords popFromBacktrack(Koords kirk, Map map)
	{
		Koords result = backTrack.get(backTrack.size() - 1);
		int distance = shortestRoute(kirk, result, map).size();

		for (Koords k : backTrack)
		{
			int asd = shortestRoute(kirk, k, map).size();
			if (asd < distance)
			{
				result = k;
				distance = asd;
			}
		}
		backTrack.remove(result);
		return result;
	}

	//puts reachable koords that reveal parts of the map into the backtrack
	private static void populateBacktrack(Koords kirk, Map map)
	{
		for (int y = 0; y < map.height; y++)
		{
			for (int x = 0; x < map.width; x++)
			{
				Map.Field f = map.map[y][x];
				if (f == Map.Field.WALL || f == Map.Field.UNKNOWN)
					continue;
				Koords k = new Koords(y, x);
				System.err.println("Checking " + k);
				if (shortestRoute(kirk, k, map) == null)
					continue;

				if (reveals(k, map) > 0)
					backTrack.add(k);
			}
		}

		System.err.println("Backtrack populated with " + backTrack.size() + " options");
	}

	//A* algorithm
	//returns null if there is no route
	//source: https://de.wikipedia.org/wiki/A*-Algorithmus
	private static List<Koords> shortestRoute(Koords st, Koords ta, Map map)
	{

		//swap them so the route is in a correct order later
		Koords start = ta;
		Koords target = st;

		PriorityQueue<Node> openList = new PriorityQueue<>();
		Set<Node> closedList = new HashSet<>();
		openList.add(new Node(start, 0));

		while (openList.size() > 0)
		{
			Node current = openList.poll();

			if (current.koords.equals(target))
			{
				List<Koords> result = new ArrayList<>();
				result.add(current.koords);
				Node previous = current.predecessor;
				//				System.err.println("Route found");
				while (previous != null)
				{
					result.add(previous.koords);
					//					System.err.println(previous.koords);
					previous = previous.predecessor;
				}

				return result;
			}

			closedList.add(current);

			expandNode(current, closedList, openList, target, map);
		}
		return null;
	}

	//part of the A* algorithm
	//source: https://de.wikipedia.org/wiki/A*-Algorithmus
	private static void expandNode(Node current, Set<Node> closedList, PriorityQueue<Node> openList, Koords target, Map map)
	{
		List<Koords> successors = map.neighboursForPath(current.koords);

		for (Koords successor : successors)
		{
			Node next = new Node(successor);
			next.predecessor = current;

			if (closedList.contains(next))
				continue;

			int costs = costs(current) + 1;

			if (openList.contains(next))
			{

				Node nextFromList = null;
				for (Node n : openList)
				{
					if (n.equals(next))
					{
						nextFromList = n;
						break;
					}
				}
				if (costs >= costs(nextFromList))
					continue;

			}

			next.predecessor = current;
			double f = costs + linearDistance(target, next.koords);

			openList.remove(next);
			next.priority = f;
			openList.add(next);
		}
	}

	//Part of the A* algorithm
	//exactly: h(x)
	private static double linearDistance(Koords a, Koords b)
	{
		int difY = a.y - b.y;
		int difX = a.x - b.x;

		difY *= difY;
		difX *= difX;

		return Math.sqrt(0.0 + difY + difX);
	}

	//Part of the A* algorithm
	//calculates the costs of a path which on this map are 1 for every node it goes through
	private static int costs(Node n)
	{
		int result = 0;

		Node tmp = n.predecessor;
		while (tmp != null)
		{
			tmp = tmp.predecessor;
			result++;
		}

		return result;
	}
}

//Object used in the A* algorithm
class Node implements Comparable<Node>
{
	Koords koords;
	double priority;
	Node predecessor;

	public Node(Koords k)
	{
		koords = k;
	}

	public Node(Koords k, double p)
	{
		koords = k;
		priority = p;
	}

	@Override public boolean equals(Object o)
	{
		Node n = (Node) o;
		return this.koords.equals(n.koords);
	}

	@Override public int compareTo(Node o)
	{
		return (int) Math.round(priority - o.priority);
	}

	@Override public int hashCode()
	{
		return koords.hashCode();
	}
}

class Map
{
	int height;
	int width;

	enum Field
	{
		T, C, WALL, EMPTY, UNKNOWN
	}

	Koords c = null;
	Koords t = null;

	Field[][] map;

	public boolean isValidKoords(Koords k)
	{
		return !(k.y < 0 || k.y >= height || k.x < 0 || k.x >= width);
	}

	public void updateMap(String[] data)
	{
		height = data.length;
		width = data[0].length();
		map = new Field[height][];
		for (int y = 0; y < height; y++)
		{
			map[y] = new Field[width];
			int x = 0;
			for (char c : data[y].toCharArray())
			{
				switch (c)
				{
				case 'T':
					map[y][x] = Field.T;
					this.t = new Koords(y, x);
					break;
				case 'C':
					map[y][x] = Field.C;
					this.c = new Koords(y, x);
					break;
				case '?':
					map[y][x] = Field.UNKNOWN;
					break;
				case '#':
					map[y][x] = Field.WALL;
					break;
				case '.':
					map[y][x] = Field.EMPTY;
					break;
				default:
					System.err.println("OMGWTFBBQ");
				}

				x++;
			}
		}
	}

	public List<Koords> neighboursForPath(Koords k)
	{
		List<Koords> result = new ArrayList<>();

		if (k.y > 0)
			addIfWalkable(new Koords(k.y - 1, k.x), result);

		if (k.y < height - 1)
			addIfWalkable(new Koords(k.y + 1, k.x), result);

		if (k.x > 0)
			addIfWalkable(new Koords(k.y, k.x - 1), result);

		if (k.x < width - 1)
			addIfWalkable(new Koords(k.y, k.x + 1), result);

		return result;
	}

	private void addIfWalkable(Koords k, List<Koords> result)
	{
		Field field = map[k.y][k.x];
		if (field == Field.T || field == Field.C || field == Field.EMPTY)
			result.add(k);
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