/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtlang;

/**
 *
 * @author Martijn
 */
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileList {

    //fields
    public static List<Path> fileNames;
    public int filesFound = 0;
    public int foldersFound = 1;

    //constructor
    public FileList() {
        FileList.fileNames = new ArrayList<>();
    }

    //method
    public List<Path> makeList(Path nextDirectory, int replace) {

        Pattern pattern = Pattern.compile("\\.(?:nl|en|dut|eng|nld)\\d?\\.");

        //Filter the files grabbed by the DirectoryStream
        DirectoryStream.Filter<Path> filter;
        filter = (Path path) -> {
            String filename = path.toString();

            //When the path is no srt-file and no directory, skip it
            if (!filename.endsWith(".srt")) {
                return Files.isDirectory(path);
            } //When the language is already determined, skip the file
            else {
                Matcher match = pattern.matcher(filename);
                if (replace==1) {
                    return true;
                } else {
                    return !match.find();      
                }
            }
        };

        //Search the directory
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(nextDirectory, filter)) {
            for (Path path : directoryStream) {

                //If the path is a directory, search it by re-calling this method
                if (Files.isDirectory(path)) {
                    foldersFound++;
                    makeList(path, replace);
                } //Otherwise add the path to the list of files.
                else {
                    filesFound++;
                    fileNames.add(path);
                }
            }
            directoryStream.close();
        } catch (IOException ex) {

        }

        return fileNames;
    }

    public void getCount() {
        System.out.println(filesFound + " files found in " + foldersFound + " folders.");
    }

}
