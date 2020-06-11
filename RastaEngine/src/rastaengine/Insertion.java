
package rastaengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Insertion 
{
    StandardAnalyzer analyzer = new StandardAnalyzer();
    static Directory index = new RAMDirectory();
    private static List<String> lines;
    static ArrayList<String> ls =new ArrayList<>();
    static String query, onoma;
    protected static File tryf;
    static String[]  researcherName;
    IndexWriter w;
    IndexWriterConfig config;
    
    protected static Directory getIndex() {
        return index;
    }
    
    public void init(){
        /**/
    }
    
    public Insertion() 
    {
       StandardAnalyzer analyzer = new StandardAnalyzer();

	index = new RAMDirectory();
	config = new IndexWriterConfig(analyzer);

        try {
            w = new IndexWriter(index, config);
        } catch (IOException ex) {
            Logger.getLogger(Insertion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
    protected static void parseFile(File fileName,IndexWriter writer)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String sCurrentLine;
            BufferedWriter bw = new BufferedWriter(new FileWriter(tryf));
            System.out.println("File for " + onoma + " was inserted");
            JOptionPane.showMessageDialog(null, "The document for " + onoma + " was inserted successfully", "Information Message", JOptionPane.INFORMATION_MESSAGE);
            String title = " ";
            String bookTitle = " ";
            ArrayList<String> arrq = new ArrayList();
            int ct = 0;
            while ((sCurrentLine = br.readLine()) != null ) {
                ct++;
                if (sCurrentLine.contains("title")) {
                    do{
                        arrq.add(sCurrentLine.toLowerCase() + "\n");
                        if(sCurrentLine.contains("},"))
                        {
                            break;
                        }
                    }while((sCurrentLine = br.readLine()) != null);    
                }
                if (sCurrentLine.contains("booktitle")) {
                        do{
                            arrq.add(sCurrentLine.toLowerCase() + "\n");
                            if(sCurrentLine.contains("},"))
                            {
                                break;
                            }
                        }while((sCurrentLine = br.readLine()) != null);
                }		
            }
            for (String f: arrq) {
                f = f.replaceAll("\\p{Punct}","");
                addDoc(writer,f);
                bw.write(f);
            }
            bw.close();
            System.out.println(ct);
        } catch (IOException e) {
                System.err.println("Parsing Error " + e);
        }
    }

    private static void addDoc(IndexWriter w, String title) throws IOException {
            Document doc = new Document();
            doc.add(new TextField("title", title, Field.Store.YES));
            
            //doc.add(new StringField("booktitle", booktitle, Field.Store.YES));
            w.addDocument(doc);
    }
        
    public static String getName(){
        return onoma;
    }  
    
    
}
