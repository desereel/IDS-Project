package edu.nd.cse.ids.stocks;

import com.opencsv.bean.*;

import java.util.List;
import java.util.HashMap;
import java.io.FileReader;

public class StockReader
{
	private List<StockEntry> stockHistory;

	public StockReader()
	{
		stockHistory = null;
	}

	public void readStockEntry(String filename)
	{
		try {
			this.stockHistory = new CsvToBeanBuilder(new FileReader(filename)).withType(StockEntry.class).build().parse();
		} catch (Exception ex)
		{
			System.out.println(ex);
		}
	}

	public List<StockEntry> getStockHistory()
	{
		return this.stockHistory;
	}
}
