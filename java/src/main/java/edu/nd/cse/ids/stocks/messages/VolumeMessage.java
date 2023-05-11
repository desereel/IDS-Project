package edu.nd.cse.ids.stocks.messages;

import edu.nd.cse.ids.stocks.*;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class VolumeMessage extends Message
{
    private String date;
    private double volume;

    public VolumeMessage() {}

    public void generate(String ticker, String date) {

		String pythonInterpreter = "python3";
		String pythonScript = "../python/messages/VolumeMessage.py";

		LocalDate today = LocalDate.now();
		LocalDate parsed_date = LocalDate.parse(date);
		long daysBetween = ChronoUnit.DAYS.between(parsed_date, today);		

		List<String> command = new ArrayList<>();
		command.add(pythonInterpreter);
		command.add(pythonScript);
		command.add(ticker);
		command.add(String.valueOf(daysBetween));

		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null){
				setVolume(Double.parseDouble(line));
			}

			reader.close();

		} catch ( Exception e) {
			e.printStackTrace();
	
		}
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}	
	public double getVolume() {
        return this.volume; 
    }

}
