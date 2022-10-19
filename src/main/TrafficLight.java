package main;

import java.io.IOException;

public class TrafficLight
{
	static int tlTimeGreen;
	static int tlTimeYellow;
	static int tlTimeRed;
	static int tlTimeRedYellow;

	enum tlPhases
	{
		AWAITINGINPUT,
		RED,
		REDYELLOW,
		GREEN,
		YELLOW,

	}

	static tlPhases tlState = tlPhases.AWAITINGINPUT;

	public TrafficLight()
	{
		try
		{
			tlTimeGreen = Integer.parseInt(Main.readConfigFile("timeGreen"));
			tlTimeYellow = Integer.parseInt(Main.readConfigFile("timeYellow"));
			tlTimeRed = Integer.parseInt(Main.readConfigFile("timeRed"));
			tlTimeRedYellow = Integer.parseInt(Main.readConfigFile("timeRedYellow"));
		} catch (IOException ignored)
		{
		}
	}

}
