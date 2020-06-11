/*
 * Galatis Athanasios AM: 2022201500017 email: dit15017@uop.gr
 * Vakouftsis Athanasios AM: 2022201500007 email: dit15007@uop.gr
 */
package rastaengine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import javax.swing.JOptionPane;
import static rastaengine.Insertion.onoma;

public class FileInsertion extends Insertion 
{
    private static List<String> lines;

    public FileInsertion() 
    {   
        onoma = MainFrame.resNameIns;

        File file =new File("src/rastaengine/" + onoma + ".bib");
        
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);  
        }
        catch(IOException e) {
            System.out.println("Something went wrong" + e);
            JOptionPane.showMessageDialog(null, "Check the file name and try again", "File Not Found", JOptionPane.ERROR_MESSAGE);
        }
        try {
            tryf = new File("parsedFiles/" + onoma + ".txt");
            parseFile(file,w);
            w.close();
        } catch (IOException e) {}

    }
    
}
