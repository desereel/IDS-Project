package edu.nd.cse.ids.stocks.messages;

import edu.nd.cse.ids.stocks.*;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.util.Arrays;

public class VolumeMessage extends Message
{
    private String date;
    private double volume;

    public VolumeMessage() {}

    public void generate(List<StockEntry> stockHistory, String date) {

		for (StockEntry entry : stockHistory){
			if (entry.getDate().equals(date)) {
				setVolume(entry.getVolume());
				break;
			}
		}

		
	}
    public void setDate(String date) {
        this.date = date;
    }
    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getDate() {
        return this.date;
    }
    public double getVolume() {
        return this.volume;
    }
}
