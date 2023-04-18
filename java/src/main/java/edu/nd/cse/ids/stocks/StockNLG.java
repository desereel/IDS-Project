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

	public StockNLG()
	{
	}

	public static void main(String[] args)
	{

	}
}
