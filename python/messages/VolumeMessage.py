import yfinance as yf
import sys

# Get the ticker symbol and date from the command line arguments
ticker = sys.argv[1]
date = sys.argv[2]

# Get the stock data for the ticker
stock_data = yf.Ticker(ticker)

# Get the volume data for the specified date
volume = stock_data.history(period=date+'d')['Volume'][0]

# Print the volume data as a string
print(str(volume))
