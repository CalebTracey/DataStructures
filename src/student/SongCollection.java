package student;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/*
 * 1.2021 - Caleb Tracey & Abdikarim Jimale - 
 *      Collaborated to impliment SongCollection method for reading and sorting 
 *      data from file.
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 - Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 * SongCollection.java 
 * Read the specified data file and build an array of songs.
 */
/**
 *
 * @author calebtracey
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
        // temp ArrayList for holding song objects
        ArrayList<Song> songList = new ArrayList<>(); 
        Scanner inputFile = null;
        try {
            inputFile = new Scanner(new File(filename));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (!inputFile.hasNext()) {
            System.err.println("No data in file " + filename);
            return; // exits program
        }

        while (inputFile.hasNext()) { // while the file is not empty
            StringBuilder str = new StringBuilder();
            // Read file by line and set variables
            String artist = inputFile.nextLine();
            String title = inputFile.nextLine();
            String lyrics = inputFile.nextLine();
            boolean moreLyrics = true; // Needed to aquire all lyrics
            String nextLine; // Used to check for end of lyrics

            if (artist.startsWith("ARTIST")) {
                // If artist var has correct data, create substring from inside
                // quotation marks. Same for title.
                artist = artist.substring(artist.indexOf("\"") + 1,
                        artist.lastIndexOf("\""));
            }
            if (title.startsWith("TITLE")) {
                title = title.substring(title.indexOf("\"") + 1,
                        title.lastIndexOf("\""));
            }
            if (lyrics.startsWith("LYRICS")) {
                str.append(lyrics).append("\n");
                // str is appended with data already in lyrics var
                while (moreLyrics) {
                    // Next line of data is then assigned to nextLine 
                    nextLine = inputFile.nextLine();
                    if (nextLine.startsWith("\"")) {
                        str.append(nextLine);
                        moreLyrics = false;
                        // If nextLine contains only a single quotation then
                        // all lyrics have been appended and the loop ends
                    } else {
                        // append str with next line of lyrics
                        str.append(nextLine).append("\n");
                    }
                }
                lyrics = str.toString(); 
                // Assing lyrics var to the substring of data between quotations
                lyrics = lyrics.substring(lyrics.indexOf("\""),
                        lyrics.lastIndexOf("\"") + 1);
            }
            // create new Song object and add it to 
            Song song = new Song(artist, title, lyrics);
            songList.add(song);
        }
        // set size of Songs array equal to songList ArrayList
        songs = new Song[songList.size()]; 
        songList.toArray(songs); // push Song objects to songs array

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

        // show song count and first 10 songs
        Song[] list = sc.getAllSongs();
        System.out.println("Total songs: " + list.length + ", first songs:");
        Stream.of(list).limit(10).forEach(System.out::println);
    }
}
