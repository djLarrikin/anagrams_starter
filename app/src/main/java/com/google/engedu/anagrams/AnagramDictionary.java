package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);

            ArrayList<String> sizeList;
            if (sizeToWord.containsKey(word.length())) {
                sizeList = sizeToWord.get(word.length());
                sizeList.add(word);
                sizeToWord.put(word.length(), sizeList);
            } else {
                sizeList = new ArrayList<>();
                sizeList.add(word);
                sizeToWord.put(word.length(), sizeList);
            }

            String sortedLetters = sortLetters(word);
            ArrayList<String> anagramList;
            if (lettersToWord.containsKey(sortedLetters)) {
                anagramList = lettersToWord.get(sortedLetters);
                anagramList.add(word);
                lettersToWord.put(sortedLetters, anagramList);
            } else {
                anagramList = new ArrayList<>();
                anagramList.add(word);
                lettersToWord.put(sortedLetters, anagramList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        return lettersToWord.get(sortLetters(targetWord));
    }

    /*public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> anagrams;
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String appendedWord = word + alphabet;
            anagrams = getAnagrams(appendedWord);
            if (anagrams != null && anagrams.size() > 0) {
                result.addAll(anagrams);
            }
        }
        return result;
    }*/

    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> anagrams;
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            for (char alphabetj = alphabet; alphabetj <= 'z'; alphabetj++) {
                String appendedWord = word + alphabet + alphabetj;
                anagrams = getAnagrams(appendedWord);
                if (anagrams != null && anagrams.size() > 0) {
                    result.addAll(anagrams);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public String pickGoodStarterWord() {
        while (true) {
            ArrayList<String> limitedWordList = sizeToWord.get(wordLength);
            String testWord = limitedWordList.get(random.nextInt(limitedWordList.size()));
            ArrayList<String> anagrams = getAnagramsWithTwoMoreLetter(testWord);
            int numberOfAnagrams = anagrams.size();

            Log.d("anagrams", "testWord: " + testWord);
            Log.d("anagrams", "number of anagrams " + numberOfAnagrams);
            if (numberOfAnagrams > MIN_NUM_ANAGRAMS) {
                if (wordLength <= MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return testWord;
            }

//            if (wordLength <= MAX_WORD_LENGTH) {
//                wordLength++;
//            }
//            return testWord;
        }
    }

    public String sortLetters(String unorderedWord) {
        char[] unorderedCharacters = unorderedWord.toCharArray();
        Arrays.sort(unorderedCharacters);
        return String.valueOf(unorderedCharacters);
    }
}
