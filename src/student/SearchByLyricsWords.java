/**
 * SearchByLyricsWords.java
 *****************************************************************************
 *                       revision history
 *****************************************************************************
 * 4.2021 - Caleb Tracey - implemented the search method and added testing to 
 *          main()
 * 4.2021 - Caleb Tracey & Abdikarim Jimale -
 *          Collaborated to implement this class and all methods
 *****************************************************************************
 * Search By Lyrics Words allows the user to search the database of songs by
 * individual lyrics
 *
 */
package student;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import static java.util.Comparator.comparingInt;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
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
            = " the of and a to in is you that it he for was on\n"
            + " are as with his they at be this from I have or\n"
            + " by one had not but what all were when we there\n"
            + " can an your which their if do will each how them\n"
            + " then she many some so these would into has more\n"
            + " her two him see could no make than been its now\n"
            + " my made did get our me too";

    /**
     * Parameterized constructor for SearchByLyricsWords class.
     *
     * @param sc the collection of songs.
     */
    public SearchByLyricsWords(SongCollection sc) {
        songs = sc.getAllSongs();
        buildCommonWordSet(commonWordsRaw);
        buildLyricsWordMap(songs);
    }

    /**
     * Creates a set of individual common words and assigns it to the
     * commonWordSet variable. Used in buildLyricsSet method for removing common
     * words from lyrics with set difference.
     *
     * @param cwr String of common words
     */
    private void buildCommonWordSet(String cwr) {
        this.commonWordSet
                = new TreeSet<>(Arrays.asList(cwr.split("[^a-zA-Z]+")));
    }

    /**
     * Creates a set of individual words from a String of lyrics then removes
     * any common words. Used by buildLyricsWordMap when creating the map.
     *
     * @param lyrics String of lyrics for an individual song.
     * @return TreeSet of type String of words for lyrics with common words
     * removed.
     */
    private TreeSet<String> buildLyricsSet(String lyrics) {
        TreeSet<String> retVal
                = new TreeSet<>(Arrays.asList(lyrics.split("[^a-zA-Z]+")));
        retVal.removeAll(commonWordSet);
        return retVal;
    }

    /**
     * Creates a new set of type Song when buildLyricsWordMap encounters a word
     * in a song that has not yet been added to the map.
     *
     * @param song the Song in which the un-added lyric was found.
     * @return New TreeSet of type Song with the parameter as the first value.
     */
    private TreeSet<Song> buildSongSet(Song song) {
        TreeSet<Song> retVal = new TreeSet<>();
        retVal.add(song);
        return retVal;
    }

    /**
     * Creates a TreeMap of Strings (words of lyrics) to a TreeSet of Songs
     * (songs that contain the individual lyric).
     *
     * @param songs Array of Songs created from the Song Collection.
     */
    private void buildLyricsWordMap(Song[] songs) {
        for (Song song : songs) {
            // create a new lyrics set for the song
            TreeSet<String> lyricsSet
                    = buildLyricsSet(song.getLyrics().toLowerCase());
            for (String word : lyricsSet) {
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
    
    /**
     * Searches the lyricsWordMap to find all songs that contain all the words 
     * that are being searched for. This method uses set intersection to quickly
     * find the results within the map.
     *  
     * @param lyricsWords the large string of words being searched for
     * @return an array of type Song containing every song that contains all the
     * relevant search terms.
     */
    public Song[] search(String lyricsWords) {
        TreeSet<String> searchSet = buildLyricsSet(lyricsWords.toLowerCase());
        TreeSet<Song> lyricSongs = new TreeSet<>(); // needed for intersection
        
        // build map and use set intersection to retain only the relevant keys
        buildLyricsWordMap(songs);
        this.lyricsWordMap.keySet().retainAll(searchSet);
        
        if (!this.lyricsWordMap.keySet().isEmpty()){
            // if theres a match then add all songs for the remaining key(s) 
            // into the lyricSongs set. Also remove this key from the map.
            lyricSongs.addAll(this.lyricsWordMap.pollFirstEntry().getValue());
        }
        // iterate through any other remaining keys and use set intersection to
        // retain only the songs that include all previous terms as well.
        this.lyricsWordMap.values().forEach((songSet) -> {
            lyricSongs.retainAll(songSet);
        });
        // create new array of type Song for the return value and add all songs 
        // in the lyricSongs array to it.
        Song[] retVal = new Song[lyricSongs.size()];
        int i = 0;
        for (Song song : lyricSongs){
            retVal[i++] = song;
        }
        
        return retVal;
    }

    private static void top10Words(TreeMap<String, TreeSet> lwm) {
        // New map to hold the word and the song count value
        Map<String, Integer> wordCounts = new TreeMap<>();
        int count = 0;
        // Populate new map with words/ song count
        for (String key : lwm.keySet()) {
            wordCounts.put(key, lwm.get(key).size());
        }
        // Create a linked hash map from the wordCounts map so they can be 
        // sorted by value.
        Map<String, Integer> sorted = wordCounts.entrySet().stream()
                .sorted(comparingInt(e -> -e.getValue()))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));
        // Print the first 10 keys and corresponding values from the sorted
        // linked hash map
        for (String s : sorted.keySet()) {
            if (count < 10) {
                System.out.printf("%-5s %6s\n", s, sorted.get(s));
                count++;
            }
        }
    }

    /**
     * Used by main method for testing purposes
     *
     * @param sblw SearchByLyricsWords object
     */
    private static void statistics(SearchByLyricsWords sblw, String[] args) {
        TreeMap<String, TreeSet> lwm = sblw.lyricsWordMap;
        int numKeys = sblw.lyricsWordMap.size();
        int songRefs = 0;
        // Add the size of each set of songs for each word to get the total
        // count of mapped song references
        for (TreeSet<Song> songSet : lwm.values()) {
            songRefs += songSet.size();
        }
        // Average song references per key
        double avgRefs = (double) songRefs / numKeys;
        // Average indexing terms to songs
        double avgTTS = (double) songRefs / sblw.songs.length;
        System.out.printf("Number of keys in map(K):    %-1d\n", numKeys);
        System.out.printf("Total mapped song refs(N):   %-1d\n", songRefs);
        System.out.printf("Average song refs per key:   %-1.3f\n", avgRefs);
        System.out.printf("Average indexing terms/song: %-1.3f\n", avgTTS);
        if (args.length > 1 && args[1].equals("-top10words")) {
            System.out.println("\n(EC) 10 most used words:");
            System.out.println("------------------------");
            System.out.println("Word:   Count:");
            top10Words(sblw.lyricsWordMap);
        }
    }

    /**
     * main method used for testing purposes only
     *
     * @param args AllSongs file
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
        //statistics(sblw, args);
        
        // *** Part 8 Testing ***
        if (args.length > 1 && !args[1].equals("-top10words")) {
            String lyricWords = args[1];

            Song[] res = sblw.search(lyricWords);
            System.out.printf("Search Terms: \"%s\"\n", lyricWords);
            System.out.printf("Total Matches: %d\n", res.length);
            System.out.println("--------------------------------------------");
            Stream.of(res).limit(10).forEach(System.out::println);
        }
    }
}
