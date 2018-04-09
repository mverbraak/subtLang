package subtlang;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Martijn
 */
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class ReadFile {

    //fields
    private final String path;

    //constructor
    public ReadFile(String file_path) {
        path = file_path;
    }

    //method
    public String OpenFile() throws IOException {

        //Open the FileReader and try reading the file
        FileReader fr = new FileReader(path);

        //The number of lines to read. Not all are used
        String textData = "";
        int toRead = 20;
        int i = 0;
        String line;

        try (BufferedReader textReader = new BufferedReader(fr)) {

            while (i <= toRead) {
                line = textReader.readLine();

                if (line == null) {
                    break;
                } else {
                    
                    //When the line begins with a digit it is ignored
                    if (line.matches("\\d.*") || (line.length() == 0)) {
                    } //Else are lines are placed after eachother
                    else {
                        textData = textData + line + " ";
                        i++;
                    }
                }

            }
        }
        return textData;

    }

}
