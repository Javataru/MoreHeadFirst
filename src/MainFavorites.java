import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFavorites {
    public static void main(String[] args) throws IOException {
        /**
        * finds pathway to the file
        */
        //      File file = new File("icecreamTopping.txt");
        //      System.out.println(file.getCanonicalPath());

        BufferedReader reader = null;
        ArrayList <String> myFileLines = new ArrayList <String>();
        try {
            String sCurrentLine;
            reader = new BufferedReader(new FileReader("file/Myfile.txt"));
            while ((sCurrentLine = reader.readLine()) != null) {
                //System.out.println(sCurrentLine);
                myFileLines.add(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
        } finally {
            try {
                if (reader != null)reader.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        int numElements = myFileLines.size();
        System.out.println ("there are n lines in the file:" + numElements);

        for (int counter = numElements-1; counter >= 0; counter--) {
            String mylineout = myFileLines.get(counter);
            System.out.println (mylineout);
        }
    }
}