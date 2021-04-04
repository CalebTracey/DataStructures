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
            = " the of and a to in is you that it he for was on "
            + " are as with his they at be this from I have or "
            + " by one had not but what all were when we there "
            + " can an your which their if do will each how them "
            + " then she many some so these would into has more "
            + " her two him see could no make than been its now "
            + " my made did get our me too ";

    // Constructor
    public SearchByLyricsWords(SongCollection sc) {
        songs = sc.getAllSongs();
        buildCommonWordSet(commonWordsRaw);
        buildLyricsWordMap(songs);
    }

    // Break up the string of common words by each word and assign
    //each to a set
    private void buildCommonWordSet(String cwr) {
        this.commonWordSet
                = new TreeSet<>(Arrays.asList(cwr.split("[^a-zA-Z]+")));
    }

    // Used by the buildLyricsWordMap method to create a set of words for 
    // an individual song
    private TreeSet<String> buildLyricsSet(String lyrics) {
        TreeSet<String> retVal
                = new TreeSet<>(Arrays.asList(lyrics.split("[^a-zA-Z]+")));
        retVal.removeAll(commonWordSet);
        return retVal;
    }

    // Used by buildLyricsWordMap to create a new set of songs if a new word is
    // found without a matching song
    private TreeSet<Song> buildSongSet(Song song) {
        TreeSet<Song> retVal = new TreeSet<>();
        retVal.add(song);
        return retVal;
    }

    // Builds the map of words to a treeset of songs
    private void buildLyricsWordMap(Song[] songs) {
        for (Song song : songs) {
            // create a new lyrics set for the song
            TreeSet<String> lyricsSet
                    = buildLyricsSet(song.getLyrics().toLowerCase());
            for (String word : lyricsSet) {
                if (allLetters(word)) {
                    // if the word is already a key in the map then get the set of
                    // songs for it and add the new song
                    if (word.length() > 1 && lyricsWordMap.containsKey(word)) {
                        lyricsWordMap.get(word).add(song);
                        // if not a key then create new set with song and add to map
                    } else if (word.length() > 1
                            && !lyricsWordMap.containsKey(word)) {
                        TreeSet<Song> songSet = buildSongSet(song);
                        lyricsWordMap.put(word, songSet);
                    }
                }
            }
        }

    }

    private boolean allLetters(String s) {
        if (s == null) {
            return false;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            // checks whether the character is not a letter
            // if it is not a letter ,it will return false
            if ((Character.isLetter(s.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    private static void statistics(SearchByLyricsWords sblw) {
        int numKeys = sblw.lyricsWordMap.size();
        int songRefs = 0;

        for (TreeSet<Song> songSet : sblw.lyricsWordMap.values()) {
            songRefs += songSet.size();
        }

        double avgRefs = (double) songRefs / numKeys;
        double avgTTS = (double) songRefs / sblw.songs.length;

        System.out.println("Number of keys in map (K): " + numKeys);
        System.out.println("Total mapped song references (N): " + songRefs);
        System.out.printf("average song refs per key: %.3f\n", avgRefs);
        System.out.printf("average indexing terms/song: %.3f\n", avgTTS);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
        statistics(sblw);

        if (args.length > 1) {
            System.out.println("searching for: " + args[1] + "\n");
        }
    }

}
