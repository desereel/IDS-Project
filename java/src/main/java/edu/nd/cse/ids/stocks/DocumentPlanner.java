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
    final String[] basicMessages = {"text:topFive", "text:dividend", "text:volume", "chart:candle", "text:trend", "text:currPrice", "text:priceChange", "text:events", "text:news", "text:history"};
    final String[] modifyMessages = {"modify:week", "modify:month", "modify:year", "modify:day", "modify:done"};
    private List<Message> messages;
    private MultiLayerNetwork model;
    private KerasTokenizer tokenizer;
    private String [] messageList = basicMessages;
    private String ticker;
    private Message lastMessage;
    private String stock; 
	
	public DocumentPlanner()
	{
        try {
            // use the java dl4j library to pull in the model & tokenizer
            String simpleMlp = "../data/models/basicModel.h5";
            model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
            tokenizer = KerasTokenizer.fromJson("../data/models/basicTok.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        messages = new LinkedList<Message>();
    //    this.stock = stock;
	}

    public void switchModel(String modelName, String tokName, String[] messageList){
        try {
            // use the java dl4j library to pull in the model & tokenizer
            this.model = KerasModelImport.importKerasSequentialModelAndWeights("../data/models/" + modelName);
            this.tokenizer = KerasTokenizer.fromJson("../data/models/" + tokName);
            this.messageList = messageList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearMessages(){
        messages = new LinkedList<Message>();
    }

   public static int[] padcrop(Integer[][] seqp, int seqlen)
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

    public void promptQuestion(){
        PromptMessage m0 = new PromptMessage();
        m0.generate(this.stock);
        this.messages.add(m0);
        return;
    }

	public void answerQuestion(String question, String ticker, String date) {
        // use the tokenizer to create a vectorized representation of the question
        Preprocess preproc = new Preprocess();
        String rformatted = preproc.preprocess(question);
        int seqlen = 200;

        String[] qs = new String[1];
        qs[0] = rformatted;

        Integer[][] seq = this.tokenizer.textsToSequences(qs);
        int newseq[] = padcrop(seq, seqlen);

        // use the model to produce a prediction about what class the question belongs to
        INDArray input = Nd4j.create(1, seqlen);

        for(int i = 0; i < seqlen; i++) {
            input.putScalar(new int[] {i}, newseq[i]);
        }

        // System.out.println(input);
        INDArray output = model.output(input);

        int messageNum = output.argMax(1).getInt();

        // create message list that includes the message types necessary to answer the question
        // message order is from ['numBedroom', 'numBathroom', 'numBeds', 'numGuests', 'itemCount', 'includesList', 'viewType', 'houseLocation', 'travelDistance', 'petsAllowed', 'itemFeatures', 'greeting', 'roomType', 'detailMessage']

        // String[] messageList = {"text:topFive", "text:dividend", "text:volume", "chart:candle", "text:trend", "text:currPrice", "text:priceChange", "text:events", "text:news", "text:history"};

        // System.out.println(question);
        // System.out.println(messageList[messageNum]);

        switch(messageList[messageNum]) {
            case "text:trend":
                TrendMessage m1 = new TrendMessage();
                m1.generate(ticker);
                this.messages.add(m1);
                switchModel("modifyModel.h5", "modifyTok.json",modifyMessages);
                TrendPromptMessage m2 = new TrendPromptMessage();
                m2.generate();
                this.messages.add(m2);
                this.lastMessage = m1;
                break;
            case "modify:week":
                if(this.lastMessage instanceof TrendMessage) {
                    ((TrendMessage) this.lastMessage).generate(ticker);
                    this.messages.add((TrendMessage) this.lastMessage);
                    TrendPromptMessage m3 = new TrendPromptMessage();
                    m3.generate();
                    this.messages.add(m3);
                }
                if(this.lastMessage instanceof PriceChangeMessage) {
                    ((PriceChangeMessage) this.lastMessage).generate(ticker);
                    this.messages.add((PriceChangeMessage) this.lastMessage);
                    PriceChangePromptMessage m11 = new PriceChangePromptMessage();
                    m11.generate();
                    this.messages.add(m11);
                }
                break;
            case "modify:month":
                if(this.lastMessage instanceof TrendMessage) {
                    ((TrendMessage) this.lastMessage).generate(ticker);
                    this.messages.add((TrendMessage) this.lastMessage);
                    TrendPromptMessage m3 = new TrendPromptMessage();
                    m3.generate();
                    this.messages.add(m3);
                }
                if(this.lastMessage instanceof PriceChangeMessage) {
                    ((PriceChangeMessage) this.lastMessage).generate(ticker);
                    this.messages.add((PriceChangeMessage) this.lastMessage);
                    PriceChangePromptMessage m10 = new PriceChangePromptMessage();
                    m10.generate();
                    this.messages.add(m10);
                }
                break;
            case "modify:day":
                if(this.lastMessage instanceof TrendMessage) {
                    ((TrendMessage) this.lastMessage).generate(ticker);
                    this.messages.add((TrendMessage) this.lastMessage);
                    TrendPromptMessage m4 = new TrendPromptMessage();
                    m4.generate();
                    this.messages.add(m4);
                }
                if(this.lastMessage instanceof PriceChangeMessage) {
                    ((PriceChangeMessage) this.lastMessage).generate(ticker);
                    this.messages.add((PriceChangeMessage) this.lastMessage);
                    PriceChangePromptMessage m9 = new PriceChangePromptMessage();
                    m9.generate();
                    this.messages.add(m9);
                }
                break;
            case "modify:year":
                if(this.lastMessage instanceof TrendMessage) {
                    ((TrendMessage) this.lastMessage).generate(ticker);
                    this.messages.add((TrendMessage) this.lastMessage);
                    TrendPromptMessage m5 = new TrendPromptMessage();
                    m5.generate();
                    this.messages.add(m5);
                }
                if(this.lastMessage instanceof PriceChangeMessage) {
                    ((PriceChangeMessage) this.lastMessage).generate(ticker);
                    this.messages.add((PriceChangeMessage) this.lastMessage);
                    PriceChangePromptMessage m8 = new PriceChangePromptMessage();
                    m8.generate();
                    this.messages.add(m8);
                }
                break;
            case "modify:done":
                switchModel("basicModel.h5", "basicTok.json",basicMessages);
                promptQuestion();
                break;
            case "text:priceChange":
                PriceChangeMessage m6 = new PriceChangeMessage();
                m6.generate(ticker);
                this.messages.add(m6);
                this.lastMessage = m6;
                switchModel("modifyModel.h5", "modifyTok.json",modifyMessages);
                PriceChangePromptMessage m7 = new PriceChangePromptMessage();
                m7.generate();
                this.messages.add(m7);
                break;
        }
        return;
    }


	// public void answerQuestion(List<StockEntry> stockHistory, String question) {

    //     classifyResponse(question);
    //     // Map<String, List<String>> categories = new HashMap<>();
		
	// 	// String fileContents = "";
	// 	// try (Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/" + questionsFile))) {
    // 	// 	fileContents = scanner.useDelimiter("\\A").next();
	// 	// } catch (Exception e) {
    // 	// 	e.printStackTrace();
	// 	// }

    //     // String output = "";

	// 	// try {
    //     //     output = classify(question, fileContents);
    //     // } catch (Exception e){
    //     //     e.printStackTrace();
    //     // }

	// 	// // String category = output.substring(output.indexOf("text"));
	// 	// String category = output;

	// 	// if(category.contains("text:history")){
	// 	// 	VolumeMessage m1 = new VolumeMessage();
	// 	// 	String date = "12-12-1980";
	// 	// 	m1.generate(stockHistory, date);
	// 	// 	this.messages.add(m1);
	// 	// }
    //     // if(category.contains("text:trend")) {
    //     //     TrendMessage m1 = new TrendMessage();
    //     //     int period = 5; // must pull from prompts
    //     //     m1.generate(stockHistory, period);
    //     //     this.messages.add(m1);
    //     //     // prep for next question
    //     //     this.questionsFile = "trendQuestions.txt";
    //     // }
    //     // if(category.contains("trend:week")){
    //     //     TrendMessage m1 = new TrendMessage();
    //     //     int period = 5; // average number of trading days in a week
    //     //     m1.generate(stockHistory, period);
    //     //     this.messages.add(m1);
    //     //     // prep for next question
    //     //     this.questionsFile = "trendQuestions.txt";
    //     // }
    //     // if(category.contains("trend:month")){
    //     //     TrendMessage m1 = new TrendMessage();
    //     //     int period = 21; // average number of trading days in a month
    //     //     m1.generate(stockHistory, period);
    //     //     this.messages.add(m1);
    //     //     // prep for next question
    //     //     this.questionsFile = "trendQuestions.txt";
    //     // }


    // }

    public List<Message> getMessages()
    {
        return messages;
    }
}
