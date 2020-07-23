# IR-SEARCH-ENGIGNE
in this project we implemented a little search engine, which takes tables from json format ,
then make index out of them and then search the query 
in corpus 
we did implement it in java , based on Lucene library specifically
first we took the json parse it into  fileds and then into lucne document 
each document have several fields which taken from the table 
----------------------------------------------------------------
in the searcher we did use a multifield query parser 
and weighted each field diffrently based on it's importance in the table 
in the index the analayzer that we used is a custom one we made the standard analayzer thenadded some filters to it
like porterstemmer lowercasing ,casefolding .
the retrieval is for the best 20 docouments 
the score is the VSM&boolean model .
