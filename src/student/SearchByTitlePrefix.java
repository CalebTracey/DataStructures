/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 * @author calebtracey
 */
public class SearchByTitlePrefix {

    //private Song[] songs;
    private Comparator<Song> cmp;
    private RaggedArrayList<Song> ral;

    public SearchByTitlePrefix(SongCollection sc) {
        //songs = sc.getAllSongs();
        cmp = new Song.CmpTitle();
        ral = new RaggedArrayList<>(cmp);
        for (Song song : sc.getAllSongs()) {
            ral.add(song);
        }
    }

    public Song[] search(String titlePrefix) {
        RaggedArrayList<Song> subRal = new RaggedArrayList<>(cmp);
        Song[] matchingTitles;// temp list to hold matches
        String titlePre = titlePrefix.toLowerCase();
        ((CmpCnt) cmp).resetCmpCnt(); // set counter to 0

        char[] temp = titlePre.toCharArray();
        char lastLetter = temp[temp.length - 1];
        lastLetter++;
        temp[temp.length - 1] = lastLetter;
        String newTitlePre = new String(temp);

        Song prefixSong = new Song("none", titlePre, "none");
        Song prefixSong2 = new Song("none", newTitlePre, "none");

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

        if (args.length > 1) {
            System.out.println("searching for: " + args[1] + "\n");
            Song[] byTitleResult = sbtp.search(args[1]);

            System.out.println(
                    "The number of comparisons to build the RaggedArrayList = "
                    + ((CmpCnt) sbtp.cmp).getCmpCnt());

            // to do: show first 10 matches
            System.out.println("Total songs: " + byTitleResult.length
                    + ", first songs:");
            Stream.of(byTitleResult).limit(10).forEach(System.out::println);
        }
    }
}
