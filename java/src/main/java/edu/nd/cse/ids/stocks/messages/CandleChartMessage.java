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

public class CandleChartMessage extends Message
{

//	private double chart;

    public CandleChartMessage() {}

    public void generate(String ticker) {

        String pythonInterpreter = "python3";
        String pythonScript = "../python/messages/CandleCharts.py";

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
            }

            reader.close();
			process.waitFor();

/*            // check if chart.png file exists
            File chartFile = new File("../python/messages/chart.png");
            if (chartFile.exists()) {
                System.out.println("Chart generated successfully");
            } else {
                System.out.println("Failed to generate chart");
            }
*/

        } catch ( Exception e) {
            e.printStackTrace();

        }
    }

/*    public void setChart( double chart) {
        this.chart = chart;
    }
    public double getChart() {
        return this.chart;
    }
*/
}
