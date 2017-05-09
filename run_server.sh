#!/bin/bash
cp ./index/* ./target/index/
cp ./spam.dic ./target
cd ./target
java -cp $CLASSPATH:./lib/:./chatbotv2-0.0.1-SNAPSHOT.jar com.wanda.chatbotv1.Searcher
