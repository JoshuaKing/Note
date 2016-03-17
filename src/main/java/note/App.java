package note;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Josh on 17/03/2016.
 */
public class App {
    static final String NOTES_LOCATION = System.getProperty("user.home") + "/.notes/notes.log";

    public static void main(String[] args) throws IOException {
        File file = createFile();

        if (args.length == 0) {
            getNotes(file);
            return;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("--clear")) {
                if (file.delete()) {
                    System.out.println("Deleted note file " + NOTES_LOCATION);
                } else {
                    System.err.println("Could not delete " + NOTES_LOCATION);
                }
            }
            getHelp();
            return;
        }

        List<String> note = Arrays.asList(args);
        FileWriter fw = new FileWriter(file, true);
        fw.write(note.stream().reduce((a,b) -> a + "%%" + b).get() + "\n");
        fw.close();
    }

    private static void getNotes(File file) throws IOException {
        Note root = new Note(null, null);
        Stream<String> lines = Files.lines(file.toPath());
        lines.map(a -> a.split("%%"))
                .collect(Collectors.toList())
                .stream()
                .forEach(a -> addNote(root, a));
        System.out.println(root.getNote());
    }

    private static File createFile() throws IOException {
        File file = new File(NOTES_LOCATION);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }

    private static Note addNote(Note note, String[] hierarchy) {
        for (String h : hierarchy) {
            note = note.addNote(h);
        }
        return note;
    }

    public static void getHelp() {
        System.out.println("Usage:");
        System.out.println("   To add a note:    java -jar notes.jar category+ notetext");
        System.out.println("   To view notes:    java -jar notes.jar");
        System.out.println("   To clear notes:   java -jar notes.jar --clear");
        System.out.println();
        System.out.println("Notes file: \"" + NOTES_LOCATION + "\"");
        return;
    }
}
