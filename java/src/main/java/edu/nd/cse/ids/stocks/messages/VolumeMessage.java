package edu.nd.cse.ids.stocks.messages;

import edu.nd.cse.ids.stocks.*;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;

public class VolumeMessage extends Message
{
    private String period;
    private int volume;

    public VolumeMessage() {}

    public void generate(List<StockEntry> stockHistory, int period) {

		// for (StockEntry entry : stockHistory){
		// 	if (entry.getDate().equals(date)) {
		// 		setVolume(entry.getVolume());
		// 		break;
		// 	}
		// }
        for (int i = 0; i < period; i ++){
		    this.volume += stockHistory.get(stockHistory.size() - 1 - period).getVolume();
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

    public String getPeriod() {
        return this.period;
    }
    public int getVolume() {
        return this.volume;
    }
}
