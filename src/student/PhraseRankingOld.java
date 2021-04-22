/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author calebtracey
 */
public class PhraseRankingOld {
//    static int rankPhrase(String lyrics, String lyricsPhrase){
//        //This takes in song lyrics and the phrase we are searching for
//
//   //Start to build the regex
//   String regex = lyricsPhrase.toLowerCase().replaceAll(" ","(.|\\n)*?");
//   System.out.println(regex);
//   //Create the pattern
//    Pattern p = Pattern.compile(regex.toString(), Pattern.DOTALL);
//    Matcher m = p.matcher(lyrics);
//
//    //string for regex match found
//    String regexMatch = "";
//        while(m.find()){
//
//            regexMatch = m.group();
//            System.out.println(regexMatch);
//    }
//
//    return regexMatch.length();
//
//}
//(\\bShe\\b)(?:(?!\\b(?:she|loves|you)\\b).)*(\\bloves\\b)(?:(?!\\b(?:she|loves|you)\\b).)*(\\byou\\b)
    // ^(?=.*\bshe\b)(?=.*\bloves\b)(?=.*\byou\b.*$
    // ^(?=.*\bjack\b)(?=.*\bjames\b)(?=.*\bjason\b)(?=.*\bjules\b).*$

    //(\bshe\b)(?:(?!\b(?:she|loves|you)\b).)(\bloves\b).+?(?:(?!\b(?:she|loves|you)\b).)(\byou\b)
    //(\bshe\b)(?:(?!\b(?:she|loves|you)\b).)(\bloves\b).+?(?:(?!\b(?:she|loves|you)\b).)(\byou\b)
    //(she(?:(?!(?:she|loves|you))).+?(loves).+?(?:(?!(?:she|loves|you))).+?you)
    //(she(?:(?!(?:she|loves|you))).+?loves).+?(?:(?!(?:she|loves|you))).+?you)
    static int rankPhrase(String lyrics, String lyricsPhrase) {

//        StringBuilder regexSkipFirst = new StringBuilder("((?<=" + searchFirst
//                + ")(?:(?!(?:" + searchFirst).append("|");
//        for (int i = 1; i < searchPhrase.length - 1; i++) {
//            regexSkipFirst.append(searchPhrase[i]).append("|");
//        }
//
//        regexSkipFirst.append(searchLast).append("))).+?");
//
//        for (int j = 1; j < searchPhrase.length - 1; j++) {
//            regexSkipFirst.append("(").append(searchPhrase[j]).append(").+?");
//        }
//        regexSkipFirst.append("(?:(?!(?:").append(searchFirst).append("|");
//        for (int k = 1; k < searchPhrase.length - 1; k++) {
//            regexSkipFirst.append(searchPhrase[k]).append("|");
//        }
//        regexSkipFirst.append(searchLast).append("))).+?");
//        regexSkipFirst.append(searchLast).append(")");
//
//        StringBuilder regexSkipLast = new StringBuilder("(" + searchFirst
//                + "(?:(?!(?:" + searchFirst).append("|");
//        for (int i = 1; i < searchPhrase.length - 1; i++) {
//            regexSkipLast.append(searchPhrase[i]).append("|");
//        }
//
//        regexSkipLast.append(searchLast).append("))).+?");
//
//        for (int j = 1; j < searchPhrase.length - 1; j++) {
//            regexSkipLast.append("(").append(searchPhrase[j]).append(").+?");
//        }
//        regexSkipLast.append("(?:(?!(?:").append(searchFirst).append("|");
//        for (int k = 1; k < searchPhrase.length - 1; k++) {
//            regexSkipLast.append(searchPhrase[k]).append("|");
//        }
//        regexSkipLast.append(searchLast).append("))).+?");
//        regexSkipLast.append("(?=").append(searchLast).append("))");
//String[] searchPhrase = lyricsPhrase.toLowerCase().split(" ");
//        String searchFirst = searchPhrase[0];
//        String searchLast = searchPhrase[searchPhrase.length - 1];
//        // (\b)(?=cow).*?(?=milk)(\b)
//        StringBuilder regex = new StringBuilder("(" + searchFirst
//                + "(?:(?!(?:" + searchFirst).append("|");
//        for (int i = 1; i < searchPhrase.length - 1; i++) {
//            regex.append(searchPhrase[i]).append("|");
//        }
//
//        regex.append("(" + searchLast + "))).+?");
//
//        for (int j = 1; j < searchPhrase.length - 1; j++) {
//            regex.append(searchPhrase[j]).append(").+?");
//        }
//        regex.append("(?:(?!(?:").append(searchFirst).append("|");
//        for (int k = 1; k < searchPhrase.length - 1; k++) {
//            regex.append(searchPhrase[k]).append("|");
//        }
//        regex.append(searchLast).append("))).+?");
//        regex.append(searchLast).append(")");
//
//        Pattern p = Pattern.compile(regex.toString());
//        Matcher m = p.matcher(lyrics.toLowerCase());
//        Pattern pFirst = Pattern.compile(regexSkipFirst.toString());
//
//        Pattern pLast = Pattern.compile(regexSkipLast.toString());
        TreeMap<String, Integer[]> posMap = new TreeMap();
        String lyricsLower = lyrics.toLowerCase();
        String phraseLower = lyricsPhrase.toLowerCase();
        //String[] lyricsWords = lyricsLower.split("\\W+");
        //String[] lyricsArr = lyricsLower.split(" ");
        String[] searchPhrase = phraseLower.split("^|\\s");
        String[] lyricsArr = lyricsLower.split("^|\\s");
        String firstPhrase = searchPhrase[0];
        String lastPhrase = searchPhrase[searchPhrase.length - 1];
        TreeSet<String> phraseSet = new TreeSet<>(Arrays.asList(phraseLower.split("^|\\s")));
        TreeSet<String> lyricsSet = new TreeSet<>(Arrays.asList(lyricsLower.split("^|\\s")));
        TreeSet<String> matches = new TreeSet<>();
        TreeSet<Integer> firstInstances = new TreeSet<>();
        TreeSet<Integer> lastInstances = new TreeSet<>();

        for (int i = 0; i < lyricsArr.length; i++) {
            if (lyricsArr[i].equals(firstPhrase)) {
                firstInstances.add(lyrics.indexOf(lyricsArr[i]));
            }
            if (lyricsArr[i].equals(lastPhrase)) {
                lastInstances.add(lyrics.indexOf(lyricsArr[i]));
            }
        }

        TreeMap<Integer, Integer> subIds = new TreeMap<>();
        firstInstances.removeIf(instance -> instance > lastInstances.first());
        lastInstances.removeIf(instance -> instance < firstInstances.last());

        for (Integer i : firstInstances) {
            String sub = lyricsLower.substring(i);
            TreeSet<String> subString = new TreeSet<>(Arrays.asList(sub.split("^|\\s")));
            if (subString.containsAll(phraseSet)) {
                TreeMap<Integer, String> orderCheck = new TreeMap<>();
                for (String word : searchPhrase) {
                    orderCheck.put(sub.indexOf(word), word);
                }
                if (Arrays.equals(searchPhrase, orderCheck.values().toArray())) {
                    matches.add(sub);
                }
            }
        }
        Arrays.asList(phraseLower.split("^|\\s")).forEach((word) -> {
            Integer[] posArray = new Integer[0];
            posMap.put(word, posArray);
        });

        for (int i = 0; i < firstInstances.size(); i++) {

        }
        int phraseLength = posMap.keySet().size();
        int length = lyricsArr.length;
        posMap.keySet().forEach((key) -> {
            for (int i = 0; i < length; i++) {
                int valLength = posMap.get(key).length;
                Integer[] wordVals
                        = Arrays.copyOf(posMap.get(key), valLength + 1);

                if (lyricsArr[i].equals(key)) {
                    if (wordVals.length == 1 && wordVals[0] == null) {
                        wordVals[0] = i;
                        posMap.put(key, wordVals);
                    } else {
                        wordVals[wordVals.length - 1] = i;
                        posMap.put(key, wordVals);
                    }
                }
            }
        });

        for (Integer f : firstInstances) {

        }
        // (\b)(?=cow).*?(?=milk)(\b)
        //(\b)(?=cow).*?(?=happy).*?(!?milk)(\b)
        StringBuilder regex = new StringBuilder("\\b");
        for (int i = 0; i < searchPhrase.length - 1; i++) {
            regex.append("(?=")
                    .append(searchPhrase[i]).append(").*?");
        }
        regex.append("(!?").append(lastPhrase).append(")(\\b)");

        //String testRegex = "^\\b\\b+$";
        String testRegex = "(?smi)^\\b" + firstPhrase + "\\b.+?^" + lastPhrase + "]";
        Pattern p = Pattern.compile(regex.toString(),
                Pattern.DOTALL);

        Matcher m = p.matcher(lyricsLower);

        //int end = lyricsArr.length;
        //int length = lyrics.toLowerCase().length();
        //ArrayList<Integer> ss = new ArrayList<>(Arrays.asList(posMap.get(firstPhrase)));
        //ArrayList<Integer> end = new ArrayList<>(Arrays.asList(posMap.get(lastPhrase)));
        TreeMap<Integer, String> matchMap = new TreeMap<>();
        Integer[] end = (posMap.get(lastPhrase));
        Integer[] start = (posMap.get(firstPhrase));
        Collections.reverse(Arrays.asList(end));
        if (lyricsSet.containsAll(phraseSet)) {
            for (int i = 0; i < start.length; i++) {
                for (int j = 0; j < end.length; j++) {

                    if (start[i] < end[j]) {
                        m.region(start[i], end[j]);
                        if (m.find()) {
                            String match = m.group();
                            //System.out.printf("Match found: \"%s\" at position [%d, %d)%n",
                            //m.group(), startIndex, endIndex);
                            //ArrayList<String> subArray = new ArrayList<>(Arrays.asList(match.split(" ")));
                            //  if match.contains()
                            if (!matchMap.containsKey(match.length())) {
                                matchMap.put(match.length(), match);
                            }
                        }
                    }
                }
            }

        }
        ArrayList<Integer> validRanks = new ArrayList<>();
        if (!matchMap.isEmpty()) {
            for (Integer key : matchMap.keySet()) {
                TreeSet<String> valueSet = new TreeSet<>(Arrays.asList(matchMap.get(key).split(" ")));
                if (valueSet.containsAll(phraseSet)) {
                    validRanks.add(key);
                }
            }

        }
        if (!validRanks.isEmpty()) {
            return validRanks.get(0);
        }
        //start = Arrays.asList(posMap.get(posMap.firstKey())).toArray();
//        int end = lyricsLower.lastIndexOf(lastPhrase);
//        if (start < end) {
//            for (int i = start; i < end; ++i) {
//                for (int j = end; j <= end; ++j) {
//                    if (i < j) {
//                        m.region(i, j);
//                        start = lyricsLower.indexOf(firstPhrase, i);
//                            end = lyricsLower.lastIndexOf(lastPhrase, j);
//
//                        if (m.find()) {
//                            
//                            System.out.printf("Match found: \"%s\" at position [%d, %d)%n",
//                                    m.group(), i, j);
//                        }
//                    }
//                }
//            }
//        }
//        while (nextMatch) {
//            if (m.find()) {
//                String match = m.group();
//                matchFinal = match.length();
//                Matcher mFirst = pFirst.matcher(match);
//                Matcher mLast = pLast.matcher(match);
//
//                if (mFirst.find()) {
//                    match = mFirst.group();
//                    m = p.matcher(match);
//                    if (m.find()) {
//                        m = p.matcher(m.group());
//                    } else if (mLast.find()) {
//                        match = mLast.group();
//                        m = p.matcher(match);
//                        if (m.find()) {
//                            m = p.matcher(m.group());
//                        }
//                    } else {
//                        matchFinal = match.length();
//                        nextMatch = false;
//                    }
//                    // System.out.println("****" + match + "****");
//                }
//            } else {
//                nextMatch = false;
//            }
//        }
        return -1;

    }
    //"(?<=\\s|^|\\b)(?:[-'.%$#&/]\\b|\\b[-'.%$#&/]|[A-Za-z0-9]|\\(‌​[A-Za-z0-9]+\\))+(?=‌​\\s|$|\\b)"
    //(?:^|\W)material(?:$|\W)
    //    static int rankPhrase(String lyrics, String lyricsPhrase) {
    //
    //        TreeMap<String, Integer[]> posMap = new TreeMap();
    //        String lyricsLower = lyrics.toLowerCase();
    //        String phraseLower = lyricsPhrase.toLowerCase();
    //        //String[] lyricsWords = lyricsLower.split("\\W+");
    //        //String[] lyricsArr = lyricsLower.split(" ");
    //        String[] phraseArr = phraseLower.split("^|\\W");
    //        Arrays.asList(phraseLower.split("^|\\W")).forEach((word) -> {
    //            Integer[] posArray = new Integer[0];
    //            posMap.put(word, posArray);
    //        });
    //
    //        String regex = lyricsLower.replaceAll(" ", "(.|\\n)*?");
    //
    //        //System.out.println(regex);
    //        //Create the pattern
    //        Pattern p = Pattern.compile(regex.toString(), Pattern.DOTALL);
    //        Matcher m = p.matcher(lyrics);
    //
    //        int phraseLength = phraseArr.length;
    //        TreeSet<String> phraseSet = new TreeSet<>(Arrays.asList(phraseLower.split("^|\\W")));
    //        TreeSet<String> lyricsSet = new TreeSet<>(Arrays.asList(lyricsLower.split("^|\\W")));
    //        //phraseSet.addAll(Arrays.asList(phraseArr));
    //        //lyricsSet.addAll(Arrays.asList(lyricsArr));
    //        TreeMap<Integer, String> matchMap = new TreeMap<>();
    //
    //        if (lyricsSet.containsAll(phraseSet)) {
    //            String firstWord = phraseArr[0];
    //            String lastWord = phraseArr[phraseLength - 1];
    //            int first = lyricsLower.indexOf(firstWord);
    //            int last = lyricsLower.lastIndexOf(lastWord);
    //            last += lastWord.length();
    //
    //            boolean firstLast = true;
    //            if (first == -1 || last == -1) {
    //                firstLast = false;
    //            }
    //
    //            while (firstLast) {
    //                if (first < last) {
    //                    String subString = lyricsLower.substring(first, last);
    //                    TreeSet<String> subSet
    //                            = new TreeSet<>(Arrays.asList(subString.split("^|\\W")));
    //                    if (subSet.containsAll(phraseSet)) {
    //                        TreeMap<Integer, String> orderCheck = new TreeMap<>();
    //                        for (String word : phraseArr) {
    //                            orderCheck.put(subString.indexOf(word), word);
    //                        }
    //                        if (Arrays.equals(phraseArr, orderCheck.values().toArray())) {
    //                            matchMap.put(subString.length(), subString);
    //                        }
    //                        first = lyricsLower.indexOf(firstWord, first + 1);
    //                        last = lyricsLower.lastIndexOf(lastWord, last - 1);
    //                        if (first == -1 || last == -1) {
    //                            firstLast = false;
    //                        }
    //                        subSet.clear();
    //
    //                    }
    //                } else {
    //                    first = lyricsLower.indexOf(firstWord, first + 1);
    //                    last = lyricsLower.lastIndexOf(lastWord, last - 1);
    //                    if (first == -1 || last == -1) {
    //                        firstLast = false;
    //                    }
    //                }
    //
    //            }
    //
    //            if (!matchMap.isEmpty()) {
    //                int bestRank = matchMap.firstKey();
    //                return bestRank;
    //                //System.out.println("************" + bestRank + "  " + matchMap.get(bestRank) + "*************");
    //            }
    //
    //        }
    ////        int phraseLength = posMap.keySet().size();
    ////        int length = lyricsWords.length;
    ////        posMap.keySet().forEach((word) -> {
    ////            for (int i = 0; i < length; i++) {
    ////                int valLength = posMap.get(word).length;
    ////                Integer[] wordVals
    ////                        = Arrays.copyOf(posMap.get(word), valLength + 1);
    ////
    ////                if (lyricsArr[i].equals(word)) {
    ////                    if (wordVals.length == 1 && wordVals[0] == null) {
    ////                        wordVals[0] = i;
    ////                        posMap.put(word, wordVals);
    ////                    } else {
    ////                        wordVals[wordVals.length - 1] = i;
    ////                        posMap.put(word, wordVals);
    ////                    }
    ////                }
    ////            }
    ////        });
    ////        Integer[] subStringIndex = new Integer[phraseLength];
    ////        Iterator<String> itr = posMap.keySet().iterator();
    ////        int currVal = -1;
    ////
    ////        for (int i = 0; i < phraseLength; i++) {
    ////            if (itr.hasNext()) {
    ////                String currentKey = itr.next();
    ////                Integer[] currArr = posMap.get(currentKey);
    ////                if (itr.hasNext()) {
    ////                    String nextKey = itr.next();
    ////                    Integer[] nextArr = posMap.get(nextKey);
    ////                    if (currArr.length != 0) {
    ////                        for (int j = 0; j < currArr.length; j++) {
    ////                            currVal = currArr[j];
    ////                            //if (itr.hasNext()) {
    ////
    ////                            if (nextArr.length != 0 && currVal != -1) {
    ////                                int nextVal = nextArr[0];
    ////
    ////                                if (currVal > nextVal) {
    ////                                    if (j!=0){
    ////                                    currVal = currArr[j - 1];
    ////                                    }
    ////                                }
    ////                                subStringIndex[j] = currVal;
    ////                            }
    ////                            // }
    ////                        }
    ////                    }
    ////                }
    ////            } else if (i == phraseLength - 1) {
    ////                Integer[] lastArr = posMap.get(last);
    ////
    ////                for (int j = lastArr.length - 1; j > 0; j--) {
    ////                    if (lastArr.length != 0) {
    ////                        int lastVal = lastArr[j];
    ////                        if (lastVal < currVal) {
    ////                            lastVal = lastArr[j];
    ////                            subStringIndex[i] = lastVal;
    ////                        }
    ////                    }
    ////
    ////                }
    ////            }
    ////        }
    ////        //} else if ()
    ////        StringBuilder str = new StringBuilder();
    ////        boolean allMatch = true;
    ////        for (Integer num : subStringIndex) {
    ////            if (num == null) {
    ////                allMatch = false;
    ////            }
    ////        }
    ////        if (allMatch) {
    ////            for (int i = subStringIndex[0]; i < subStringIndex[subStringIndex.length - 1]; i++) {
    ////                str.append(lyricsArr[i]);
    ////            }
    ////            for (int i = 0; i < phraseLength; i++) {
    ////                int index = subStringIndex[i];
    ////                if (i < phraseLength - 2) {
    ////                    while (index < subStringIndex[i + 1]) {
    ////                        str.append(lyricsArr[index]);
    ////                    }
    ////                } else if (i == phraseLength - 1) {
    ////                    while (index < subStringIndex[phraseLength - 1]) {
    ////                        str.append(lyricsArr[index]);
    ////                    }
    ////                }
    ////            }
    //        // }
    //        //System.out.println(str.length() + "    " + str.toString());
    //        //if (index <= phraseLength) {
    //        //}
    //        //   ++) {
    ////            if (phraseMap.containsKey(lyricsWords[i])) {
    ////                for ()
    ////                int index = phraseMap.get(lyricsWords[i]);
    //////                for (String word : phraseMap.keySet()){
    //////                    if (word.equals(lastWord)){
    //////                        
    //////                    }
    //////                }
    ////                if (index == -1) {
    ////                    count++;
    ////                    if (i < phraseMap){
    ////                    phraseMap.put(lyricsWords[i], i);
    ////                    }
    ////                    if (count == length) {
    ////                        int min = Integer.MAX_VALUE;
    ////                        for (Map.Entry<String, Integer> m
    ////                                : phraseMap.entrySet()) {
    ////                            int val = m.getValue();
    ////                            if (val < min) {
    ////                                min = val;
    ////                            }
    ////                        }
    ////                        int s = i - min;
    ////                        if (s < len_sub) {
    ////                            local_start = min;
    ////                            local_end = i;
    ////                            len_sub = s;
    ////                        }
    ////                    }
    ////
    ////                } 
    ////             
    ////            }
    ////        }
    ////           String[] original_parts = lyrics.split(" ");
    ////        for (int i = local_start; i <=local_end; i++){
    ////            System.out.print(original_parts[i] + " ");
    ////        }
    ////        return original_parts.length;
    //        // }
    ////        String first = phraseArray[0];
    ////        String last = phraseArray[phraseArray.length - 1];
    ////        String subMatch = "";
    ////        TreeSet<String> searchPhrase
    ////                = new TreeSet<>(Arrays.asList(phraseArray));
    ////        TreeSet<String> lyricsSet
    ////                = new TreeSet<>(Arrays.asList(lyrics.toLowerCase().split("\\s")));
    ////        TreeMap<Integer, String> substringMatches = new TreeMap<>();
    ////        int retVal = -1;
    ////        if (lyricsSet.containsAll(searchPhrase)) {
    ////            String[] matchOrder = new String[phraseArray.length];
    ////            int firstIndex = lyrics.indexOf(first);
    ////            int lastIndex = lyrics.indexOf(last, firstIndex);
    ////            boolean moreMatches = true;
    ////            lastIndex += last.length();
    ////
    ////            /**
    ////             * IDEA: CREATE TWO INT ARRAYS. ONE EACH FOR FIRST/LAST NEXT INDEXES
    ////             * WHILE THE .HAVENEXT() RUN THROUGH WHILE LOOPS. CREATE LIST OF SUBSTRINGS
    ////             * FOR ALL COMBINATIONS, CHECK IF THEY CONTAIN ALL SEARCH PHRASES IN 
    ////             * CORRECT ORDER, THEN TAKE SMALLEST MATCH FOR RANK
    ////             */
    ////            while (moreMatches) {
    ////                if (firstIndex != -1 && lastIndex != -1) {
    ////                    Int[] firstArray = new Int[lyricsSet]
    ////                    if (firstIndex < lastIndex) {
    ////                        subMatch = lyrics.toLowerCase()
    ////                                .substring(firstIndex, lastIndex);
    ////                        int nextFirst = subMatch.indexOf(first, first.length());
    ////                        int nextLast = subMatch.indexOf(last, subMatch.length() - last.length());
    ////
    ////                        if (nextFirst == -1 && nextLast == -1) {
    ////                            for (int i = 0; i < phraseArray.length; i++) {
    ////                                if (subMatch.contains(phraseArray[i])) {
    ////                                    matchOrder[i] = phraseArray[i];
    ////                                }
    ////                            }
    ////                            if (Arrays.equals(matchOrder, phraseArray)) {
    ////                                substringMatches.put(subMatch.length(), subMatch);
    ////
    ////                            }
    ////                        }
    ////                        while (nextFirst != -1 || nextLast != -1) {
    ////                            TreeSet<String> phraseCheck = new TreeSet<>();
    ////                            while (nextFirst == -1 && nextLast != -1) {
    ////                                String nextSubMatch = subMatch.substring(0, nextLast);
    ////                                for (int i = 0; i < phraseArray.length; i++) {
    ////                                    if (nextSubMatch.contains(phraseArray[i])) {
    ////                                        matchOrder[i] = phraseArray[i];
    ////                                    }
    ////                                }
    ////                                if (Arrays.equals(matchOrder, phraseArray)) {
    ////                                    substringMatches.put(nextSubMatch.length(), nextSubMatch);
    ////                                }
    ////                            }
    ////                            while (nextFirst != -1 && nextLast == -1) {
    ////                                String nextSubMatch = subMatch.substring(nextFirst, subMatch.length());
    ////                                phraseCheck.addAll(Arrays.asList(nextSubMatch));
    ////                                if (phraseCheck.containsAll(searchPhrase)) {
    ////                                    for (int i = 0; i < phraseArray.length; i++) {
    ////                                        if (nextSubMatch.contains(phraseArray[i])) {
    ////                                            matchOrder[i] = phraseArray[i];
    ////                                        }
    ////                                    }
    ////                                } else {
    ////                                    
    ////                                }
    ////                                    if (Arrays.equals(matchOrder, phraseArray)) {
    ////                                        substringMatches.put(nextSubMatch.length(), nextSubMatch);
    ////                                    }
    ////                                }
    ////                                while (nextFirst != -1 && nextLast != -1) {
    ////                                    String nextSubMatch = subMatch.substring(nextFirst, nextLast);
    ////                                    phraseCheck.addAll(Arrays.asList(nextSubMatch));
    ////                                    if (phraseCheck.containsAll(searchPhrase)) {
    ////                                        for (int i = 0; i < phraseArray.length; i++) {
    ////                                            if (nextSubMatch.contains(phraseArray[i])) {
    ////                                                matchOrder[i] = phraseArray[i];
    ////                                            }
    ////                                        }
    ////                                    } else {
    ////                                        moreMatches = false;
    ////                                    }
    ////                                    if (Arrays.equals(matchOrder, phraseArray)) {
    ////                                        substringMatches.put(nextSubMatch.length(), nextSubMatch);
    ////                                    }
    ////                                }
    ////                                moreMatches = false;
    ////                            }
    ////                        
    ////                    }
    ////                    moreMatches = false;
    ////                }
    ////            }
    ////        }
    ////
    ////        if (!substringMatches.isEmpty()) {
    ////            retVal = substringMatches.firstKey();
    ////        }
    ////        return retVal;
    //
    //        return -1;
    //
    //    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        SongCollection sc = new SongCollection(args[0]);
        //SearchByLyricsWords sblw = new SearchByLyricsWords(sc);

        TreeMap<String, TreeMap> songsRanked = new TreeMap<>();
        Song[] songs = sc.getAllSongs();
//        for (Song song : sc.getAllSongs()) {
//            rankPhrase(song.getLyrics(), args[1]);
//            
//        }
        for (Song song : songs) {
            int rank = rankPhrase(song.getLyrics(), args[1]);
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
            //System.out.println(rank);
        }
        //System.out.println(rank);

        System.out.println("\nSEARCH TERMS = " + args[1]);
        System.out.println("RESULTS = " + songsRanked.size() + "\n");
        for (String key : songsRanked.keySet()) {
            TreeMap<Integer, Song> printMap = new TreeMap<>(songsRanked.get(key));
            for (Integer ranks : printMap.keySet()) {

                System.out.println(ranks + "  " + printMap.get(ranks));
            }

        }

    }
}
