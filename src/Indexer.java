import com.medallia.word2vec.Searcher;
import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.Word2VecTrainerBuilder;
import com.medallia.word2vec.neuralnetwork.NeuralNetworkType;
import com.medallia.word2vec.util.Common;
import com.medallia.word2vec.util.Format;
import com.medallia.word2vec.util.ProfilingTimer;
import com.medallia.word2vec.util.ThriftUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//import org.apache.lucene.document.StringField;


public class Indexer
{
    private IndexWriter writer;
    public Indexer(String indexDirectoryPath) throws IOException
    {
        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

        Analyzer analyzer = new MyAnalayzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36,  analyzer);
        //create the indexer
        writer = new IndexWriter (indexDirectory,
               analyzer,true,
                IndexWriter.MaxFieldLength.UNLIMITED);


    }
    public void close() throws CorruptIndexException, IOException
    {
        writer.close();
    }
    private Document getDocument(File file,JSONObject table,String key) throws IOException
    {
        Document document = new Document();

        //index file contents
//        Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file));//,
//                                Field.Store.YES,
//                Field.Index.NOT_ANALYZED));


        //index file name
        Field fileNameField = new Field(LuceneConstants.FILE_NAME,
                file.getName(),
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);


        //index file path
        Field filePathField = new Field(LuceneConstants.FILE_PATH,
                file.getCanonicalPath(),
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);


        //index file TABLE_ID
        Field FileTABLE_TABLE_ID = new Field(LuceneConstants.TABLE_ID,
                (String)key,
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);

        //index file Title
        String TitleStr=getTitleStr(table);
        Field FileTABLE_TITLE = new Field(LuceneConstants.TITLE,
                TitleStr,
                Field.Store.YES,
                Field.Index.ANALYZED);

//        String numColsStr= (String)table.get("numCols");
        String numColsStr= table.get("numCols").toString();
        Field FileTABLE_NUMCOLS = new Field(LuceneConstants.NUMCOLS,
                numColsStr,
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);

        //we have to chech if correct becuce it is array
//        String numericColumnsStr=(String) table.get("numericColumns");
        String numericColumnsStr=table.get("numericColumns").toString();
        Field FileTABLE_NUMERICCOLUMNS = new Field(LuceneConstants.NUMERICCOLUMNS,
                numericColumnsStr,
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);

//        String pgTitleStr=(String) table.get("pgTitle");
        String pgTitleStr=table.get("pgTitle").toString();
        Field FileTABLE_PGTITLE = new Field(LuceneConstants.PGTITLE,
                pgTitleStr,
                Field.Store.YES,
                Field.Index.ANALYZED);

//        String NUMDATAROWSStr=(String) table.get("numDataRows");
        String NUMDATAROWSStr=table.get("numDataRows").toString();
        Field FileTABLE_NUMDATAROWS = new Field(LuceneConstants.NUMDATAROWS,
                NUMDATAROWSStr,
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);


//        String SECONDTITLEStr=(String) table.get("secondTitle");
        String SECONDTITLEStr=table.get("secondTitle").toString();
        Field FileTABLE_SECONDTITLE = new Field(LuceneConstants.SECONDTITLE,
                SECONDTITLEStr,
                Field.Store.YES,
                Field.Index.ANALYZED);

//        String NUMHEADERROWSStr=(String) table.get("numHeaderRows");
        String NUMHEADERROWSStr=table.get("numHeaderRows").toString();
        Field FileTABLE_NUMHEADERROWS = new Field(LuceneConstants.NUMHEADERROWS,
                NUMHEADERROWSStr,
                Field.Store.YES,
                Field.Index.NOT_ANALYZED);

//        String CAPTIONStr=(String) table.get("caption");
        String CAPTIONStr=table.get("caption").toString();
        Field FileTABLE_CAPTION = new Field(LuceneConstants.CAPTION,
                CAPTIONStr,
                Field.Store.YES,
                Field.Index.ANALYZED);

        String DataStr=getDataStr(table);
        Field FileTABLE_DATA = new Field(LuceneConstants.DATA,
                DataStr,
                Field.Store.YES,
                Field.Index.ANALYZED);


        document.add(fileNameField);
        document.add(filePathField);

        document.add(FileTABLE_TABLE_ID);
        document.add(FileTABLE_TITLE);
        document.add(FileTABLE_NUMCOLS);
        document.add(FileTABLE_NUMERICCOLUMNS);
        document.add(FileTABLE_PGTITLE);
        document.add(FileTABLE_NUMDATAROWS);
        document.add(FileTABLE_SECONDTITLE);
        document.add(FileTABLE_NUMHEADERROWS);
        document.add(FileTABLE_CAPTION);
        document.add(FileTABLE_DATA);


//        System.out.println(document.getFields());
//        System.out.println(document.get("data"));

        return document;
    }

    private void indexFile(File file) throws IOException
    {
        JSONParser jsonParser = new JSONParser();
        Object object;

        try {
            object = jsonParser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) object;
            for (Object key : jsonObject.keySet()) {
                // System.out.println("1");
                JSONObject table = (JSONObject)jsonObject.get((String)key);
                Document document = getDocument(file,table,(String)key);
                writer.addDocument(document);

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // System.out.println("Indexing "+file.getCanonicalPath());
        // Document document = getDocument(file);
        //  writer.addDocument(document);
    }

    public int createIndex(String dataDirPath, FileFilter filter) throws IOException
    {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();
        for (File file : files)
        {
            if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file) )
            {
                indexFile(file);
            }
        }
        return writer.numDocs();
    }

    private String getTitleStr(JSONObject table) {
        String str="";
        JSONArray ObTitle = (JSONArray) table.get("title");
        Iterator itr2 = ObTitle.iterator();
        while (itr2.hasNext()) {
            str = str + itr2.next();
            str = str + " ";
        }
        return str;
    }

    private String getDataStr(JSONObject table) {
        String str="";
        JSONArray ObTitle = (JSONArray) table.get("data");
        // travel on the every array
        for (int index = 0; index < ObTitle.size(); index++) {
            JSONArray SubData = (JSONArray)ObTitle.get(index);
            Iterator itr2 = SubData.iterator();
            // travel on the inner array
            while (itr2.hasNext()) {
                str = str + itr2.next();
                str = str + " ";
            }
        }
        return str;
    }

    private String getStrFromArray(String ob[]) {
        String str="";
        for(int i=0; i<ob.length;i++){
            str = str + ob[i];
            str = str + " ";
        }
        return str;
    }

}