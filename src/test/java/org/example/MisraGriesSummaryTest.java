package org.example;

import org.example.Exceptions.AlghoritmException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class MisraGriesSummaryTest {


    @Test
    void MisraGriesSummary() {

        assertDoesNotThrow( () -> new MisraGriesSummary("english.200MB.txt"));

        assertDoesNotThrow( () -> new MisraGriesSummary("dickens.simple"));

        assertThrows(AlghoritmException.class, () -> new MisraGriesSummary(null));

        assertThrows(AlghoritmException.class, () -> new MisraGriesSummary("random_file.txt"));

    }


    @Test
    void startAlgorithm() {

        assertThrows(AlghoritmException.class, () -> {
            MisraGriesSummary alg = new MisraGriesSummary("dickens.simple");
            alg.startAlgorithm(-2);
        });

        assertThrows(AlghoritmException.class, () -> {
            MisraGriesSummary alg = new MisraGriesSummary("dickens.simple");
            alg.startAlgorithm(0);
        });

        assertDoesNotThrow( () -> {
            MisraGriesSummary alg = new MisraGriesSummary("dickens.simple");
            alg.startAlgorithm(10);
        });

    }

    @Test
    void getAmountOfWordsAfterFilter() {

        assertDoesNotThrow( () -> {

            MisraGriesSummary english = new MisraGriesSummary("english.200MB.txt");
            english.startAlgorithm(100);

            int englishWords = english.getAmountOfWordsAfterFilter();
            assertEquals(29_250_532, englishWords);

        });


        assertDoesNotThrow( () -> {

            MisraGriesSummary dickens = new MisraGriesSummary("dickens.simple");
            dickens.startAlgorithm(100);

            int dickensWords = dickens.getAmountOfWordsAfterFilter();
            assertEquals(1_380_830, dickensWords);

        });


        assertThrows(AlghoritmException.class, () -> {
            MisraGriesSummary dickens = new MisraGriesSummary("dickens.simple");
            dickens.getAmountOfWordsAfterFilter();
        });



    }


    @Test
    void printDictionary() {

        assertDoesNotThrow( () -> {
            MisraGriesSummary alg = new MisraGriesSummary("dickens.simple");
            alg.startAlgorithm(100);

            int printedLines = alg.printDictionary();

            assertEquals(8, printedLines);

        });

    }

    @Test
    void replaceNonLetterToSpace() {

        String text = "t)(*()&#@$#e#s&to!!w.y N@ap(i)-->s";
        text = MisraGriesSummary.replaceNonLetterToSpace(text);

        assertEquals("t e s to w y N ap i s", text);

    }
}