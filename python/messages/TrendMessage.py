import yfinance as yf
import sys

# Define the ticker symbol
tickerSymbol = sys.argv[1]

# Get the historical data for the past year
tickerData = yf.Ticker(tickerSymbol)
tickerDf = tickerData.history(period='1y')

# Calculate the 50-day moving average and the 200-day moving average
tickerDf['MA50'] = tickerDf['Close'].rolling(window=50).mean()
tickerDf['MA200'] = tickerDf['Close'].rolling(window=200).mean()

# Check if the current price is above the moving averages to determine the trend
if tickerDf['Close'][-1] > tickerDf['MA50'][-1] and tickerDf['Close'][-1] > tickerDf['MA200'][-1]:
    trend = "Upward"
elif tickerDf['Close'][-1] < tickerDf['MA50'][-1] and tickerDf['Close'][-1] < tickerDf['MA200'][-1]:
    trend = "Downward"
else:
    trend = "Uncertain"

print(f"{trend}")

