/**
 * SearchByArtistPrefix.java
 *****************************************************************************
 *                       revision history
 *****************************************************************************
 * 2.2021 - Caleb Tracey & Dale Das - Collaborated to impliment
 *   functionallity for the search method using binary search to find all
 *   matches for a given parameter.
 * 8/2015 Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 *****************************************************************************
 * Search by Artist Prefix searches the artists in the song database
 * for artists that begin with the input String
 */
package student;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class SearchByArtistPrefix {

    // keep a local direct reference to the song array
    private Song[] songs;

    /**
     * constructor initializes the property. [Done]
     *
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches) converts artistPrefix to lowercase and
     * creates a Song object with artist prefix as the artist in order to have a
     * Song to compare. walks back to find the first "beginsWith" match, then
     * walks forward adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name
     * @return an array of songs by artists with substrings that match the
     * prefix
     */
    public Song[] search(String artistPrefix) {
        Comparator<Song> cmp = new Song.CmpArtist();
        //Song.CmpArtist cmp = new Song.CmpArtist();
        ArrayList matchingArtists = new ArrayList();// temp list to hold matches
        // create new song object with param for searching
        String artistPre = artistPrefix.toLowerCase();
        Song key = new Song(artistPre, "none", "none");

        ((CmpCnt) cmp).resetCmpCnt(); // set counter to 0
        int partLength = artistPre.length();
        // call the Arrays.binarySearch with the array, key, the Song
        int i = Arrays.binarySearch(songs, key, cmp);
        System.out.println(((CmpCnt) cmp).getCmpCnt() + " compares for search."
                + "\n");
        System.out.println("The result of the binary search: " + i);
        int loopCount = 0; // counter for while loops

        if (i < 0) {
            i = -i - 1;
        }
        System.out.println("Index key is: " + i + "\n");
        System.out.println("Artist and title at index " + i + ":" + "\n"
                + songs[i] + "\n");
        System.out.println("Artist for comparison: " + songs[i].getArtist());

        if (i >= 0) {// it should be, but it never hurts to be careful
            // find the front - the first partial match.
            while (i >= 0
                    && songs[i].getArtist().length() >= partLength
                    && songs[i].getArtist().substring(0, partLength).
                            compareToIgnoreCase(artistPre) == 0) {
                i--;
                loopCount++; //counter for the loop
            }
            // we WILL go one too far.
            i++;
            // now fill the list by walking forward
            while (i < songs.length
                    && songs[i].getArtist().length() >= partLength
                    && songs[i].getArtist().substring(0, partLength).
                            compareToIgnoreCase(artistPre) == 0) {
                matchingArtists.add(songs[i]);
                i++;
                loopCount++;
            }
            Song[] artistsFound = new Song[matchingArtists.size()];// set size
            matchingArtists.toArray(artistsFound);// push list to array
            int totalCount = loopCount + ((CmpCnt) cmp).getCmpCnt();
            System.out.println("Total count of comparisons: " + totalCount
                    + "\n");
            return artistsFound;
        } else {
            return null;
        }
    }

    /**
     * testing method for this unit
     *
     * @param args command line arguments set in Project Properties - the first
     * argument is the data file name and the second is the partial artist name,
     * e.g. be which should return beatles, beach boys, beegees, etc.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

        if (args.length > 1) {
            System.out.println("searching for: " + args[1] + "\n");
            Song[] byArtistResult = sbap.search(args[1]);

            // to do: show first 10 matches
            System.out.println("Total songs: " + byArtistResult.length
                    + ", first songs:");
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}
