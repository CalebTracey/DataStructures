/**
 * SearchByArtistPrefix.java
 *****************************************************************************
 *                       revision history
 *****************************************************************************
 *
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
        
        Song.CmpArtist cmpArtist = new Song.CmpArtist(); //not sure if needed
        ArrayList matchingArtists = new ArrayList(); // temp list to hold matches
        // create new song object with param for searching
        String artistPre = artistPrefix.toLowerCase();
        Song artistSearch = new Song(artistPre, null, null);
        int retVal; //return val for binary search below

        for (int i = songs.length - 1; i > 0; i--) { // walk backwards
            if (songs[i].getArtist().toLowerCase() // find first "startsWith"
                    .startsWith(artistSearch.getArtist())) {
                retVal = Arrays.binarySearch(songs, songs[i]); //find key
                // move forwards through data to find all results containt the
                // param and add to ArrayList
                for (int j = retVal; j < songs.length; j++) {
                    if (songs[j].getArtist().toLowerCase()
                            .contains(artistPre)) {
                        // check to see if the found object is already in list
                        if (!matchingArtists.contains(songs[j])) {
                            matchingArtists.add(songs[j]);
                        }
                    }
                }
            }
        }
        Song[] artistsFound = new Song[matchingArtists.size()]; // set size
        matchingArtists.toArray(artistsFound); // push list to array
        Arrays.sort(artistsFound); // sort them

        return artistsFound;
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
            System.out.println("searching for: " + args[1]);
            Song[] byArtistResult = sbap.search(args[1]);

            // to do: show first 10 matches
            System.out.println("Total songs: " + byArtistResult.length + 
                    ", first songs:");
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}
