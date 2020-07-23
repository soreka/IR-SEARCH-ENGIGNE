import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.neuralnetwork.NeuralNetworkType;
import com.medallia.word2vec.util.Common;
import com.medallia.word2vec.util.Format;
import org.apache.lucene.queryParser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Word2vecer {

    public Word2vecer() throws InterruptedException, org.json.simple.parser.ParseException, ParseException, IOException {

    }

    public void demoWord() throws IOException, ParseException, org.json.simple.parser.ParseException, InterruptedException {
        File f = new File("D:\\Lucene\\Data");
        if (!f.exists()) {
            throw new IllegalStateException("Please download and unzip the text8 example from http://mattmahoney.net/dc/text8.zip");
        } else {

            List<List<String>> partitioned = Creater("D:\\Lucene\\Data", new TextFileFilter());


            Word2VecModel model = Word2VecModel.trainer().setMinVocabFrequency(5).useNumThreads(20).setWindowSize(8).type(NeuralNetworkType.CBOW).setLayerSize(200).useNegativeSamples(25).setDownSamplingRate(1.0E-4D).setNumIterations(5).setListener((stage, progress) -> System.out.println(String.format("%s is %.2f%% complete", Format.formatEnum(stage), progress * 100.0D))).train(partitioned);

            //interact(model.forSearch());
        }
    }

    public List<List<String>> Creater(String dataDirPath, FileFilter filter) throws IOException, ParseException, org.json.simple.parser.ParseException {
//        ArrayList<ArrayList<String>> all_tables_inFile = new ArrayList<ArrayList<String>>();
        List<List<String>> all_tables_inFile = new ArrayList<>();

//        List<List<String>> listOfLists = new ArrayList<>();


        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();
        for (File file : files)
        {
            if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file) )
            {
                JSONParser jsonParser = new JSONParser();
                Object object;
                object = jsonParser.parse(new FileReader(file));
                JSONObject jsonObject = (JSONObject) object;
                for (Object key : jsonObject.keySet()) {
                    JSONObject table = (JSONObject)jsonObject.get((String)key);
//                        ArrayList<String> list;
//                    List<String> innerList = new ArrayList<>();
                    List<String> list = new ArrayList<>();
                    list= (ArrayList<String>) getDataStrList(table);
                    all_tables_inFile.add(list);
                }

            }
        }
        return all_tables_inFile;
    }
    private List<String> getDataStrList(JSONObject table) {
        List<String> list =new ArrayList<String>();
//        String str="";
        JSONArray ObTitle = (JSONArray) table.get("data");
        // travel on the every array
        for (int index = 0; index < ObTitle.size(); index++) {
            JSONArray SubData = (JSONArray)ObTitle.get(index);
            Iterator itr2 = SubData.iterator();
            // travel on the inner array
            while (itr2.hasNext()) {
                list.add((String) itr2.next());
                //  str = str + itr2.next();
                //    str = str + " ";
            }
        }
//        return str;
        return list;
    }

}
