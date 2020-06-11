/*
 * Galatis Athanasios AM: 2022201500017 email: dit15017@uop.gr
 * Vakouftsis Athanasios AM: 2022201500007 email: dit15007@uop.gr
 */
package rastaengine;

import java.io.IOException; 
import java.nio.file.*; 
import javax.swing.JOptionPane;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.Query;
  
public class RemoveResearcher extends Insertion
{
    public RemoveResearcher(String delTxt) 
    { 
        Query q = null;
	try {
		q = new ComplexPhraseQueryParser("title", analyzer).parse(MainFrame.delText);
	} catch (org.apache.lucene.queryparser.classic.ParseException e) {
		e.printStackTrace();
	}
        
        try { 
            Files.delete(Paths.get("parsedFiles/" + delTxt + ".txt")); 
            w.deleteDocuments(q);
            System.out.println("Deletion successful");
            JOptionPane.showMessageDialog(null, "Deletion successful", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (NoSuchFileException e) { 
            System.out.println("No such file/directory exists");
            JOptionPane.showMessageDialog(null, "This researcher does not exist", "Deletion Message", JOptionPane.WARNING_MESSAGE);
        } 
        catch (DirectoryNotEmptyException e) { 
            System.out.println("Directory is not empty"); 
            JOptionPane.showMessageDialog(null, "Directory is not empty", "Deletion Message", JOptionPane.WARNING_MESSAGE);
        } 
        catch (IOException e) { 
            System.out.println("Invalid permissions");
            JOptionPane.showMessageDialog(null, "Invalid permissions", "Deletion Message", JOptionPane.WARNING_MESSAGE);
        } 
    } 
}
