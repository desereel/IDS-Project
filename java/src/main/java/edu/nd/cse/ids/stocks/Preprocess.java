// code copied directly from class examples - i did not write myself

package edu.nd.cse.ids.stocks;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Preprocess {

    protected StanfordCoreNLP pipeline;

    public Preprocess() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        /*
         * This is a pipeline that takes in a string and returns various analyzed linguistic forms. 
         * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator), 
         * and then other sequence model style annotation can be used to add things like lemmas, 
         * POS tags, and named entities. These are returned as a list of CoreLabels. 
         * Other analysis components build and store parse trees, dependency graphs, etc. 
         * 
         * This class is designed to apply multiple Annotators to an Annotation. 
         * The idea is that you first build up the pipeline by adding Annotators, 
         * and then you take the objects you wish to annotate and pass them in and 
         * get in return a fully annotated object.
         * 
         *  StanfordCoreNLP loads a lot of models, so you probably
         *  only want to do this once per execution
         */
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String> lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }

    public String preprocess(String text)
    {
        text = text.toLowerCase();
        text = text.replaceAll("^\"|\"$", "");
        text = String.join(" ", lemmatize(text));
        return text;
    }

    public static void main(String[] args) {              
        
        //String text = "\"4 Bed / 4.5 Bath in Bushwick, Brooklyn. All Pets Allowed. Newly constructed 4 bedroom / 4.5 bathroom apartment featuring high ceilings, hardwood floors, large windows and ample closet space. Kitchens are equipped with stainless steel appliances including a dishwasher, refrigerator, microwave and stove. Amenities include PRIVATE YARD, central ac/heating.\"";
        
        Preprocess slem = new Preprocess();
        
        BufferedReader reader;
        BufferedWriter writer;
        
        try {
            reader = new BufferedReader(new FileReader("data/descr-pets-no-noid.csv"));
            writer = new BufferedWriter(new FileWriter("data/descr-pets-no-noid-pp.csv"));
            
            String text = reader.readLine();
            
            while(text != null)
            {
                text = slem.preprocess(text);
                writer.write(text);
                writer.newLine();
                text = reader.readLine();
            }

            reader.close();
            writer.close();
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

}
