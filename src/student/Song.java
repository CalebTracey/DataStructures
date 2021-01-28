/*
 * 1.2021 - Caleb Tracey & Abdikarim Jimale - Collaborated to impliment 
 *   functionallity for the Song class with variables and a constructor. Also 
 *   compareTo, toString, and accessor methods.
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 - Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods for part 1, just implement the ones that are 
 * here.
 */
package student;

import java.util.*;

/**
 *
 * @author calebtracey
 */
public class Song implements Comparable<Song> {
    // fields
    private String artist;
    private String title;
    private String lyrics;

    /**
     * Parameterized constructor
     *
     * @param artist the author of the song
     * @param title the title of the song
     * @param lyrics the lyrics as a string with linefeeds embedded
     */
    public Song(String artist, String title, String lyrics) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
    }

    /**
     * Accessor for artist
     * 
     * @return artist
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * Accessor for lyrics
     * 
     * @return lyrics
     */
    public String getLyrics() {
        return this.lyrics;
    }

    /**
     * Accessor for title
     * 
     * @return title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * returns name and title ONLY
     *
     * @return a string representing the artist and title only
     */
    @Override
    public String toString() {
        return this.artist + ", \"" + this.title + "\"";
    }

    /**
     * the default comparison of songs primary key: artist, secondary key: title
     * used for sorting and searching the song array if two songs have the same
     * artist and title they are considered the same
     *
     * @param song2
     * @return a negative number, positive number or 0 depending on whether this
     * song should be before, after or is the same. Used for a "natural" sorting
     * order. In this case first by author then by title so that the all of an
     * artist's songs are together, but in alpha order. Follow the given
     * example.
     */
    public int compareTo(Song song2) {
        // int variables that hold values for artist and title comparison 
        int artistVal = this.artist.compareToIgnoreCase(song2.getArtist());
        int titleVal = this.title.compareToIgnoreCase(song2.getTitle());
        // named constants for clarity
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        
        if (song2 == null) // Optimization in case of null object
            return AFTER;

        if (artistVal != 0) {
            if (artistVal > 0) {
                return AFTER;
            }
            if (artistVal < 0) {
                return BEFORE;
            }
        }
        if (artistVal == 0 && titleVal != 0) {
            if (titleVal > 0) {
                return AFTER;
            }
            if (titleVal < 0) {
                return BEFORE;
            }
        }
        return EQUAL;
    }
    
    public static class CmpArtist implements Comparator<Song> {
        public int compare(Song s1, Song s2) {
            return s1.getArtist().compareToIgnoreCase(s2.getArtist());
        }
    }

    /**
     * testing method to unit test this class
     *
     * @param args
     */
    public static void main(String[] args) {
        Song s1 = new Song("Professor B",
                "Small Steps",
                "Write your programs in small steps\n"
                + "small steps, small steps\n"
                + "Write your programs in small steps\n"
                + "Test and debug every step of the way.\n");

        Song s2 = new Song("Brian Dill",
                "Ode to Bobby B",
                "Professor Bobby B., can't you see,\n"
                + "sometimes your data structures mystify me,\n"
                + "the biggest algorithm pro since Donald Knuth,\n"
                + "here he is, he's Robert Boothe!\n");

        Song s3 = new Song("Professor B",
                "Debugger Love",
                "I didn't used to like her\n"
                + "I stuck with what I knew\n"
                + "She was waiting there to help me,\n"
                + "but I always thought print would do\n\n"
                + "Debugger love .........\n"
                + "Now I'm so in love with you\n");

        System.out.println("testing getArtist: " + s1.getArtist());
        System.out.println("testing getTitle: " + s1.getTitle());
        System.out.println("testing getLyrics:\n" + s1.getLyrics());

        System.out.println("testing toString:\n");
        System.out.println("Song 1: " + s1);
        System.out.println("Song 2: " + s2);
        System.out.println("Song 3: " + s3);

        System.out.println("testing compareTo:");
        System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
        System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
        System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
        System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
        System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
        
        Song.CmpArtist testCmpArtist = new Song.CmpArtist();
        
        System.out.println("testing CmpArtist:");
        System.out.println("Song1 vs Song2 = " + testCmpArtist.compare(s1, s2));
        System.out.println("Song2 vs Song1 = " + testCmpArtist.compare(s2, s1));
        System.out.println("Song1 vs Song3 = " + testCmpArtist.compare(s1, s3));
        System.out.println("Song3 vs Song1 = " + testCmpArtist.compare(s3, s1));
        System.out.println("Song1 vs Song1 = " + testCmpArtist.compare(s1, s1));
    }
}
