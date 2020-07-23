import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;


import java.io.IOException;
import java.io.Reader;

public class MyAnalayzer extends Analyzer {
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream stream = new StandardTokenizer(Version.LUCENE_36, reader);
        TokenStream result = stream;
        result =new ClassicFilter(result);
        result = new LowerCaseFilter(result);
        result =new PorterStemFilter(result);
        return result;
    }}