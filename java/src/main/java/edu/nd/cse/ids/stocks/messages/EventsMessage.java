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

public class EventsMessage extends Message
{
    private List<String> events;

    public EventsMessage() {

        this.events = new LinkedList<String>();

    }

    public void generate(String ticker) {

        String pythonInterpreter = "python3";
        String pythonScript = "../python/messages/Earnings.py";

        List<String> command = new ArrayList<>();
        command.add(pythonInterpreter);
        command.add(pythonScript);
        command.add(ticker);

        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null){
                setEvents(line);
            }

            reader.close();

        } catch ( Exception e) {
            e.printStackTrace();

        }
    }

    public void setEvents(String event) {
        this.events.add(event);
    }
    public List<String> getEvents() {
        return this.events;
    }

}

