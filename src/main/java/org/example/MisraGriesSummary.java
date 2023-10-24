package org.example;

import org.example.Exceptions.AlghoritmException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.example.Settings.WORD_MIN_LEN;

/**
 * A class to perform Misra-Gries summarization
 */
public class MisraGriesSummary {

    /**
     * file line-by-line reader
     */
    private BufferedReader reader;


    /**
     * Path do the file we want to analyze
     */
    private final String filePath;


    /**
     * First iteration dictionary
     */
    private final Map<String, Integer> D1;


    /**
     * Result dictionary which contains the most common words
     */
    private final Map<String, Integer> D2;


    /**
     * Max number of result words
     */
    private int k;

    /**
     * Stores amount of words after filter
     */
    private int filtered = 0;


    /**
     * Determines if read of filtered words amount is available to check
     */
    private boolean canReadWordsAmonut = false;


    /**
     * Creates a new MisraGriesSummary object
     *
     * @param filePath path to the file you want to read from
     * @throws AlghoritmException if the file path is null or program cannot to open file
     */
    public MisraGriesSummary(String filePath) throws AlghoritmException {

        if (filePath == null)
            throw new AlghoritmException("Filepath is null");

        try {

            this.filePath = filePath;
            reader = setupReader();

            D1 = new LinkedHashMap<>();
            D2 = new LinkedHashMap<>();

        } catch (IOException e) {
            throw new AlghoritmException(e.getMessage());
        }


    }

    /**
     * Starts reading file and counting the most common words
     *
     * @param k max number of result words
     * @throws IOException        if there is a problem during file reading
     * @throws AlghoritmException if K value is less than 1
     */
    public void startAlgorithm(int k) throws IOException, AlghoritmException {

        if (k < 1)
            throw new AlghoritmException("K < 1");

        this.k = k;

        try {

            runLineReader(IterationLevel.FIRST);
            resetReader();
            runLineReader(IterationLevel.SECOND);

            canReadWordsAmonut = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            reader.close();
        }

    }


    /**
     * Performs iterations
     *
     * @param iterationLevel iteration which we want to perform
     * @throws IOException if there is a problem during reading the file
     */
    private void runLineReader(IterationLevel iterationLevel) throws IOException {

        String line;

        while ((line = reader.readLine()) != null) {

            String[] wordsInSingleLine = getFilteredArrayOfWords(line);

            if ((iterationLevel == IterationLevel.FIRST)) {
                firstIteration(wordsInSingleLine);
            } else {
                secondIteration(wordsInSingleLine);
            }

        }

    }


    /**
     * First iteration logic
     *
     * @param wordList list of parsed words from single file line
     */
    private void firstIteration(String[] wordList) {

        for (String word : wordList) {

            if (word.length() < WORD_MIN_LEN)
                continue;

            insertWordToD1(word);
            filtered++;
        }


    }


    /**
     * Word insertion logic in the first iteration dictionary (D1).
     *
     * @param word single word
     */
    private void insertWordToD1(String word) {

        if (dictionarySize(D1) < k - 1) {
            int amount = D1.getOrDefault(word, 0);
            amount++;
            D1.put(word, amount);
            return;
        }

        if (dictionarySize(D1) == k - 1) {

            if (D1.containsKey(word)) {
                int amount = D1.get(word);
                amount++;
                D1.put(word, amount);
            } else
                decreaseAmountOfEachDictionaryElementAndRemoveIfZero();

        }


    }


    /**
     * Decreases amount of each element in first iteration dictionary (D1)
     * and removes if equals to 0
     */
    private void decreaseAmountOfEachDictionaryElementAndRemoveIfZero() {

        List<String> wordsToRemove = new ArrayList<>();

        for (Map.Entry<String, Integer> set : D1.entrySet()) {
            int amount = set.getValue();
            amount--;

            if (amount <= 0)
                wordsToRemove.add(set.getKey());
            else
                D1.put(set.getKey(), amount);

        }


        for (String toRemove : wordsToRemove)
            D1.remove(toRemove);

    }


    /**
     * Second iteration logic
     *
     * @param wordList list of parsed words from single file lin
     */
    private void secondIteration(String[] wordList) {

        int amount;

        for (String word : wordList) {
            if (!D1.containsKey(word) || word.length() < WORD_MIN_LEN)
                continue;

            amount = D2.getOrDefault(word, 0);
            amount++;

            D2.put(word, amount);
        }

    }


    /**
     * Prints result dictionary in the console.
     * For a number to be displayed, its value
     * must be >= amount_of_words_after_filter / k
     *
     * @return amount of printed words
     */
    public int printDictionary() {

        System.out.println("\n\n Dictionary \n\n");
        int outputs = 0;

        for (Map.Entry<String, Integer> set : D2.entrySet()) {

            if (set.getValue() < filtered / k)
                continue;

            outputs++;
            System.out.println(outputs + ". " + set.getKey() + " --> " + set.getValue());
        }

        return outputs;
    }


    /**
     * Return the size of given dictionary
     *
     * @param dictionary dictionary
     * @return amount of elements
     */
    private int dictionarySize(Map<String, Integer> dictionary) {
        return dictionary.size();
    }


    /**
     * Creates new BufferedReader object
     *
     * @return new BufferedReader object reference
     * @throws FileNotFoundException if the file cannot be found
     */
    private BufferedReader setupReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }


    /**
     *
     * Resets the reader to the beginning of the file
     *
     * @throws IOException if the file cannot be found
     */
    private void resetReader() throws IOException {
        this.reader.close();
        this.reader = setupReader();
    }


    /**
     *
     * Replaces all non-letter characters with spaces
     *
     * @param text text
     * @return string without non-letter characters
     */
    public static String replaceNonLetterToSpace(String text) {
        return text.replaceAll("[^a-zA-Z]+", " ");
    }


    /**
     *
     * Filters given line to avoid non-letter characters
     * and gets rid of excess spaces. Finally, parses text into words
     *
     * @param line text
     * @return words array
     */
    private String[] getFilteredArrayOfWords(String line) {

        // to lower
        line = line.toLowerCase();

        // remove non leters
        line = replaceNonLetterToSpace(line);

        return line.split("\\s+");
    }


    /**
     *
     * Returns amount of words after filtering
     *
     * @return amount of words after filtering
     * @throws AlghoritmException if there is no data to return or the program has not completed
     */
    public int getAmountOfWordsAfterFilter() throws AlghoritmException {

        if (!canReadWordsAmonut)
            throw new AlghoritmException("Words are not already counted!");

        return filtered;
    }

}
