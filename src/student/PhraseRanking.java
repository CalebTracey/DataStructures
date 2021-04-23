/**
 * PhraseRanking.java
 *****************************************************************************
 *                       revision history
 *****************************************************************************
 * 4.2021 - Caleb Tracey & Abdikarim Jimale - Collaborated to implement the 
 * class and all methods.
 *          
 *****************************************************************************
 * Search By Lyrics Words allows the user to search the database of songs by
 * individual lyrics
 *
 */
package student;

import java.util.TreeMap;

/**
 *
 * @author calebtracey
 */
public class PhraseRanking {


    static int findExactWord(String word, int pos, String lyrics) {
        int wordStart = lyrics.indexOf(word, pos);
        if (wordStart == -1) {
            return -1;
        }
        int wordEnd = wordStart + word.length();
        boolean isFrontSpace = false;
        boolean isBackSpace = false;
        boolean found = isFrontSpace && isBackSpace;
        if (wordStart != -1 && wordEnd != -1) {
            isFrontSpace = Character.isWhitespace(wordEnd + 1);
            isBackSpace = Character.isWhitespace(wordStart - 1);
            if (!isFrontSpace || !isBackSpace) {
                found = false;
            }
            while (wordStart > 0 && wordEnd < lyrics.length() && !found) {
                if (Character.isLetter(lyrics.charAt(wordEnd))) {
                    pos = wordStart + 1;
                    isFrontSpace = false;
                } else {
                    isFrontSpace = true;
                }
                if (Character.isLetter(lyrics.charAt(wordStart - 1))) {
                    pos = wordEnd;
                    isBackSpace = false;
                } else {
                    isBackSpace = true;
                }
                if (isFrontSpace && isBackSpace) {
                    found = true;
                } else {
                    wordStart = lyrics.indexOf(word, pos);
                    wordEnd = wordStart + word.length();
                }
            }
        }
        return wordStart;
    }

    static int rankPhrase(String lyrics, String lyricsPhrase) {
        int startPos = 0;
        int endPos = 0;
        String lowLyrics = lyrics.toLowerCase();
        String phrase = lyricsPhrase.toLowerCase();
        String lyricReplace = lowLyrics
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("[^a-zA-Z ]", " ")
                .trim().replaceAll("\\s+", " ");
        String phraseReplace = phrase
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("[^a-zA-Z ]", " ")
                .trim().replaceAll("\\s+", " ");
        
        int maxInt = lyricReplace.length();
        int bestRank = maxInt;
        int currentRank = maxInt;
        if (lyricReplace.contains(phraseReplace)) {
            return phraseReplace.length();
        }
        String[] words = phraseReplace.split(" ");
        if (words.length == 0) {
            return -1;
        }
        startPos = findExactWord(words[0], 0, lyricReplace);
        if (startPos == -1) {
            return -1;
        }
        boolean foundPhrase = false;
        while (startPos != -1) {
            endPos = startPos + words[0].length();
            for (int i = 1; i < words.length && endPos >= 0; i++) {
                endPos = findExactWord(words[i], endPos, lyricReplace);
                if (endPos >= 0) {
                    endPos += words[i].length();
                }
            }
            currentRank = endPos - startPos;
            if (currentRank > 0 && currentRank < bestRank) {
                bestRank = currentRank;
                foundPhrase = true;
            }
            startPos = findExactWord(words[0], startPos + 1, lyricReplace);
        }
        if (foundPhrase) {
            return bestRank;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        SongCollection sc = new SongCollection(args[0]);
        SearchByLyricsWords sblw = new SearchByLyricsWords(sc);
        TreeMap<String, TreeMap> songsRanked = new TreeMap<>();
        Song[] searchSongs = sblw.search(args[1].toLowerCase());
        int size = 0;

        for (Song song : searchSongs) {
            int rank = rankPhrase(song.getLyrics(), args[1]);
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
        size = songsRanked.values().stream().map((m) -> 
                m.size()).reduce(size, Integer::sum);
        System.out.println("\nSEARCH TERMS = " + args[1]);
        System.out.println("RESULTS = " + size + "\n");
        for (String key : songsRanked.keySet()) {
            TreeMap<Song, Integer> artistSongs = 
                    new TreeMap<>(songsRanked.get(key));
            artistSongs.keySet().forEach((song) -> {
                System.out.println(artistSongs
                        .get(song) + "  " + song.toString());
            });
        }
    }
}
