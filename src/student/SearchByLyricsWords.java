/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 *
 * @author calebtracey
 */
public class SearchByLyricsWords {

    private TreeMap<String, TreeSet> lyricsWordMap = new TreeMap<>();
    private TreeSet<String> commonWordSet;
    private Song[] songs;
    private String commonWordsRaw
            = "the of and a to in is you that it he for was on"
            + " are as with his they at be this from I have or"
            + " by one had not but what all were when we there"
            + " can an your which their if do will each how them"
            + " then she many some so these would into has more"
            + " her two him see could no make than been its now"
            + " my made did get our me too";

    public SearchByLyricsWords(SongCollection sc) {
        songs = sc.getAllSongs();
        buildCommonWordSet(commonWordsRaw);
        buildLyricsWordMap(songs);

    }

    private void buildCommonWordSet(String cwr) {
        this.commonWordSet
                = new TreeSet<>(Arrays.asList(cwr.split("[^a-zA-Z]+")));
    }

    private TreeSet<String> buildLyricsSet(String lyrics, TreeSet<String> cws) {
        TreeSet<String> retVal
                = new TreeSet<>(Arrays.asList(lyrics.split("[^a-zA-Z]+")));
        retVal.removeAll(cws); // difference
        return retVal;
    }

    private TreeSet<Song> buildSongSet(Song song) {
        TreeSet<Song> retVal = new TreeSet<>();
        retVal.add(song);
        return retVal;
    }

    private void buildLyricsWordMap(Song[] songs) {
        for (Song song : songs) {
            TreeSet<String> lyricsSet
                    = buildLyricsSet(song.getLyrics().toLowerCase(),
                            commonWordSet);
            for (String word : lyricsSet) {
                if (word.length() > 1 && lyricsWordMap.containsKey(word)) {
                    lyricsWordMap.get(word).add(song);
                } else if (word.length() > 1
                        && !lyricsWordMap.containsKey(word)) {
                    TreeSet<Song> songSet = buildSongSet(song);
                    lyricsWordMap.put(word, songSet);
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        int n = 0;
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
        
        for (String word : sblw.lyricsWordMap.keySet()) {
            n += sblw.lyricsWordMap.get(word).size();
            if (sblw.lyricsWordMap.get(word).size() == 0){
                System.out.println(sblw.lyricsWordMap.get(word) + word);
            }
        }
        
        System.out.println("Number of keys in map (K):"
                + sblw.lyricsWordMap.size());
        System.out.println("Total mapped song references (N):" + n);
        
        //System.out.println(sblw.lyricsWordMap.values());
        if (args.length > 1) {
            System.out.println("searching for: " + args[1] + "\n");
            //Song[] byTitleResult = sblw.search(args[1]);

            // to do: show first 10 matches
//            System.out.println("Total songs: " + byTitleResult.length
//                    + ", first songs:");
//            Stream.of(byTitleResult).limit(10).forEach(System.out::println);
        }
    }

}
