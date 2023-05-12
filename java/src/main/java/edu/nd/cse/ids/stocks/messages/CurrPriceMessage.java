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

public class CurrPriceMessage extends Message
{
    private double price;

    public CurrPriceMessage() {}

    public void generate(String ticker) {

        String pythonInterpreter = "python3";
        String pythonScript = "../python/messages/CurrPriceMessage.py";

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
				System.out.println(line);
                setPrice(Double.parseDouble(line));
            }

            reader.close();

        } catch ( Exception e) {
            e.printStackTrace();
   
        }
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return this.price;
    }

}

