package rastaengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.lucene.index.IndexWriter;
import static rastaengine.Insertion.parseFile;


public class UrlInsertion extends Insertion
{
    private static List<String> lines;
    Map <String,String> map =  new LinkedHashMap<>();
    static String webName;
    static String[] researcherName;
    File file;
    
    public UrlInsertion(IndexWriter w) 
    {
        webName = MainFrame.resNameIns;
                
        researcherName = webName.split(" ");
        onoma = "";        
        webName = "https://dblp.org/pers/hb/"+ webName.toLowerCase().charAt(0) + "/" + researcherName[0] + ":" + researcherName[1] + ".html" ;
          
        System.out.println(webName);
        onoma = researcherName[0] + "_" + researcherName[1];     
        Searcher(webName);
        tryf = new File("parsedFiles/" + researcherName[0] + "_" + researcherName[1] + ".txt");     
               
        parseFile(file,w);
                
        try {   
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(UrlInsertion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public UrlInsertion() 
    {
        webName = MainFrame.resNameIns;
                
        researcherName = webName.split(" ");
        onoma = "";        
        webName = "https://dblp.org/pers/hb/"+ webName.toLowerCase().charAt(0) + "/" + researcherName[0] + ":" + researcherName[1] + ".html" ;
          
        System.out.println(webName);
        onoma = researcherName[0] + "_" + researcherName[1];     
        Searcher(webName);
        tryf = new File("parsedFiles/" + researcherName[0] + "_" + researcherName[1] + ".txt");     
               
        parseFile(file,w);
                
        try {   
            w.close();
        } catch (IOException ex) {
            Logger.getLogger(UrlInsertion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    protected void Searcher(String url)
    {
            URL urll;
            file = new File("src/rastaengine/" + researcherName[0] + "_" + researcherName[1] + "_url.bib");
            try {
                // get URL content
                urll = new URL(url);
                URLConnection conn = urll.openConnection();

                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new FileWriter(file)); 
                int cnt = 0;
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    
                    // display input line to the screen
                    System.out.println(inputLine);
                    
                    // just remove the header of the file in order to do less later in our parser
                    if ( ! (inputLine.equals("<pre class=\"verbatim select-on-click\">")) && cnt == 0) {
                        continue;
                    }
                    cnt = 1;
                    
                    // write input line to the file
                    bw.write(inputLine);
                    bw.write("\n");
                }
                br.close();
                bw.close();

                System.out.println("Done");

            }    
            catch (MalformedURLException e) {
                // malformed url is not possible in this situation but just in case...
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Malformed URL", "Error", JOptionPane.ERROR_MESSAGE);
            } 
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Check the file name and try again", "File Not Found", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    
}
