/*
 * Galatis Athanasios AM: 2022201500017 email: dit15017@uop.gr
 * Vakouftsis Athanasios AM: 2022201500007 email: dit15007@uop.gr
 */
package rastaengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class DocsCosineSimilarity 
{
    private static List<String> lines;
    static String query, rDoc;
    ArrayList<String> arrq;
    
    public DocsCosineSimilarity() throws IOException, ParseException 
    {                        
            query = MainFrame.cosSimRes1;
            rDoc = MainFrame.cosSimRes2;
                
            File fileDoc = new File("parsedFiles/" + rDoc + ".txt");
            rDoc = "";
            try {
                lines = Files.readAllLines(fileDoc.toPath(), StandardCharsets.UTF_8);
                rDoc += lines;
            }
            catch(IOException e) {
                System.out.println("Κάτι πήγε στραβά" + e);
            }
                
            File file = new File("parsedFiles/" + query + ".txt");
                 
            query = "";
            try {
                lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                query += lines;
            }
            catch(IOException e) {
                System.out.println("Κάτι πήγε στραβά" + e);
            }
            System.out.println("-> " + query + "  <-");
            String querystr = /*query.length() == 0 ? null :*/ query;
		               
	
            StandardAnalyzer analyzer = new StandardAnalyzer();

            // 1. create the index
            Directory index = new RAMDirectory();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter w = new IndexWriter(index, config);
            
            try (BufferedReader br = new BufferedReader(new FileReader(fileDoc))) {
		String sCurrentLine;
                arrq = new ArrayList();
                int ct=0;
		while ((sCurrentLine = br.readLine()) != null ) { 
                    arrq.add(sCurrentLine.toLowerCase().replaceAll("\\p{Punct}", "") + "\n");
                }
            } catch (IOException e) {
                    System.err.println("Parsing Error " + e);
            }
	
            addDoc(w, arrq);

            w.close();
            Query q = null;
            querystr=querystr.replaceAll("\\p{Punct}", "");
            querystr = querystr.replaceAll("\n","");
            querystr=querystr.replaceAll("\t","");
            //System.out.println("--> " + querystr);
            try {
		q = new ComplexPhraseQueryParser("title", analyzer).parse(querystr);
            } catch (org.apache.lucene.queryparser.classic.ParseException e) {
		e.printStackTrace();
            }
            
            // 3. search	
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(10);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            // 4. display results
            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
		Document d = searcher.doc(docId);
		System.out.println(" Score: "+ hits[i].score);
                JOptionPane.showMessageDialog(null, " Score: "+ hits[i].score, "Similarity Score", JOptionPane.INFORMATION_MESSAGE);
            }

            // reader can only be closed when there is no need to access the documents any more.
            reader.close();
    }
    
    private static void addDoc(IndexWriter w, ArrayList<String> title) throws IOException 
    {
            Document doc = new Document();
            doc.add(new TextField("title", title.toString(), Field.Store.YES));
            w.addDocument(doc);
    }

}
