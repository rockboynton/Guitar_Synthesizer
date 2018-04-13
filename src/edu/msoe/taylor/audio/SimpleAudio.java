/*
 * CS2852 - 021
 * Spring 2018
 * Lab 5 - Guitar Synthesizer
 * Name: Rock Boynton
 * Created: 4/11/18
 */

package edu.msoe.taylor.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine;
import java.util.List;

/**
 * A facade class that simplifies interaction with the Java sound
 *  library.  The class provides functionality for creating and playing
 *  sounds and saving the sounds as .wav files.
 * @author t a y l o r@msoe.edu
 * @author b o y n t o n r l@msoe.edu
 * @version 2018.04.11.10
 */
public class SimpleAudio {

    /**
     * BITS_PER_SAMPLE number of bits used to represent an audio sample. 
     */
    private static final int BITS_PER_SAMPLE = 16;

    /**
     * CHANNELS number of audio channels. 
     */
    private static final int CHANNELS = 1;

    /**
     * SIGNED_BYTES are the audio samples signed.
     */
    private static final boolean SIGNED_BYTES = true;

    /**
     * BIGENDIAN byte order format for audio samples.
     */
    private static final boolean BIGENDIAN = true;

    /**
     * BUFFER_SIZE size of the buffer used by SimpleAudio objects.
     */
    private static final int BUFFER_SIZE = 64000;

    /**
     * BUFFER_SIZE size of the buffer used by SimpleAudio objects.
     */
    private static final int DEFAULT_SAMPLE_RATE = 8000;

    /**
     * format audio format information.
     */
    private AudioFormat format;

    /**
     * Constructor 
     * @param sampleRate number of audio samples per second 
     */
    public SimpleAudio(float sampleRate) {
        format = new AudioFormat(sampleRate, BITS_PER_SAMPLE, CHANNELS,
                SIGNED_BYTES, BIGENDIAN);
    }

    /**
     * Default constructor which uses the default sample rate.
     */
    public SimpleAudio() {
        this(DEFAULT_SAMPLE_RATE);
    }

    /**
     * Send audio samples to the the speakers.
     * @param samples Audio samples to be sent to the speakers.
     * @throws LineUnavailableException
     * @throws IOException
     */
    public void play(List<Float> samples) throws LineUnavailableException, IOException {
        AudioInputStream audioIS = toAudioInputStream(samples);
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        if(AudioSystem.isLineSupported(dataLineInfo)) {
            try (SourceDataLine line = (SourceDataLine)AudioSystem.getLine(dataLineInfo)) {
                line.open(format);
                line.start();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = 0;
                int offset = 0;
                while(bytesRead != -1) {
                    bytesRead = audioIS.read(buffer);
                    if(bytesRead>0) {
                        line.write(buffer, offset, bytesRead);
                    }
                }
                line.drain();
            }
        }
    }

    /**
     * Save audio samples to a file using the .wav format.
     * @param file File to receive the audio samples.
     * @param samples Audio samples to be written to the file.
     * @throws IOException
     */
    public void saveWAV(File file, List<Float> samples) throws IOException {
        AudioInputStream audioIS = toAudioInputStream(samples);
        AudioSystem.write(audioIS, AudioFileFormat.Type.WAVE, file);
    }

    /**
     * Convert the audio samples into an audio input stream.
     * @param samples Audio samples to be used.
     * @return An audio input stream containing the audio samples.
     */
    private AudioInputStream toAudioInputStream(List<Float> samples) {
        byte[] data = toBytes(samples);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        long numOfFrames = data.length / format.getFrameSize();
        return new AudioInputStream(byteStream, format, numOfFrames);
    }

    /**
     * Convert the audio samples into a byte array that can be used
     * to populate an AudioInputStream.
     * @param samples Audio samples to be used.
     * @return A byte array representation of the audio samples.
     */
    private byte[] toBytes(List<Float> samples) {
        byte[] byteArray = new byte[samples.size()*2];
        int index = 0;
        for(float sample : samples) {
            sample = Math.min(1.0f, Math.max(-1.0f, sample));
            int quantized = Math.round(sample * 32767.0f);
            byte high = (byte)((quantized>>8) & 0xff);
            byte low = (byte)(quantized & 0xff);
            byteArray[index++] = high;
            byteArray[index++] = low;
        }
        return byteArray;
    }
}
