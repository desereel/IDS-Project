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
	private DocumentPlanner docplanner;
	private MicroPlanner microplanner;
	private Realizer realizer;

	public StockNLG()
	{

		this.docplanner = new DocumentPlanner();

		this.microplanner = new MicroPlanner();
		
		this.realizer = new Realizer();

	}

	public List<String> askQuestion(String question, String ticker) {

		this.docplanner.clearMessages();

		this.docplanner.answerQuestion(question, ticker);

        List<Message> documentPlan = this.docplanner.getMessages();

        List<SPhraseSpec> sentences = this.microplanner.lexicalize(documentPlan);

        return(this.realizer.realize(sentences));

	}

	public List<String> promptQuestion(String ticker) {
		this.docplanner.clearMessages();
		this.docplanner.promptQuestion(ticker);
		List<Message> documentPlan = this.docplanner.getMessages();
		List<SPhraseSpec> sentences = this.microplanner.lexicalize(documentPlan);
        return(this.realizer.realize(sentences));

	}


	public static void main(String[] args)
	{

		Scanner scanner = new Scanner(System.in);		

		String question = "";
		String ticker = "";
		System.out.println("What is the ticker for the stock you would like to look at today?");
		ticker = scanner.nextLine().trim().toUpperCase();

		StockNLG stockNLG = new StockNLG();
		List<String> answer = stockNLG.promptQuestion(ticker);

		for(String sentence: answer)
		{
			System.out.println(sentence);
		}

		while (true) {

			question = scanner.nextLine().trim();
			if(question.contains("quit")){
				return;
			}

			answer = stockNLG.askQuestion(question, ticker);
			for(String sentence: answer)
			{
				System.out.println(sentence);
			}
		}
	}
}
