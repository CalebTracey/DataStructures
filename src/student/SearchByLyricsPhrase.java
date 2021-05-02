package student;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchByLyricsPhrase {

    private static SearchByLyricsWords sblw;
    private TreeMap<String, TreeSet<Song>> lyricsWordMap = new TreeMap<>();
    TreeMap<String, TreeMap<Song, Integer>> songsRanked = new TreeMap<>();
    String phrase;

    public SearchByLyricsPhrase(SongCollection sc) {
        SearchByLyricsPhrase.sblw = new SearchByLyricsWords(sc);
        buildLyricsWordMap(sc.getAllSongs());
    }

    private TreeSet<String> buildLyricsSet(String lyrics) {
        TreeSet<String> retVal = new TreeSet<>(Arrays.asList(lyrics.split("[^a-zA-Z]+")));
        return retVal;
    }

    private TreeSet<Song> buildSongSet(Song song) {
        TreeSet<Song> retVal = new TreeSet<>();
        retVal.add(song);
        return retVal;
    }

    private void buildLyricsWordMap(Song[] songs) {
        for (Song song : songs) {
            TreeSet<String> lyricsSet = buildLyricsSet(song.getLyrics().toLowerCase());
            for (String word : lyricsSet) {
                if (word.length() > 1 && lyricsWordMap.containsKey(word)) {
                    lyricsWordMap.get(word).add(song);
                } else if (word.length() > 1 && !lyricsWordMap.containsKey(word)) {
                    TreeSet<Song> songSet = buildSongSet(song);
                    lyricsWordMap.put(word, songSet);
                }
            }
        }
    }

    private void rankPhrase(Song[] wordSearch, String phrase) {
        for (Song song : wordSearch) {
            int rank = PhraseRanking.rankPhrase(song.getLyrics(), phrase);
            if (rank != -1) {
                String artist = song.getArtist();
                if (!songsRanked.containsKey(artist)) {
                    TreeMap<Song, Integer> newArtist = new TreeMap<>();
                    newArtist.put(song, rank);
                    songsRanked.put(artist, newArtist);
                } else {
                    songsRanked.get(artist).put(song, rank);
                }
            }
        }
    }

    public Song[] search(String lyricsPhrase) {
        String phraseReplace = lyricsPhrase.replace("\n", " ").replace("\r", " ").replaceAll("[^a-zA-Z ]", " ").trim()
                .replaceAll("\\s+", " ");
        TreeMap<Integer, Song[]> rankedMap = new TreeMap<>();
        TreeSet<Song> lyricSongs = new TreeSet<>();
        TreeSet<String> searchSet = buildLyricsSet(phraseReplace);
        this.phrase = phraseReplace;
        TreeMap<String, TreeSet<Song>> lyricsWordMapCopy = new TreeMap<>();

        lyricsWordMapCopy.putAll(this.lyricsWordMap);
        lyricsWordMapCopy.keySet().retainAll(searchSet);

        if (!lyricsWordMapCopy.keySet().isEmpty()) {
            lyricSongs.addAll(lyricsWordMapCopy.pollFirstEntry().getValue());
        }
        lyricsWordMapCopy.values().forEach((songSet) -> {
            lyricSongs.retainAll(songSet);
        });

        Song[] wordSearch = new Song[lyricSongs.size()];
        int i = 0;
        for (Song song : lyricSongs) {
            wordSearch[i++] = song;
        }

        rankPhrase(wordSearch, phraseReplace);
        // TreeMap<Integer, Song> mergeMap = new TreeMap<>();
        ArrayList<Song> rankedSongs = new ArrayList<>();

        if (!songsRanked.isEmpty()) {
            for (String artist : songsRanked.keySet()) {
                TreeMap<Song, Integer> artistMap = new TreeMap<>(songsRanked.get(artist));
                for (Song song : artistMap.keySet()) {
                    int val = artistMap.get(song);
                    if (!rankedMap.keySet().contains(val)) {
                        Song[] newVal = new Song[1];
                        newVal[0] = song;
                        rankedMap.put(val, newVal);

                    } else {
                        Song[] newVal = Arrays.copyOf(rankedMap.get(val), rankedMap.get(val).length + 1);
                        newVal[rankedMap.get(val).length] = song;
                        rankedMap.put(val, newVal);
                    }
                }
            }
            for (Integer rank : rankedMap.keySet()) {
                rankedSongs.addAll(Arrays.asList(rankedMap.get(rank)));
            }
        }

        return rankedSongs.toArray(Song[]::new);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsPhrase sblp = new SearchByLyricsPhrase(sc);
        TreeMap<Integer, Song> sr = new TreeMap<>();

        // *** Part 10 Testing ***
        if (args.length > 1) {
            Song[] res = sblp.search(args[1]);
            System.out.printf("Search Terms: \"%s\"\n", args[1]);
            System.out.printf("Total Matches: %d\n", res.length);
            System.out.println("--------------------------------------------");
            for (int i = 0; i < 10; i++) {
            System.out.printf("%d %s\n", PhraseRanking.rankPhrase(res[i].getLyrics(),
            args[1]), res[i]);
            }
            // System.out.println("------- ");
            //Stream.of(res).limit(10).forEach(System.out::println);

        }
    }
}