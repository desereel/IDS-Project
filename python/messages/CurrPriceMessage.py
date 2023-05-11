import yfinance as yf
import sys

# Get the ticker symbol from the command line arguments
ticker = sys.argv[1]

# Get the stock data for the ticker
stock_data = yf.Ticker(ticker)

# Retrieve the current share price
currPrice = stock_data.info['currentPrice']

# Print the volume data as a string
print(str(currPrice))

