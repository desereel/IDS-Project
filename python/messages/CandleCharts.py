import yfinance as yf
import mplfinance as mpf
import sys

ticker = sys.argv[1]

# download the data for the ticker symbol
df = yf.download(ticker, start="2022-04-01", end="2022-04-30")

# plot the candlestick chart
mpf.plot(df, type='candle', mav=(10,20,30))
