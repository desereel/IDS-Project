package edu.nd.cse.ids.stocks;

import edu.nd.cse.ids.stocks.messages.*;

import edu.nd.cse.ids.stocks.Preprocess;
import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;
import java.io.*;
import org.deeplearning4j.nn.modelimport.keras.preprocessing.text.KerasTokenizer;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LossLayer;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class DocumentPlanner
{

	private List<Message> messages;
    private static final String API_KEY = "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";
    private static final String MODEL_NAME = "text-davinci-003";
    private static final double TEMPERATURE = 0;
    private static final int MAX_TOKENS = 64;

    private Map<String, List<String>> categories;
	
	public DocumentPlanner()
	{
        messages = new LinkedList<Message>();
	}


    public String classify(String text, String fileContents) throws Exception {
		
		String indication = "I am providing a list of questions and the categories they are in, separated by a colon. Please store this for future use as I will be providing a question for you to classify within these categories. Here is the list: ";

        String classifier = "Now classify this question based on the previous information I provided you: ";

		try{		
			String response = generateResponse(indication + fileContents + classifier + text);
			return response;
		} catch ( Exception e){
			e.printStackTrace();
		
		}
		
		return null;

    }
	
    private String generateResponse(String prompt) throws Exception {

        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + API_KEY);

        JSONObject data = new JSONObject();
        data.put("model", MODEL_NAME);
        data.put("prompt", prompt);
        data.put("max_tokens", MAX_TOKENS);
        data.put("temperature", TEMPERATURE);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());


        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();
		System.out.println(output);
        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }

	public void answerQuestion(List<StockEntry> stockHistory, String question) {

        String questionsFile = "questions.txt";

		String fileContents = "";
		try (Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/" + questionsFile))) {
    		fileContents = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
    		e.printStackTrace();
		}	

		try {
            String output = classify(question, fileContents).trim();

			String category = output.substring(output.indexOf("text"));
        	
			// create message list that includes the message types necessary to answer the question
			List<String> messageList = new ArrayList<String>();

			messageList.addAll(Arrays.asList("text:topFive", "text:dividend", "text:volume", "chart:candle", "text:trend", "text:currPrice", "text:priceChange", "text:events", "text:news", "text:history"));

			int index = messageList.indexOf(category);
			
			try {
            	switch(index) {

                	case 4:
						System.out.println("hello");
                    	TrendMessage m1 = new TrendMessage();
                		// TODO: add timeframe for trend
                    	int period = 5;
                    	m1.generate(stockHistory, period);
                    	this.messages.add(m1);
                    	break;

           		}
			} catch (Exception e){
				e.printStackTrace();
			}

        } catch (Exception e){
            e.printStackTrace();
        }
	}
    public List<Message> getMessages()
    {
        return messages;
    }
}
