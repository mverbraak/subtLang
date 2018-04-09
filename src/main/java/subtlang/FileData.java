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
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileData {

    public static void main(String[] arg) throws IOException {

        //Test arguments
        int rename = 0;
        int replace = 0;
        if (arg.length == 0) {
            System.out.println("Wrong argument. Use 'rename' or 'test' and "
                    + "additionally 'replace' if the script also has to process "
                    + "files that already have a language tag.");
            return;
        } else if (!arg[0].equals("rename") && !arg[0].equals("test")) {
            System.out.println("Wrong argument. Use 'rename' or 'test' and "
                    + "additionally 'replace' if the script also has to process "
                    + "files that already have a language tag.");
            return;
        } else if (arg[0].equals("rename")) {
            rename = 1;
        }

        if (arg.length == 2) {
            if (arg[1].equals("replace")) {
                replace = 1;
            }
        }

        //Ask for the directory to process
        Scanner user_input = null;
        String approval = "";
        Path directory = null;
        try{
            user_input = new Scanner(System.in);
            while (!approval.equals("Y") && !approval.equals("y")) {
                System.out.print("Input directory: ");
                directory = Paths.get(user_input.nextLine().replaceAll("^\"|\"$", ""));
                while (!Files.isDirectory(directory)) {
                    System.out.print("Input is not a directory, try again: ");
                    directory = Paths.get(user_input.nextLine().replaceAll("^\"|\"$", ""));
                }
                System.out.println("You typed: " + directory);
                System.out.println("Is this correct? Y/N");
                approval = user_input.nextLine();
            }
        }
        finally {
            if(user_input!=null)
                user_input.close();
        }

            //Retrieve the list of files to process selected and filtered by the 
            //class FileList
            FileList file_list = new FileList();
            List<Path> file_names = file_list.makeList(directory, replace);
            file_list.getCount();

        //Print this list
        System.out.println();
        System.out.println("Files to process:");
        for (Path element : file_names) {
            System.out.println(element);
        }

        System.out.println();
        System.out.println("Start processing:");

        //For each file on the list an iteration is done
        int processed = 0;
        for (Path path : file_names) {

            //Retrieve the element's old filename and print it
            String file_name = path.getFileName().toString();
            System.out.println("Old name: " + file_name);

            try {

                //Open the file and use the class ReadFile to obtain some content
                ReadFile file = new ReadFile(path.toString());
                String content = file.OpenFile();

                //Use the class SubtLang to determine the language of the content
                SubtLang category = new SubtLang(content);
                String language = category.Language();
                System.out.println("Recognized language: " + language);

                //Essemble a new name for the file and rename the file
                String new_path;

                Pattern pattern = Pattern.compile("([\\s\\S]*)\\.(?:nl|en|dut|nld|eng)?\\d?\\.");
                Matcher match = pattern.matcher(path.toString());
                if (match.find()) {
                    new_path = match.group(1);
                    new_path = Matcher.quoteReplacement(new_path);
                    System.out.println("Stripped name: " + new_path);
                } else {
                    new_path = path.toString().substring(0, path.toString().length() - 4);
                }

                int id;

                switch (language) {
                    case "dutch":
                        id = 0;
                        break;
                    case "english":
                        id = 1;
                        break;
                    default:
                        id = 4;
                        break;
                }

                String[] tag = new String[5];
                tag[0] = ".nl";
                tag[1] = ".en";
                tag[2] = ".dut";
                tag[3] = ".eng";
                tag[4] = "";

                int i = 2;
                String j = "";
                while (Files.exists(Paths.get(new_path + tag[id] + j + ".srt"))
                        & !Paths.get(new_path + tag[id] + j + ".srt").equals(path)) {
                    if (id < 2 & j.equals("")) {
                        id = id + 2;
                    } else if (id < 4 & j.equals("")) {
                        id = id - 2;
                        j = "" + i;
                        i++;
                    } else if (id == 4) {
                        j = "." + i;
                        i++;
                    } else {
                        j = "" + i;
                        i++;
                    }
                }
                new_path = new_path + tag[id] + j + ".srt";
                String new_name = Paths.get(new_path).getFileName().toString();

                //Only rename the files if specifically said to do
                if (rename == 1 & !Paths.get(new_path).equals(path)) {
                    Files.move(path, path.resolveSibling(new_name));
                    processed++;
                }
                System.out.println("New name: " + new_name);

            } //In case of an error, print the error //In case of an error, print the error
            catch (IOException e) {
                System.out.println("Error :" + e.getMessage());
            }

        }
        System.out.println();
        System.out.println("Finished");
        file_list.getCount();
        System.out.println("Renamed " + processed + " files.");

    }

}
