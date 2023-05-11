package edu.nd.cse.ids.stocks;

import edu.nd.cse.ids.stocks.messages.*;

import java.util.List;
import java.util.LinkedList;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class MicroPlanner
{

    private Lexicon lexicon;
    private NLGFactory nlgFactory;


	public MicroPlanner()
	{
		this.lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(this.lexicon);
	}

	public List<SPhraseSpec> lexicalize(List<Message> documentPlan)
	{
        List<SPhraseSpec> phrases = new LinkedList<SPhraseSpec>();
        for (Message message : documentPlan) {
            if (message instanceof TrendMessage) {
                SPhraseSpec s1 = handleMessage((TrendMessage) message);
                phrases.add(s1);
			}
			if (message instanceof VolumeMessage) {
				SPhraseSpec s1 = handleMessage((VolumeMessage) message);
				phrases.add(s1);
			}
			if (message instanceof CurrPriceMessage) {
				SPhraseSpec s1 = handleMessage((CurrPriceMessage) message);
				phrases.add(s1);
			}
			if (message instanceof DividendMessage) {
				SPhraseSpec s1 = handleMessage((DividendMessage) message);
				phrases.add(s1);
			}
			if (message instanceof CandleChartMessage) {
				SPhraseSpec s1 = handleMessage((CandleChartMessage) message);
				phrases.add(s1);
			}
			if (message instanceof NewsMessage) {
				SPhraseSpec s1 = handleMessage((NewsMessage) message);
				phrases.add(s1);
			}
			if (message instanceof EventsMessage) {
				SPhraseSpec s1 = handleMessage((EventsMessage) message);
				phrases.add(s1);
			}
			if (message instanceof PriceChangeMessage) {
				SPhraseSpec s1 = handleMessage((PriceChangeMessage) message);
				phrases.add(s1);
			}
			if ( message instanceof TrendPromptMessage) {
				SPhraseSpec s1 = handleMessage((TrendPromptMessage) message);
				phrases.add(s1);
			}
			if (message instanceof PromptMessage) {
				SPhraseSpec s1 = handleMessage ((PromptMessage) message);
				phrases.add(s1);
			}
			if (message instanceof PriceChangePromptMessage) {
				SPhraseSpec s1 = handleMessage ((PriceChangePromptMessage) message);
				phrases.add(s1);

			}
		}
		return phrases;
	}

	public SPhraseSpec handleMessage(TrendMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();
		String trend = message.getTrend();

        WordElement we_stock = new WordElement("stock", LexicalCategory.NOUN);
        NPPhraseSpec np_stock = nlgFactory.createNounPhrase(we_stock);
        np_stock.setDeterminer("that");

		s1.setSubject(np_stock);
		s1.setVerb("is");
		s1.addComplement("trending");
		s1.addComplement(trend);

		return s1;
	}

	public SPhraseSpec handleMessage(TrendPromptMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();

		s1.setSubject("you");
		s1.setVerb("want");
		s1.addComplement("to learn more about the trend");

		s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		s1.setFeature(Feature.TENSE, Tense.PRESENT);
		
		return s1;
	}

	public SPhraseSpec handleMessage(PriceChangePromptMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();

		s1.setSubject("you");
		s1.setVerb("want");
		s1.addComplement("to learn more about the price change");
		
		s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		s1.setFeature(Feature.TENSE, Tense.PRESENT);
		
		return s1;
	}

	public SPhraseSpec handleMessage(PromptMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();

		s1.setSubject(nlgFactory.createNounPhrase("you"));
		s1.setVerb("want");
		s1.addComplement("to learn about " + message.getStock());
		
		s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		s1.setFeature(Feature.TENSE, Tense.PRESENT);

		return s1;
	}

	public SPhraseSpec handleMessage(VolumeMessage message)
    {

        SPhraseSpec s1 = nlgFactory.createClause();
		NumberFormat nf = new DecimalFormat("##.###");

		s1.setSubject("The volume");
		s1.setVerb("is");
		s1.setObject(nf.format(message.getVolume()));

        return s1;
    }
	public SPhraseSpec handleMessage(CurrPriceMessage message)
	{
		SPhraseSpec s1 = nlgFactory.createClause();
		NumberFormat nf = new DecimalFormat("##.###");
		
		s1.setSubject("The share price");
		s1.setVerb("is");
		s1.setObject(nf.format(message.getPrice()));
	
		return s1;
	}
	public SPhraseSpec handleMessage(DividendMessage message)
	{
		SPhraseSpec s1 = nlgFactory.createClause();
		NumberFormat nf = new DecimalFormat("##.###");
		
		s1.setSubject("The current dividend value");
		s1.setVerb("is");
		s1.setObject(nf.format(message.getDividend()));

		return s1;
	}
	public SPhraseSpec handleMessage(CandleChartMessage message)
	{
	
		//message.getChart();

		SPhraseSpec s1 = nlgFactory.createClause();

		s1.setSubject("The candle chart");
		s1.setVerb("is");

		//s1.setObject(message.getChart());		
		return s1;
	}

	public SPhraseSpec handleMessage(NewsMessage message)
	{

		StringBuilder sb = new StringBuilder();

		for (String article : message.getArticles()){

			sb.append(article).append(System.lineSeparator());
		}

		String result = sb.toString();

		SPhraseSpec s1 = nlgFactory.createClause();
		s1.setSubject("The articles");
		s1.setVerb("are");
		s1.setObject(result);
		
		return s1;
	}

	public SPhraseSpec handleMessage(EventsMessage message)
	{
        StringBuilder sb = new StringBuilder();

        for (String event : message.getEvents()){

            sb.append(event).append(System.lineSeparator());
        }

        String result = sb.toString();

        SPhraseSpec s1 = nlgFactory.createClause();
        s1.setSubject("The events");
        s1.setVerb("are");
        s1.setObject(result);

        return s1;
	}

	public SPhraseSpec handleMessage(PriceChangeMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();
        String change = message.getChange();

		s1.setSubject("The price change");
		s1.setVerb("is");
		s1.setObject(change);

        return s1;

	}	
}
