package edu.nd.cse.ids.stocks;

import edu.nd.cse.ids.stocks.messages.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Option;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class StockNLG
{
	private StockReader reader;
	private DocumentPlanner docplanner;
	private MicroPlanner microplanner;
	private Realizer realizer;

	public StockNLG(String datfile)
	{
		// Note: This will be changed, final version will not take in a datfile as we do not yet know which stock to look at (thus which file)

		this.reader = new StockReader();
		this.reader.readStockEntry(datfile);

		this.docplanner = new DocumentPlanner();

		this.microplanner = new MicroPlanner();
		
		this.realizer = new Realizer();

	}

	// Note: Will need to be changed to include a "stockIn" input when program detects stock
	public List<String> askQuestion(String question) {
		List<StockEntry> stockHistory = this.reader.getStockHistory();

		// question = "Why am I using a test question?";
		this.docplanner.answerQuestion(stockHistory, question);

        List<Message> documentPlan = this.docplanner.getMessages();

        List<SPhraseSpec> sentences = this.microplanner.lexicalize(documentPlan);

        return(this.realizer.realize(sentences));

	}


	public static void main(String[] args)
	{

		Scanner scanner = new Scanner(System.in);		

		String question = "";
		int qindex = -1;
		for (int i = 0; i < args.length; i++) {
			
			if (args[i].equals("-q")) {
				qindex = i;
			}
		}

		if ( qindex != -1 ){
			for ( int i = qindex + 1; i < args.length; i++){
				question = question + args[i] + " ";
			}
		} else {
			question = String.join(" ", args);
		}
		
		question = question.trim();
		// System.out.println("You asked: " + question);
		// System.out.println("Enter the stock exchange related to your question: ");
		// String stockExchange = scanner.nextLine();

		// System.out.println("Retrieving " + stockExchange + " information...");

		// if ( stockExchange.equalsIgnoreCase("nyse")){

		// 	String fileIn = "../python/data/stock_market_data/nyse/";

		// }

		// decide which stock 
		// TODO

		// create StockNLG
		StockNLG stockNLG = new StockNLG("../data/stock_market_data/sp500/csv/AAPL.csv");
		List<String> answer = stockNLG.askQuestion(question); 

        for(String sentence: answer)
        {
            System.out.println(sentence);
        }
	}
}
