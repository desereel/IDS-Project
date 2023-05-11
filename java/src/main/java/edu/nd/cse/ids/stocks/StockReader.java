package edu.nd.cse.ids.stocks;

import com.opencsv.bean.*;

import java.util.List;
import java.util.HashMap;
import java.io.FileReader;

/*public class StockReader
{
	private String stock;

	public StockReader()
	{
		stock = null;
	}

	public void readStockEntry(String ticker)
	{
		try {
			this.stockHistory = new CsvToBeanBuilder(new FileReader(filename)).withType(StockEntry.class).build().parse();
		} catch (Exception ex)
		{
			System.out.println(ex);
		}

		try {
			this.stock = YahooFinance.get("AAPL");
		} catch (Exception ex){
			System.out.println(ex);
		}

	}

	public String getStock()
	{
		return this.stock;
	}
}*/
