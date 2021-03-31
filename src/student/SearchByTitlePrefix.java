/**
 * SearchByTitlePrefix.java
 *****************************************************************************
 *                       revision history
 *****************************************************************************
 * 3.2021 - Caleb Tracey & Abdikarim Jimale - 
 *      Collaborated to implement this class and all methods
 *****************************************************************************
 * Search by Title Prefix searches the Titles in the song database
 * for titles that begin with the input String
 */
package student;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author calebtracey
 */
public class SearchByTitlePrefix {
    
    private Song[] songs;
    private Comparator<Song> cmp;
    private RaggedArrayList<Song> ral;
    
    public SearchByTitlePrefix(SongCollection sc) {
        songs = sc.getAllSongs();
        Arrays.sort(songs, new Song.CmpTitle()); //sort song list by title
        cmp = new Song.CmpTitle();
        ral = new RaggedArrayList<>(cmp);
        for (Song song : songs) {
            ral.add(song);
        }
    }
    
    public Song[] search(String titlePrefix) {
        // new RAL to hold the sublist of matches
        RaggedArrayList<Song> subRal = new RaggedArrayList<>(cmp);
        Song[] matchingTitles;// Song array of matched to be returned
        String titlePre = titlePrefix.toLowerCase();
        ((CmpCnt) cmp).resetCmpCnt(); // set counter to 0
        // The next 5 lines increment the last char of the prefix for subList
        char[] temp = titlePre.toCharArray();
        char lastLetter = temp[temp.length - 1];
        lastLetter++;
        temp[temp.length - 1] = lastLetter;
        String newTitlePre = new String(temp);
        // dummy songs for subList method
        Song prefixSong = new Song("none", titlePre, "none");
        Song prefixSong2 = new Song("none", newTitlePre, "none");
        // put subList in subRal and then convert to a Song array
        subRal = ral.subList(prefixSong, prefixSong2);
        matchingTitles = new Song[subRal.size];
        subRal.toArray(matchingTitles);
        
        return matchingTitles;
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);
        System.out.println(
                "The number of comparisons to build the RaggedArrayList = "
                + ((CmpCnt) sbtp.cmp).getCmpCnt());
        System.out.println("Count of l1Arrays: " + sbtp.ral.l1NumUsed);
        if (args.length > 1) {
            System.out.println("searching for: " + args[1] + "\n");
            Song[] byTitleResult = sbtp.search(args[1]);

            // to do: show first 10 matches
            System.out.println("Total songs: " + byTitleResult.length
                    + ", first songs:");
            Stream.of(byTitleResult).limit(10).forEach(System.out::println);
        }
    }
}
