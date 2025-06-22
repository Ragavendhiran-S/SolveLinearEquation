package io.github.ragavendhiran;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LinearEquationSolver {

    private final File inputFile;

    public LinearEquationSolver(File inputFile) {
        this.inputFile = inputFile;
    }

    public void solve(File outputFile) {
        try (Scanner kb = new Scanner(inputFile)) {
            int numOfVariables = kb.nextInt();
            int numOfEquations = kb.nextInt();
            Set<Integer> toRemove = new HashSet<>();
            Complex[][] complexInputMatrix = new Complex[numOfEquations][numOfVariables + 1];
            int numOfSignificantEquations = numOfEquations;

            // Parse input matrix
            for (int i = 0; i < numOfEquations; i++) {
                for (int j = 0; j < numOfVariables + 1; j++) {
                    String inputString = kb.next().replace(" ", "");
                    double realPart = 0;
                    double imaginaryPart = 0;

                    if (inputString.contains("i")) {
                        if (inputString.equals("i")) {
                            imaginaryPart = 1;
                        } else if (inputString.equals("-i")) {
                            imaginaryPart = -1;
                        } else if (inputString.endsWith("i")) {
                            int signIndex = Math.max(inputString.lastIndexOf('+'), inputString.lastIndexOf('-', 1));
                            if (signIndex > 0) {
                                String realStr = inputString.substring(0, signIndex);
                                String imagStr = inputString.substring(signIndex, inputString.length() - 1);

                                realPart = Double.parseDouble(realStr);
                                if (imagStr.equals("+")) {
                                    imaginaryPart = 1;
                                } else if (imagStr.equals("-")) {
                                    imaginaryPart = -1;
                                } else {
                                    imaginaryPart = Double.parseDouble(imagStr);
                                }
                            }
                            else {
                                imaginaryPart = Double.parseDouble(inputString.replace("i", ""));
                            }
                        } else {
                            realPart = Double.parseDouble(inputString);
                        }
                    } else {
                        realPart = Double.parseDouble(inputString);
                    }

                    complexInputMatrix[i][j] = new Complex(realPart, imaginaryPart);
                }
            }

            // Remove zero and duplicate rows
            for (int i = 0; i < numOfEquations; i++) {
                if (Arrays.stream(complexInputMatrix[i])
                        .limit(numOfVariables)
                        .allMatch(value -> value.getReal() == 0 && value.getImaginary() == 0) &&
                        complexInputMatrix[i][numOfVariables].getReal() == 0 &&
                        complexInputMatrix[i][numOfVariables].getImaginary() == 0) {
                    toRemove.add(i);
                    numOfSignificantEquations--;
                } else if (i > 0) {
                    int finalI = i;
                    if (Arrays.stream(complexInputMatrix)
                            .limit(i)
                            .anyMatch(row -> Arrays.equals(row, complexInputMatrix[finalI]))) {
                        toRemove.add(i);
                        numOfSignificantEquations--;
                    }
                }
            }

            // Create new matrix with significant rows only
            Complex[][] complexSignificantInput = new Complex[numOfSignificantEquations][numOfVariables + 1];
            int controller = 0;
            for (int i = 0; i < numOfEquations; i++) {
                if (!toRemove.contains(i)) {
                    System.arraycopy(complexInputMatrix[i], 0, complexSignificantInput[controller], 0, numOfVariables + 1);
                    controller++;
                }
            }

            // Solve and output
            Matrix matrix = new Matrix(numOfSignificantEquations);
            matrix.setMatrix(complexSignificantInput);
            SolverUtility solver = new SolverUtility(matrix, numOfSignificantEquations);
            Solution numOfSolution = solver.solve();

            switch (numOfSolution) {
                case Solution.ZERO -> SolverUtility.outputSpecialCaseToFile(outputFile, "No solutions");
                case Solution.ONE -> solver.outputToFile(outputFile);
                case Solution.INFINITE -> SolverUtility.outputSpecialCaseToFile(outputFile, "Infinitely many solutions");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}