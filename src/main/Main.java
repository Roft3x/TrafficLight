package main;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Main extends TrafficLight
{
	public static void main(String[] args) throws InterruptedException, IOException
	{
		new TrafficLight();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		writeToLogFile("trafficLightLog", "admin", "Admin logged in");

		/*+++trafficlight system+++*/
		while (true)
		{
			carSwitch();
			pedestrianSwitch(reader);
		}
	}

	public static void tlCar(String printTlPhase)
	{
		System.out.print(printTlPhase);
	}

	public static void tlPedestrian(int wait, String printTlPhase) throws InterruptedException
	{
		TimeUnit time = TimeUnit.SECONDS;

		System.out.println(printTlPhase);
		time.sleep(wait);
	}

	public static void carSwitch() throws IOException
	{
		switch (TrafficLight.tlState)
		{
			case AWAITINGINPUT ->
			{
				writeToLogFile("trafficLightLog", "car", "green (awaiting input)");
				System.out.println("Car: Pedestrian: ");
				tlCar("green ");
			}
			case RED ->
			{
				writeToLogFile("trafficLightLog", "car", "red");
				tlCar("green ");
			}
			case REDYELLOW ->
			{
				writeToLogFile("trafficLightLog", "car", "redyellow");
				tlCar("yellow ");
			}
			case GREEN ->
			{
				writeToLogFile("trafficLightLog", "car", "green");
				tlCar("red ");
			}
			case YELLOW ->
			{
				writeToLogFile("trafficLightLog", "car", "yellow");
				tlCar("redyellow ");
			}
			default ->
			{
				writeToLogFile("trafficLightLog", "pedestrian", "error");
				System.out.print("yellow");
			}
		}
	}

	public static void pedestrianSwitch(BufferedReader reader) throws IOException, InterruptedException
	{
		switch (TrafficLight.tlState)
		{
			case AWAITINGINPUT ->
			{
				writeToLogFile("trafficLightLog", "pedestrian", "red (awaiting input)");

				System.out.println("red");
				System.out.println("Do you want to press the button? (y)/(n)");

				String input = reader.readLine();
				repeatOrExitProgram(input);
			}
			case RED ->
			{
				tlPedestrian(TrafficLight.tlTimeRed, "red");
				tlState = TrafficLight.tlPhases.REDYELLOW;

				writeToLogFile("trafficLightLog", "pedestrian", "red");
			}
			case REDYELLOW ->
			{
				tlPedestrian(TrafficLight.tlTimeRedYellow, "redyellow ");
				tlState = TrafficLight.tlPhases.GREEN;

				writeToLogFile("trafficLightLog", "pedestrian", "redyellow");
			}
			case GREEN ->
			{
				tlPedestrian(TrafficLight.tlTimeGreen, "green ");
				tlState = TrafficLight.tlPhases.YELLOW;

				writeToLogFile("trafficLightLog", "pedestrian", "green");
			}
			case YELLOW ->
			{
				tlPedestrian(TrafficLight.tlTimeYellow, "yellow ");
				tlState = TrafficLight.tlPhases.AWAITINGINPUT;

				writeToLogFile("trafficLightLog", "pedestrian", "yellow");
			}
			default ->
			{
				System.out.println("yellow");

				writeToLogFile("trafficLightLog", "pedestrian", "error");
			}
		}
	}


	public static void repeatOrExitProgram(String input) throws IOException
	{
		if (input.equals("y"))
		{
			writeToLogFile("trafficLightLog", "pedestrian", "button pressed");
			tlState = TrafficLight.tlPhases.RED;
		} else if (input.equals("n"))
		{
			System.exit(1);
		} else
			System.out.println("wrong input");
	}

	public static void writeToLogFile(String fileName, String roadUser, String content) throws IOException
	{
		String currentLogFile = "/Users/lhinz/Desktop/Ampel Projekt log/" + fileName + java.time.LocalDate.now() + ".txt";

		File file = new File(currentLogFile);

		file.setWritable(true);

		FileWriter writer = new FileWriter(currentLogFile, true);
		try
		{
			switch (roadUser)
			{
				case "pedestrian" ->
						writer.write(("ped: \t" + content + "\t" + LocalDateTime.now() + System.getProperty("line.separator") + System.getProperty("line.separator")));
				case "car" ->
						writer.write(("car: \t" + content + "\t" + LocalDateTime.now() + System.getProperty("line.separator")));
				case "admin" ->
						writer.write(("admin: \t" + content + "\t" + LocalDateTime.now() + System.getProperty("line.separator") + System.getProperty("line.separator")));
			}
			writer.close();
			file.setWritable(false);

		} catch (IOException ex)
		{
			writeErrorFile(ex + "error: not able to write to logfile");
		}
	}

	public static void writeErrorFile(String error) throws IOException
	{

		String path = "/Users/lhinz/Desktop/Ampel Projekt log/" + "error" + java.time.LocalDate.now() + ".txt";

		FileWriter writer = new FileWriter(path, true);

		new File("/Users/lhinz/Desktop/Ampel Projekt log/error" + LocalDateTime.now() + ".txt");
		writer.write(error + LocalDateTime.now());
	}


	public static String readConfigFile(String property) throws IOException
	{
		File configFile = new File("/Users/lhinz/Desktop/Ampel Projekt log/trafficLightConfig.properties");
		FileReader reader = new FileReader(configFile);
		Properties props = new Properties();
		props.load(reader);

		String output = props.getProperty(property);

		reader.close();
		return output;
	}
}