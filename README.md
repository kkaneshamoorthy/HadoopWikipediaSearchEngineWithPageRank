# WikipediaSparkAnalysis

##Overview
This coursework will implement a text search service for a large dataset such as the English Wikipedia. The project will develop a prototype system that starting from a search term (single word), outputs a ranked list of result pages that are relevant for that.

The main aim of the coursework is to design algorithms in a sensible way for data intensive parallel computing frameworks. Both Hadoop MapReduce and Spark can be selected for the implementation. Additional libraries such as Nutch are not allowed; the algorithmic implementation code must be original from the students. However, for parsing the initial input data, supporting libraries such as the Cloud9-wikipediainputformat can be used to facilitate parsing of the wikipedia input data.

The English wikipedia dataset is suggested as the standard input for the coursework. Optionally, alternative datasets such as a corpus of Stackoverflow posts can be selected after consultation with the module organiser. 

##Design decisions

* External libraries: SAX parser, XmlInputFormat (Mahout)
* Apache Hadoop - Map/Reduce
* Inverted Index
* PageRank

##Additional

* Search containing more than one search term

##Demo
Main.java has a demo

##Running
 WikipediaSearchEngine wse = new WikipediaSearchEngine(); <br/>
 wse.search(searchTerm); //prints out the top ten results
