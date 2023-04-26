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
		setPeriod(period);

		double end = stockHistory.get(stockHistory.size() - 1).getAdjusted();
		double begin = stockHistory.get(stockHistory.size() - 1 - period).getAdjusted();

		if (end - begin > 0) {
			setTrend("upward");
		} else if (end - begin < 0 ){
			setTrend("downward");
		} else {
			setTrend("neutral");
		}
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	public void setTrend(String trend) {
		this.trend = trend;
	}

	public int getPeriod() {
		return this.period;
	}
	public String getTrend() {
		return this.trend; 
	}
}
