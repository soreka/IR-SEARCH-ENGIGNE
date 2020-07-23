

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.neuralnetwork.NeuralNetworkType;
import com.medallia.word2vec.util.Common;
import com.medallia.word2vec.util.Format;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LuceneTester
{
    String indexDir = "D:\\Lucene\\Index";
    String dataDir = "D:\\Lucene\\Data";
    Indexer indexer;
    Searcher searcher;
    public static void main(String[] args) throws InterruptedException, ParseException, org.json.simple.parser.ParseException, IOException {
        LuceneTester tester;
        try
        {
            tester = new LuceneTester();
            tester.createIndex();
            tester.search("Professional ratings");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
//        Word2vecer word2vecer= new Word2vecer();
//        word2vecer.demoWord();
        // try to use word2vec

    }
    private void createIndex() throws IOException, org.json.simple.parser.ParseException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+" File indexed, time taken: " +(endTime-startTime)+" ms");
    }
    private void search(String searchQuery) throws IOException, ParseException
    {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();
        System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime));

        for(ScoreDoc scoreDoc : hits.scoreDocs)
        {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }

}