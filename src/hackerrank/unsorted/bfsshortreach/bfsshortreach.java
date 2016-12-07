package hackerrank.unsorted.bfsshortreach;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Solution
{
	public static void main(String[] args)
	{
		Scanner in;
		try
		{
			in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\test"));
		} catch (FileNotFoundException e)
		{
			in = new Scanner(System.in);
		}
		int cases = in.nextInt();

		for (int i = 0; i < cases; i++)
		{
			Map<Integer, Node> nodes = new HashMap<>();
			int n = in.nextInt();
			for (int a = 1; a <= n; a++)
				nodes.put(a, new Node(a));
			int edges = in.nextInt();

			for (int m = 0; m < edges; m++)
			{
				int n1 = in.nextInt();
				int n2 = in.nextInt();

				nodes.get(n1).connected.add(nodes.get(n2));
				nodes.get(n2).connected.add(nodes.get(n1));
			}
			int start = in.nextInt();

			start(start, nodes);

			String result = "";
			for (int b = 1; b <= n; b++)
				if (b != start)
					result += " " + nodes.get(b).distance;
			result = result.substring(1);
			System.out.println(result);
		}
	}

	private static void start(int start, Map<Integer, Node> nodes)
	{

		List<Node> stack = new ArrayList<>();

		nodes.get(start).distance = 0;
		stack.add(nodes.get(start));

		while (stack.size() > 0)
		{
			Node node = stack.remove(0);

			for (Node n : node.connected)
			{
				if (n.distance == -1 || n.distance > node.distance + 6)
				{
					n.distance = node.distance + 6;
					stack.add(n);
				}
			}
		}
	}
}

class Node
{
	int distance = -1;
	int id = -1;
	List<Node> connected = new ArrayList<Node>();

	public Node(int id)
	{
		this.id = id;
	}

}
