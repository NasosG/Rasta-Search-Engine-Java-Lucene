/*
 * Galatis Athanasios AM: 2022201500017 email: dit15017@uop.gr
 * Vakouftsis Athanasios AM: 2022201500007 email: dit15007@uop.gr
 */
package rastaengine;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Search 
{
    
    StandardAnalyzer analyzer = new StandardAnalyzer();
    Directory index = new RAMDirectory();
    private static List<String> lines;
    static String query;
    
    public Search() throws IOException, ParseException 
    {                        
        query = MainFrame.text;
        System.out.println("-> " + query + "  <-");
        //StandardAnalyzer analyzer = new StandardAnalyzer();
        String querystr = /*query.length() == 0 ? null :*/ query.toLowerCase();
		               
	Query q = null;
	try {
		q = new ComplexPhraseQueryParser("title", analyzer).parse(querystr);
	} catch (org.apache.lucene.queryparser.classic.ParseException e) {
		e.printStackTrace();
	}

	// 3. search
	int hitsPerPage = 10;
	IndexReader reader = DirectoryReader.open(Insertion.getIndex());
	IndexSearcher searcher = new IndexSearcher(reader);
	TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
	searcher.search(q, collector);
	ScoreDoc[] hits = collector.topDocs().scoreDocs;

	// 4. display results
	System.out.println("Found " + hits.length + " hits.");

        // reset button texts in every new search 
        for(int i=0;i<MainFrame.nameLabels.size();i++) {
            MainFrame.nameLabels.get(i).setVisible(false);
            MainFrame.labels.get(i).setVisible(false);
            MainFrame.nameLabels.get(i).setText("");
            MainFrame.labels.get(i).setText("");
        }
                
        //if no hits were found display it to the user by setting the first label's text
        if (hits.length == 0) {
            MainFrame.nameLabels.get(0).setVisible(true);
            MainFrame.nameLabels.get(0).setText("No relative documents found");
        }
                
        // show the hits ( <= 10) we found their researcher and their score
	for (int i = 0; i < hits.length; ++i) {
		int docId = hits[i].doc;
		Document d = searcher.doc(docId);
		System.out.println((i + 1) + ". " + "\t" + d.get("title").substring(12,d.get("title").length()));
                MainFrame.nameLabels.get(i).setVisible(true);
                MainFrame.labels.get(i).setVisible(true);
                MainFrame.nameLabels.get(i).setText((i + 1) + ". " + "\t" + Insertion.getName() + " Score: "+ hits[i].score);
                MainFrame.labels.get(i).setText( d.get("title").substring(12,d.get("title").length()) );
	}
                
	// reader can only be closed when there is no need to access the documents any more.
	reader.close();
    }

}
