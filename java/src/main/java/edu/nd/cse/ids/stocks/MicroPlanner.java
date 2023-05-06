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
			if ( message instanceof TrendPromptMessage) {
				SPhraseSpec s1 = handleMessage((TrendPromptMessage) message);
				phrases.add(s1);
			}
			if (message instanceof PromptMessage) {
				SPhraseSpec s1 = handleMessage ((PromptMessage) message);
				phrases.add(s1);
			}
		}
		return phrases;
	}

	public SPhraseSpec handleMessage(TrendMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();
		String trend = message.getTrend();
		String period = message.getPeriod();

        WordElement we_stock = new WordElement("stock", LexicalCategory.NOUN);
        NPPhraseSpec np_stock = nlgFactory.createNounPhrase(we_stock);
        np_stock.setDeterminer("that");

		s1.setSubject(np_stock);
		s1.setVerb("is");
		s1.addComplement("trending");
		s1.addComplement(trend);
		s1.addComplement("this");
		s1.addComplement(period);

		return s1;
	}

	public SPhraseSpec handleMessage(TrendPromptMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();

		s1.setSubject("you");
		s1.addComplement("want");
		s1.addComplement("to");
		s1.addComplement("learn");
		s1.addComplement("more");
		s1.addComplement("about");
		s1.addComplement("the");
		s1.addComplement("tend");
		// s1.setFeature(Feature.TENSE, Tense.FUTURE);
		s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
		return s1;
	}

		public SPhraseSpec handleMessage(PromptMessage message)
	{
        SPhraseSpec s1 = nlgFactory.createClause();

		s1.setSubject("you");
		s1.addComplement("want");
		s1.addComplement("to");
		s1.addComplement("learn");
		s1.addComplement("about");
		s1.addComplement(message.getStock());
		// s1.setFeature(Feature.TENSE, Tense.FUTURE);
		s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
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

}
