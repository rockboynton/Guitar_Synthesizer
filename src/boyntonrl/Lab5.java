/*
 * CS2852 - 021
 * Spring 2018
 * Lab 5 - Guitar Synthesizer
 * Name: Rock Boynton
 * Created: 4/11/18
 */

package boyntonrl;

import edu.msoe.taylor.audio.Note;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Driver program for Data Structures lab assignment
 * @author t a y l o r@msoe.edu
 * @author b o y n t o n r l@msoe.edu
 * @version 2018.04.12
 */
public class Lab5 {

    /**
     * Program that reads in notes from a text file and plays a song
     * using the Guitar class to generate the sounds which are then
     * played by a SimpleAudio object.
     * @param args Ignored
     */
    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        System.out.print("Enter the name of the file you'd like to play: ");
        String fileName = stdIn.nextLine();
        Path path = Paths.get(fileName);
        File audioFile = new File(path.toString());
        try (Scanner parser = new Scanner(audioFile)) {
            Guitar guitar = new Guitar();
            String line;
            int lineNumber = 0;
            while (parser.hasNextLine()) {
                line = parser.nextLine();
                Note note = parseNote(line);
                if (note != null) {
                    guitar.addNote(parseNote(line));
                } else {
                    System.err.println("Line " + lineNumber + " was ignored");
                }
                lineNumber++;
            }
            guitar.play();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (LineUnavailableException lue) {
            System.err.println("Line unavailable");
        }

    }
    
    /**
     * Returns a new Note initialized to the value represented by the specified String
     * @param line Description of a note with scientific pitch followed by duration in milliseconds.
     * @return Note represented by the String passed in.  Returns null if it is unable to parse
     * the note data correctly.
     */
    private static Note parseNote(String line) {
        Note note = null;
        String[] elements = line.split(" ");
        if (elements.length > 0) {
            String scientificPitch;
            float duration;
            scientificPitch = elements[0];
            duration = Float.parseFloat(elements[1]);
            note = new Note(scientificPitch, duration);
        }
        return note;
    }
}
