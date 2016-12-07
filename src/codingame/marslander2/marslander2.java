package marslander2;

import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 */
class Player
{

	private static enum TASK
	{
		//additional goals while trying to hit the zone:
		//  vertical speed should not exceed 50
		//  horizontal speed should not exceed 20
		REACHZONE,

		//additional goals while landing:
		//  horizontal speed needs to slow down to 0
		//  vertical speed must not exceed 40
		LAND
	}

	private static final int REACHZONE_HS_TARGET = 40;
	private static final int REACHZONE_VS_TARGET = -30;
	private static final int LAND_HS_TARGET = 15;
	private static final int LAND_VS_TARGET = -30;

	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		int N = in.nextInt(); // the number of points used to draw the surface of Mars.
		int lastY = -1;
		int lastX = -1;
		int zoneLeft = -1;
		int zoneRight = -1;
		int zoneY = 0;

		for (int i = 0; i < N; i++)
		{
			int x = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
			int y = in
					.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.

			if (y == lastY)
			{
				zoneLeft = lastX;
				zoneRight = x;
				zoneY = y;
			}
			lastX = x;
			lastY = y;
		}

		//we will tr to land in the inner 50% of the zone
		int center = (zoneLeft + zoneRight) / 2;
		zoneLeft = (zoneLeft + center) / 2;
		zoneRight = (zoneRight + center) / 2;

		System.err.println("Flat ground between " + zoneLeft + " and " + zoneRight);
		TASK task = TASK.REACHZONE;

		// game loop
		while (true)
		{
			int currentX = in.nextInt();
			int currentY = in.nextInt();
			int currentHS = in.nextInt(); // the horizontal speed (in m/s), can be negative.
			int currentVS = in.nextInt(); // the vertical speed (in m/s), can be negative.
			int fuelLeft = in.nextInt(); // the quantity of remaining fuel in liters.
			int rotationAngle = in.nextInt(); // the rotation angle in degrees (-90 to 90).
			int thrustPower = in.nextInt(); // the thrust power (0 to 4).

			int alpha = 0;
			int thrust = 4;

			if (zoneLeft < currentX && currentX < zoneRight)
			{
				task = TASK.LAND;
			} else
			{
				task = TASK.REACHZONE;
			}

			boolean goingLeft = true;
			if (currentHS > 0)
				goingLeft = false;

			boolean zoneIsLeft = true;
			if (currentX < zoneLeft)
				zoneIsLeft = false;

			int targetHS = 0;
			int targetVS = 0;
			int desiredHSChange = 0;

			if (task == TASK.REACHZONE)
			{

				targetHS = REACHZONE_HS_TARGET;
				if (zoneIsLeft)
					targetHS *= -1;

				targetVS = REACHZONE_VS_TARGET;

				desiredHSChange = targetHS - currentHS;

			} else if (task == TASK.LAND)
			{

				targetHS = LAND_HS_TARGET;

				desiredHSChange = 0;

				if (currentHS > 0 && currentHS > LAND_HS_TARGET)
				{
					desiredHSChange = LAND_HS_TARGET - currentHS;
				} else if (currentHS < 0 && currentHS < (LAND_HS_TARGET * -1))
				{
					desiredHSChange = (LAND_HS_TARGET * -1) - currentHS;
				}

				targetVS = LAND_VS_TARGET;
			}

			System.err.println("HS current " + currentHS + ", HS target " + targetHS);
			String tmp = "reaching zone";
			if (task == TASK.LAND)
				tmp = "landing";
			System.err.println("task: " + tmp + ", HS change " + desiredHSChange);

			alpha = 0;

			if (desiredHSChange > 0 || desiredHSChange < 0)
			{
				alpha = 15;
			}
			if (desiredHSChange > 20 || desiredHSChange < -20)
			{
				alpha = 30;
			}
			if (desiredHSChange > 40 || desiredHSChange < -40)
			{
				alpha = 45;
			}
			if (desiredHSChange > 60 || desiredHSChange < -60)
			{
				alpha = 60;
			}
			if (desiredHSChange > 80 || desiredHSChange < -80)
			{
				alpha = 75;
			}
			if (desiredHSChange > 100 || desiredHSChange < -100)
			{
				alpha = 90;
			}

			if (desiredHSChange > 0)
				alpha *= -1;

			thrust = 3;
			if (currentVS < targetVS)
			{
				thrust = 4;
			}
			if (task == TASK.REACHZONE)
			{
				thrust = 4;
			}

			//last minute adjustments
			//if we will soon hit the floor, we may as well do it at 0 degrees
			if (currentY - zoneY < 120)
			{
				alpha = 0;
			}

			System.out.println(alpha + " " + thrust);
		}
	}

	private static int calcAlpha(int thrust, int verticalSpeed)
	{

		// a/b = sin(alpha) / sin(beta)
		//we have a, b, and beta^
		//a = vertical speed downward
		//b = thrust (usually 4)
		//beta is 90°

		// sin(alpha) = a / (b * sin(beta))

		int alpha = (int) Math.round(Math.asin(verticalSpeed / (thrust * Math.sin(90))));
		return (90 - alpha);
	}
}



