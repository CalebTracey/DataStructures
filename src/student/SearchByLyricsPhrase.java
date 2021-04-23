package student;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

public class SearchByLyricsPhrase {

    private static SearchByLyricsWords sblw;
    private TreeMap<String, TreeSet<Song>> lyricsWordMap = new TreeMap<>();
    TreeMap<String, TreeMap<Integer, Song>> songsRanked = new TreeMap<>();
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
                    TreeMap<Integer, Song> newArtist = new TreeMap<>();
                    newArtist.put(rank, song);
                    songsRanked.put(artist, newArtist);
                } else {
                    songsRanked.get(artist).put(rank, song);
                }
            }
        }
    }

    public Song[] search(String lyricsPhrase) {
        String phraseReplace = lyricsPhrase.replace("\n", " ").replace("\r", " ").replaceAll("[^a-zA-Z ]", " ").trim()
                .replaceAll("\\s+", " ");
        Song[] rankedArray = new Song[0];
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

        if (!songsRanked.isEmpty()) {

            for (String artist : songsRanked.keySet()) {
                for (Integer rank : songsRanked.get(artist).keySet()) {
                    rankedArray = Arrays.copyOf(rankedArray, rankedArray.length + 1);
                    rankedArray[rankedArray.length + 1] = songsRanked.get(artist).get(rank);
                }
            }
        }
        return rankedArray;
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
            int printed = 0;
            for (int i = 0; i < 10; i++) {
                System.out.printf("%d  %s", PhraseRanking.rankPhrase(res[i].getLyrics(), args[1]));
            }
            // System.out.println("------- ");
            // Stream.of(res).limit(10).forEachOrdered(PhraseRanking.rankPhrase(t.getLyrics(),
            // phrase));
            // .forEach(System.out::println);

        }
    }
}