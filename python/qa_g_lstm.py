import os
import re
import csv
import json
import random
import numpy as np
import tensorflow as tf
import pandas as pd

from tensorflow import keras
from collections import defaultdict
from keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from keras import backend as K
from tensorflow.keras.layers.experimental.preprocessing import TextVectorization
from keras.utils import to_categorical
from keras.models import Sequential
from keras.models import load_model
from keras.layers import Activation, Dense, Dropout, LSTM, Embedding, Conv1D, Masking, Flatten, MaxPooling1D, GlobalMaxPooling1D
from sklearn.feature_extraction.text import TfidfTransformer

GLOVE_DIR = "input/"
EMBEDDING_DIM = 100
MAX_SEQUENCE_LENGTH = 200

random.seed(1337)
np.random.seed(1337)
tf.random.set_seed(1337)

categories = []
vectorizer = TextVectorization(max_tokens=30000, output_sequence_length=200)

with open('data/questions.txt') as f:

    firstColumn = [ line.split(',')[0] for line in f]
    secondColumn = [ line.split(',')[1] for line in f]

for category in firstColumn:

    if category not in categories:
        categories.append(category)

vocab_size = 10000

catdat = defaultdict(list)
dat = list()
cat = list()

# PREPROCESSING

for i, category in enumerate(categories):

    with open('data/questions.txt') as f:

        for line in f:

            if line.startswith(category):
                question = line.split(",",1)[1]
                question = question.replace('\n', ' ')
                question = re.sub('[^0-9a-zA-Z]+', ' ', question)
                question = question.lower()

                catdat[category].append(question)

                dat.append(question)
                cat.append(i)

# SPLIT

temp = list(zip(cat, dat))
random.shuffle(temp)
cat, dat = zip(*temp)

trainlen = int(len(cat) * 1)
vallen = int(len(cat) * 0)
testlen = int(len(cat) * 0)

traindat = dat[:trainlen]
valdat = dat[trainlen:trainlen+vallen]
testdat = dat[trainlen+vallen:]

traincat = cat[:trainlen]
valcat = cat[trainlen:trainlen+vallen]
testcat = cat[trainlen+vallen:]

# TOKENIZER

tokenizer = Tokenizer(filters='', lower=False, num_words=vocab_size, oov_token="UNK")
tokenizer.fit_on_texts(traindat)
word_index = tokenizer.word_index

Ytrain = np.asarray(traincat)

Yval = np.asarray(valcat)

Ytest = np.asarray(testcat)

Xtrain = tokenizer.texts_to_sequences(traindat)
Xval = tokenizer.texts_to_sequences(valdat)
Xtest = tokenizer.texts_to_sequences(testdat)

# SAVING TOKENIZER
tokenizer_dict = json.loads(tokenizer.to_json())

with open('tokenizer.json', 'w', encoding='utf-8') as f:
    f.write(json.dumps(tokenizer_dict, ensure_ascii=False))

text_maxlen = 200
num_classes = 10
vector_size = 100
batch_size = 1

Xtrain = pad_sequences(Xtrain, padding="post", truncating="post", maxlen=text_maxlen)
Xval = pad_sequences(Xval, padding="post", truncating="post", maxlen=text_maxlen)
Xtest = pad_sequences(Xtest, padding="post", truncating="post", maxlen=text_maxlen)

Ytrain = to_categorical(Ytrain, num_classes=num_classes)
Yval = to_categorical(Yval, num_classes=num_classes)
Ytest = to_categorical(Ytest, num_classes=num_classes)

embeddings_index = {}
f = open(os.path.join(GLOVE_DIR, 'glove.6B.100d.txt'))
for line in f:
    word, coefs = line.split(maxsplit=1)
    coefs = np.fromstring(coefs, "f", sep=" ")
    embeddings_index[word] = coefs
f.close()

embedding_matrix = np.zeros((len(word_index) + 1, EMBEDDING_DIM))
for word, i in word_index.items():
    embedding_vector = embeddings_index.get(word)
    if embedding_vector is not None:
       embedding_matrix[i] = embedding_vector

# CREATING THE NEURAL MODEL
model = Sequential()
model.add(Embedding(len(word_index) + 1,
                    EMBEDDING_DIM,
                    weights=[embedding_matrix],
                    input_length=MAX_SEQUENCE_LENGTH,
                    trainable=False))
model.add(Conv1D(filters=128, kernel_size=5, activation='relu'))
model.add(MaxPooling1D(pool_size=5))
model.add(Conv1D(filters=128, kernel_size=5, activation='relu'))
model.add(GlobalMaxPooling1D())
model.add(Dense(num_classes, activation='softmax'))

# Compile the model
model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

# Train the model
model.fit(Xtrain, Ytrain, validation_data=(Xval, Yval), epochs=4, batch_size=batch_size)

# Save the model
model.save('qa_g_lstm.h5')
