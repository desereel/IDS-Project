import os
import pandas as pd

# Get a list of all CSV files in the current directory
csv_files = [f for f in os.listdir() if f.endswith('.csv')]

# Loop through each CSV file and replace the headers
for csv_file in csv_files:
    df = pd.read_csv(csv_file)
    df = df.rename(columns={'Date': 'Date', 'Low': 'Low', 'Open': 'Open', 'Volume': 'Volume', 'High': 'High', 'Close': 'Close', 'Adjusted Close': 'Adjusted_Close'})
    df.to_csv(csv_file, index=False)

