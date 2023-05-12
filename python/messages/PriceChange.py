import yfinance as yf
import datetime as dt
import sys

# Define the ticker symbol
tickerSymbol = sys.argv[1]

# Define the start and end dates for the historical data
start_date = (dt.datetime.today() - dt.timedelta(days=7)).strftime('%Y-%m-%d')
end_date = dt.datetime.today().strftime('%Y-%m-%d')

# Get the historical data for the specified dates
tickerData = yf.Ticker(tickerSymbol)
tickerDf = tickerData.history(start=start_date, end=end_date)

# Calculate the change in stock price over the past week
price_change = tickerDf['Close'][-1] - tickerDf['Close'][0]

print(f"${price_change:.2f}")

