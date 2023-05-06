package edu.nd.cse.ids.stocks.messages;

import edu.nd.cse.ids.stocks.*;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;

public class TrendMessage extends Message
{
	private String period;
	private String trend;
	
	public TrendMessage() {}

	public void generate(List<StockEntry> stockHistory, int period) {
		// setPeriod(period);

		double end = stockHistory.get(stockHistory.size() - 1).getAdjusted();
		double begin = stockHistory.get(stockHistory.size() - 1 - period).getAdjusted();
		

		if (end - begin > 0) {
			setTrend("upward");
		} else if (end - begin < 0 ){
			setTrend("downward");
		} else {
			setTrend("neutral");
		}
		if(period == 5){
			setPeriod("week");
		} else if (period == 21) {
			setPeriod("month");
		} else if (period == 1) {
			setPeriod("day");
		} else {
			setPeriod("year");
		}
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	public void setTrend(String trend) {
		this.trend = trend;
	}

	public String getPeriod() {
		return this.period;
	}
	public String getTrend() {
		return this.trend; 
	}
}
