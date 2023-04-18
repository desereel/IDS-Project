package edu.nd.cse.ids.stocks;

import edu.nd.cse.ids.stocks.messages.*;

import java.util.List;
import java.util.LinkedList;

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

		this.reader = new StockReader();
		this.reader.readStockEntry(datfile);

		this.docplanner = new DocumentPlanner();

		this.microplanner = new MicroPlanner();
		
		this.realizer = new Realizer();

	}

	public static void main(String[] args)
	{

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
		System.out.println("You asked: " + question);

	}
}
