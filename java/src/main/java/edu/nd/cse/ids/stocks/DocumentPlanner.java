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
/*	private MultiLayerNetwork model;
	private KerasTokenizer tokenizer;
*/
    private static final String API_KEY = "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";
    private static final String MODEL_NAME = "text-davinci-003";
    private static final double TEMPERATURE = 0;
    private static final int MAX_TOKENS = 64;

    private Map<String, List<String>> categories;
	
	public DocumentPlanner()
	{
		/*try {
            // use the java dl4j library to pull in the model & tokenizer
            String simpleMlp = "qa_g_lstm.h5";
            model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
            tokenizer = KerasTokenizer.fromJson("tokenizer.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

/*        categories = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(questionsFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            String category = parts[0].trim();
            String question = parts[1].trim();
            categories.computeIfAbsent(category, k -> new ArrayList<>()).add(question);
        }
        reader.close();
*/
        messages = new LinkedList<Message>();
	}


/*	public static int[] padcrop(Integer[][] seqp, int seqlen)
    {
        Integer[] seq = seqp[0];
    
        int[] newseq = new int[seqlen];
        int i = 0;
        
        int diff = seq.length - seqlen;
        int itmax = seqlen + diff;
        
        if(diff < 0)
        {
            diff = 0;
            itmax = seq.length;
        }
        
        for (int j=diff; j<itmax; j++)
        {
            if(seq.length < 1)
                newseq[i] = 1;
            else
                newseq[i] = seq[j];
            
            i++;
        }
        
        return(newseq);
    }

*/

    public String classify(String text, String fileContents) throws Exception {
		
		String indication = "I am providing a list of questions and the categories they are in, separated by a colon. Please store this for future use as I will be providing a question for you to classify within these categories. Here is the list: ";

        String classifier = "Now classify this question based on the previous information I provided you: ";

		try{		
			String response = generateResponse(indication + fileContents + classifier + text);
			return response;
		} catch ( Exception e){
			e.printStackTrace();
		
		}
//		String classifier = "Now classify this question based on the previous information I provided you: ";

//		String response = generateResponse(classifier + text);
        /*for (String category : categories.keySet()) {
            List<String> questions = categories.get(category);
            for (String question : questions) {
                    // User input matches a question in this category
                    // Use GPT-3 to generate a response
               String prompt = "Category: " + category + "\nQuestion: " + question;
               }
            }
        }
        // User input does not match any question in the file
        return generateResponse(text);*/
		
		return null;

    }
	
    private String generateResponse(String prompt) throws Exception {

        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + API_KEY);
       // con.setRequestProperty("Connection", "close"); // Add this line to close the connection after each use

        JSONObject data = new JSONObject();
        data.put("model", MODEL_NAME);
        data.put("prompt", prompt);
        data.put("max_tokens", MAX_TOKENS);
        data.put("temperature", TEMPERATURE);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());


        //int responseCode = con.getResponseCode();
        //String responseMessage = con.getResponseMessage();
//        System.out.println("Response code: " + responseCode);
//        System.out.println("Response message: " + responseMessage);

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();
		System.out.println(output);
        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }

	public void answerQuestion(List<StockEntry> stockHistory, String question) {

        // System.out.println(question);

        String questionsFile = "questions.txt";

        Map<String, List<String>> categories = new HashMap<>();
/*        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(questionsFile);
*/
		String fileContents = "";
		try (Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/" + questionsFile))) {
    		fileContents = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
    		e.printStackTrace();
		}

		

        /*try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + questionsFile), "UTF-8"));
      
        //Path path = Paths.get(questionsFile);
      
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String category = parts[0].trim();
                String cat_question = parts[1].trim();
                categories.computeIfAbsent(category, k -> new ArrayList<>()).add(cat_question);
            }
            reader.close();
*/
		System.out.println(fileContents);
		try {
			System.out.println(question);
            String category = classify(question, fileContents);
            System.out.println(category);
        } catch (Exception e){
            e.printStackTrace();
        }
 /*       } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // use the tokenizer to create a vectorized representation of the question
/*        Preprocess preproc = new Preprocess();
        String qformatted = preproc.preprocess(question);
        int seqlen = 200;

        String[] qs = new String[1];
        qs[0] = qformatted;

        Integer[][] seq = this.tokenizer.textsToSequences(qs);
        int newseq[] = padcrop(seq, seqlen);
*/
        // use the model to produce a prediction about what class the question belongs to
/*        INDArray input = Nd4j.create(1, seqlen);

        for(int i = 0; i < seqlen; i++) {
            input.putScalar(new int[] {i}, newseq[i]);
        }

        INDArray output = model.output(input);
*/
        // System.out.println(output);

//        int messageNum = output.argMax(1).getInt();

        // System.out.println(output.argMax());

        // create message list that includes the message types necessary to answer the question
        // message order is from ['numBedroom', 'numBathroom', 'numBeds', 'numGuests', 'itemCount', 'includesList', 'viewType', 'houseLocation', 'travelDistance', 'petsAllowed', 'itemFeatures', 'greeting', 'roomType', 'detailMessage']
        // ['text:topFive', 'text:dividend', 'text:volume', 'chart:candle', 'text:trend', 'text:currPrice', 'text:priceChange', 'text:events', 'text:news', 'text:history']
//        String[] messageList = {"text:topFive", "text:dividend", "text:volume", "chart:candle", "text:trend", "text:currPrice", "text:priceChange", "text:events", "text:news", "text:history"};

        // System.out.println(messageNum);
  //      messageNum = 4;
        // System.exit(0);

 /*       switch(messageList[messageNum]) {
            case "text:trend": 
                TrendMessage m1 = new TrendMessage();
				// TODO: add timeframe for trend
				int period = 5; // 5 last days data
                m1.generate(stockHistory, period);
                this.messages.add(m1);
                break;*/
            // case "numBathroom":
            //     NumBathroomMessage m2 = new NumBathroomMessage();
            //     m2.generate(house);
            //     this.messages.add(m2);
            //     break;
		}

    public List<Message> getMessages()
    {
        return messages;
    }
}
