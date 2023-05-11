package edu.nd.cse.ids.stocks.messages;

import edu.nd.cse.ids.stocks.*;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;

public class PromptMessage extends Message
{
    private String stock; 
	public PromptMessage() {}

	public void generate(String stock) {
		System.out.println(stock);
        this.stock = stock;
	}

   	public String getStock() {
		return this.stock; 
	} 

}
