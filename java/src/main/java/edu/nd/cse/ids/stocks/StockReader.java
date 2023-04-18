package edu.nd.cse.ids.stocks;

import com.opencsv.bean.*;

import java.util.List;
import java.util.HashMap;
import java.io.FileReader;

public class StockReader
{
	private List<StockEntry> stocks;

	public StockReader()
	{
		stocks = null;
	}

	public void readStockEntry(String filename)
	{
		try {
			this.stocks = new CsvToBeanBuilder(new FileReader(filename)).withType(StockEntry.class).build().parse();
		} catch (Exception ex)
		{
			System.out.println(ex);
		}
	}

	public List<StockEntry> getStocks()
	{
		return this.stocks;
	}
}
