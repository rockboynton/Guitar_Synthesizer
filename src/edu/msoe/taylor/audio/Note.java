/*
 * CS2852 - 021
 * Spring 2018
 * Lab 5 - Guitar Synthesizer
 * Name: Rock Boynton
 * Created: 4/11/18
 */

package edu.msoe.taylor.audio;

/**
 * The Note class describes a note.  Each note must have duration and either a
 * pitch specified in scientific pitch notation or a frequency.  If scientific
 * pitch is specified, the frequency is calculated.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Scientific_pitch_notation">Scientific Pitch Notation</a>
 * 
 * @author t a y l o r@msoe.edu
 * @author b o y n t o n@msoe.edu
 * @version 2018.04.11.10
 */
public class Note {
    /**
    * Name of note in scientific pitch notation.
    */
    private final String name;
    /** 
    * Frequency of note in cycles per second. 
    */
    private final float frequency;
    
    /**
    *  Duration of note in milliseconds.
    */
    private final float duration;
    
    /** 
    * Frequency for A0 
    */
    private static final float A0 = 27.5f;

    /**
    * Constructs a new Note object with the specified pitch and duration.
    * @param name Scientific pitch notation for the note
    * @param duration Duration of the note in milliseconds
    */
    public Note(String name, float duration) {
        this.name = name;
        frequency = getFrequency(name);
        if(duration<0) {
            throw new IllegalArgumentException("Invalid duration (cannot be negative).");
        }
        this.duration = duration;
    }
    
    /**
     * Constructs a new Note object with the specified frequency and duration.
     * @param frequency Frequency in hertz
     * @param duration Duration in milliseconds
     */
    public Note(float frequency, float duration) {
        this.name = null;
        if(frequency<0) {
            throw new IllegalArgumentException("Invalid frequency (cannot be negative).");
        }
        this.frequency = frequency;
        if(duration<0) {
            throw new IllegalArgumentException("Invalid duration (cannot be negative).");
        }
        this.duration = duration;
    }
    
    /**
     * Returns the frequency of the specified scientific pitch
     * For example: ("C4") specifies middle C on a standard piano (~262Hz)
     * @param scientificPitch Note from A-G, including sharps (#) with octave
     *                        specified as "A4", "A#4", "B4", ...
     * @return The frequency of the specified note.
     * @see <a href="http://en.wikipedia.org/wiki/Note">wikipedia Note entry</a>
     */
    private float getFrequency(String scientificPitch) {
        String note = "";
        int octave;
        if(scientificPitch.charAt(1) == '#') {
            note = scientificPitch.substring(0,2);
            octave = Integer.parseInt(scientificPitch.substring(2));
        } else {
            note = scientificPitch.substring(0,1);
            octave = Integer.parseInt(scientificPitch.substring(1));
        }
        double power = 0;
        note = note.toUpperCase();
        if(note.equals("C")) {
            power = (12.0*octave-9)/12;
        } else if(note.equals("C#")) {
            power = (12.0*octave-8)/12;
        } else if(note.equals("D")) {
            power = (12.0*octave-7)/12;
        } else if(note.equals("D#")) {
            power = (12.0*octave-6)/12;
        } else if(note.equals("E")) {
            power = (12.0*octave-5)/12;
        } else if(note.equals("F")) {
            power = (12.0*octave-4)/12;
        } else if(note.equals("F#")) {
            power = (12.0*octave-3)/12;
        } else if(note.equals("G")) {
            power = (12.0*octave-2)/12;
        } else if(note.equals("G#")) {
            power = (12.0*octave-1)/12;
        } else if(note.equals("A")) {
            power = octave;
        } else if(note.equals("A#")) {
            power = (12.0*octave+1)/12;
        } else if(note.equals("B")) {
            power = (12.0*octave+2)/12;
        } else {
            throw new IllegalArgumentException("Note note in scientific pitch notation: " + note);
        }
        return A0*(float)Math.pow(2,power);
    }

    /**
     * Returns the frequency (in Hz) of this note.
     * @return frequency in hertz
     */
    public float getFrequency() {
        return frequency;
    }
    
    /**
     * Returns the duration of this note.
     * @return duration in milliseconds
     */
    public float getDuration() {
        return duration;
    }
    
    /**
     * Returns a string representation of the note.
     */
    public String toString() {
        return "" + (name == null ? frequency : name) + " " + duration;
    }
}
