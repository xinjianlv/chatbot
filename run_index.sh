#!/bin/bash
cd ./target
java -cp $CLASSPATH:./lib/:./chatbotv1-0.0.1-SNAPSHOT.jar com.wanda.chatbotv1.Indexer ../corpus/corpus.out ../index
