package edu.nd.cse.ids.stocks;

import java.util.List;
import java.util.LinkedList;

import com.github.houbb.data.struct.core.util.list.ArrayList;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class Realizer
{
	private Lexicon lexicon;
	private Realiser realizer;
	
	public Realizer()
	{
		lexicon = Lexicon.getDefaultLexicon();
		realizer = new Realiser(lexicon);
	}

	public List<String> realize(List<SPhraseSpec> sentences)
	{

		List<String> outputs = new ArrayList();
		for (SPhraseSpec sentence : sentences) {
			String output = this.realizer.realiseSentence(sentence);
			outputs.add(output);
		}

		return outputs;
	}
}
