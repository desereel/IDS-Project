package edu.nd.cse.ids.stocks;

import edu.nd.cse.ids.stocks.messages.*;

import edu.nd.cse.ids.stocks.Preprocess;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;

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

public class DocumentPlanner
{

	private List<Message> messages;
	private MultiLayerNetwork model;
	private KerasTokenizer tokenizer;
	
	public DocumentPlanner()
	{
		try {
            // use the java dl4j library to pull in the model & tokenizer
            String simpleMlp = "qa_g_lstm.h5";
            model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);
            tokenizer = KerasTokenizer.fromJson("tokenizer.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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


	


	public void answerQuestion(List<StockEntry> stockHistory, String question) {
        // use the tokenizer to create a vectorized representation of the question
        Preprocess preproc = new Preprocess();
        String qformatted = preproc.preprocess(question);
        int seqlen = 200;

        String[] qs = new String[1];
        qs[0] = qformatted;

        Integer[][] seq = this.tokenizer.textsToSequences(qs);
        int newseq[] = padcrop(seq, seqlen);

        // use the model to produce a prediction about what class the question belongs to
        INDArray input = Nd4j.create(1, seqlen);

        for(int i = 0; i < seqlen; i++) {
            input.putScalar(new int[] {i}, newseq[i]);
        }

        INDArray output = model.output(input);

        int messageNum = output.argMax(1).getInt();

        // create message list that includes the message types necessary to answer the question
        // message order is from ['numBedroom', 'numBathroom', 'numBeds', 'numGuests', 'itemCount', 'includesList', 'viewType', 'houseLocation', 'travelDistance', 'petsAllowed', 'itemFeatures', 'greeting', 'roomType', 'detailMessage']

        String[] messageList = {"trendMessage", "numBathroom", "numBeds", "numGuests", "itemCount", "includesList", "viewType", "houseLocation", "travelDistance", "petsAllowed", "itemFeatures", "greeting", "roomType", "detailMessage"};

        switch(messageList[messageNum]) {
            case "trendMessage": 
                TrendMessage m1 = new TrendMessage();
				// TODO: add timeframe for trend
				int period = 5; // 5 last days data
                m1.generate(stockHistory, period);
                this.messages.add(m1);
                break;
            // case "numBathroom":
            //     NumBathroomMessage m2 = new NumBathroomMessage();
            //     m2.generate(house);
            //     this.messages.add(m2);
            //     break;
		}
    }


    public List<Message> getMessages()
    {
        return messages;
    }
}
