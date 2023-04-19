package edu.nd.cse.ids.stocks.messages;

import edu.nd.cse.ids.stocks.*;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;

public class TrendMessage extends Message
{
	private int period;
	private String trend;
	
	public TrendMessage() {}

	public void generate(List<StockEntry> stockHistory, int period) {
		// setPeriod(period);
		System.out.println("TEST");
		// setTrend();
	}

	// public void setPeriod() {

	// }
	// public void setTrend() {

	// }

	// public void getPeriod() {

	// }
	// public void getTrend() {

	// }
}
