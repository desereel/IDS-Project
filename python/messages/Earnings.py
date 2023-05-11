import yfinance as yf
import sys

# Get the ticker symbol from the command line arguments
ticker = sys.argv[1]

# Get the stock data for the ticker
stock_data = yf.Ticker(ticker)

ES = stock_data.get_earnings_dates()
print(ES)
