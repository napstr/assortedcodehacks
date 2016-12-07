package hackerrank.algorithms.strings.twotwo;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
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
			in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\twotwo\\test"));
		} catch (FileNotFoundException e)
		{
			in = new Scanner(System.in);
		}
		int t = in.nextInt();

		String[] db = new String[801];

		Node root = new Node();

		BigInteger two = BigInteger.valueOf(1);
		for (int i = 0; i < 801; i++)
		{
			db[i] = two.toString();

			Node n = root;
			for (char c : db[i].toCharArray())
			{
				int a = Integer.parseInt("" + c);
				if (n.next[a] == null)
					n.next[a] = new Node();
				n = n.next[a];
			}
			n.endpoint = true;

			two = two.multiply(BigInteger.valueOf(2));
		}

		for (int i = 0; i < t; i++)
		{
			int result = 0;
			String line = in.next();
			List<Node> nodes = new ArrayList<>();

			for (char c : line.toCharArray())
			{
				int a = Integer.parseInt("" + c);
				nodes.add(root);
				List<Node> tmp = new ArrayList<>();

				for (Node n : nodes)
				{
					if (n.next[a] != null)
					{
						if (n.next[a].endpoint)
							result++;
						tmp.add(n.next[a]);
					}
				}

				nodes = tmp;
			}
			System.out.println(result);
		}
	}
}

class Node
{
	Node[] next;
	boolean endpoint = false;

	public Node()
	{
		next = new Node[10];
	}
}