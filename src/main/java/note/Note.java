package note;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Josh on 17/03/2016.
 */
public class Note {
    Note parent;
    int indentation = -1;
    List<Note> notes;
    String value;

    public Note(Note parent, String value) {
        this.parent = parent;
        this.value = value;
        notes = new ArrayList<>();
    }

    public Note addNote(String note) {
        Optional<Note> existing = notes.stream().filter(n -> n.value.equals(note)).findFirst();
        if (existing.isPresent()) return existing.get();
        Note n = new Note(this, note);
        n.indentation = indentation + 1;
        notes.add(n);
        return n;
    }

    public String getNote() {
        String output = "";

        if (value != null) {
            for (int i = 0; i < indentation; i++) {
                output += "   ";
            }
            output += (notes.size() > 0) ? "* " : "- ";
            output += value;
            output += (notes.size() > 0) ? ":\n" : "\n";
        }

        for (Note note : notes) {
            if (note.notes.size() == 0) output += note.getNote();
        }

        boolean first = true;
        for (Note note : notes) {
            if (note.notes.size() > 0) {
                if (first) {
                    first = false;
                } else {
                    output += "\n";
                }
                output += note.getNote();
            }

        }

        return output;
    }
}
