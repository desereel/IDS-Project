package edu.nd.cse.ids.stocks.tests;

import edu.nd.cse.ids.stocks.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;


public class Stocks_test_1
{
    @Test
    public void testTrends()
    {
        StockNLG stockNLG = new StockNLG("");
        
        List<String> description = rentalNlg.askQuestion(2, 0, "How many bedrooms?");
        
        assertEquals(description.get(0), "This house has 4 bedrooms");
    }


}