package org.example;

import org.example.Exceptions.AlghoritmException;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        exampleCode();

    }


    private static void exampleCode() {

        long startTime = System.currentTimeMillis();

        try {

            MisraGriesSummary alg = new MisraGriesSummary("english.200MB.txt");
            alg.startAlgorithm(100);
            alg.printDictionary();

        } catch (AlghoritmException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("\nExecution time: " + executionTime + "ms");
        }

    }

}