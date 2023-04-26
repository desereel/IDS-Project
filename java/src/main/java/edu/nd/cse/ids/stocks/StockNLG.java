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

		this.docplanner.clearMessages();

		// question = "Why am I using a test question?";
		this.docplanner.answerQuestion(stockHistory, question);

        List<Message> documentPlan = this.docplanner.getMessages();

        List<SPhraseSpec> sentences = this.microplanner.lexicalize(documentPlan);

        return(this.realizer.realize(sentences));

	}



	public static void main(String[] args)
	{

		Scanner scanner = new Scanner(System.in);		

		// String question = "What is the 5 day trend of Bitcoin's share price";
		String question = "";
		String ticker = "";
		System.out.println("What is the ticker for the stock you would like to look at today?");
		ticker = scanner.nextLine().trim().toUpperCase();
		// System.out.println(ticker);
		StockNLG stockNLG = new StockNLG("../data/stock_market_data/all/" + ticker + ".csv");
		System.out.println("What would you like to learn about that stock?");
		while (true) {
			question = scanner.nextLine().trim();
			if(question.contains("quit")){
				return;
			}
			List<String> answer = stockNLG.askQuestion(question); 
			for(String sentence: answer)
			{
				System.out.println(sentence);
			}
		}
	}
}
