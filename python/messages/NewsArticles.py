import requests
import sys

ticker = sys.argv[1]

# set your API key
api_key = "a48b80df7a284576af40d162e1b64d96"

# set the base URL for the News API
base_url = "https://newsapi.org/v2/everything"

# set the parameters for your query
query_params = {
    "q": ticker,
    "sortBy": "publishedAt",
    "apiKey": api_key
}

# send a GET request to the News API
response = requests.get(base_url, params=query_params)

# check if the request was successful
if response.status_code == 200:
    # extract the articles from the response JSON
    articles = response.json()["articles"]
    
    # print out the URLs for the latest articles
    for article in articles[:5]:
        print(str(article["url"]))
else:
    # handle errors
    print("Error:", response.status_code)

