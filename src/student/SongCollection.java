package student;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Stream;

/*
 * 
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 - Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 * SongCollection.java 
 * Read the specified data file and build an array of songs.
 */
/**
 *
 * @author boothe
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore not
     * portable. Java runs on a virtual machine that IS portable. So this is
     * permissable because we are programming in Java.
     *
     * @param filename The path and filename to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */
    public SongCollection(String filename) {
        // use a try catch block
        // read in the song file and build the songs array
        // you must use a StringBuilder to read in the lyrics!
        // you must add the line feed at the end of each lyric line.
        ArrayList<Song> songList = new ArrayList<>();
        Scanner inputFile = null;
        try {
            inputFile = new Scanner(new File(filename));
        } catch (InputMismatchException e) {
            System.out.println("Probably using nextInt or nextDouble"
                    + " when the file input is not of that type.");
            System.out.println(e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Probably some problem with the input"
                    + " data file or the keyboard input.");
            System.out.println(e);
            e.printStackTrace();
        }
        if (!inputFile.hasNext()) {
            System.err.println("No data in file " + filename);
            return; // exits program
        }

        while (inputFile.hasNext()) { // while the file is not empty
            StringBuilder str = new StringBuilder();
            String artist = inputFile.nextLine();
            String title = inputFile.nextLine();
            String lyrics = inputFile.nextLine();
            boolean moreLyrics = true;
            String nextLine;

            if (artist.startsWith("ARTIST")) {
                artist = artist.substring(artist.indexOf("\"") + 1,
                        artist.lastIndexOf("\""));
            }
            if (title.startsWith("TITLE")) {
                title = title.substring(title.indexOf("\"") + 1,
                        title.lastIndexOf("\""));
            }
            if (lyrics.startsWith("LYRICS")) {
                str.append(lyrics).append("\n");
                while (moreLyrics) {
                    nextLine = inputFile.nextLine();
                    if (nextLine.startsWith("\"")) {
                        str.append(nextLine);
                        moreLyrics = false;
                    } else {
                        str.append(nextLine).append("\n");
                    }
                }
                lyrics = str.toString();
                lyrics = lyrics.substring(lyrics.indexOf("\""),
                        lyrics.lastIndexOf("\"") + 1);

            }
            Song song = new Song(artist, title, lyrics);
            songList.add(song);
        }
        songs = new Song[songList.size()];
        songList.toArray(songs);

        // sort the songs array using Array.sort (see the Java API)
        Arrays.sort(songs);

    }

    /**
     * this is used as the data source for building other data structures
     *
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }

    /**
     * unit testing method
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // todo: show song count 
        System.out.println("Total songs: " + sc.getAllSongs().length);
        Stream.of(sc.getAllSongs()).limit(10).forEach(System.out::println);
    }
}
