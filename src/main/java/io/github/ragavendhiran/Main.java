package io.github.ragavendhiran;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String fileInput = "C:\\My_Computer\\Resume Projects\\Linear_equation_solver\\src\\main\\java\\input.txt",
                fileOutput = "C:\\My_Computer\\Resume Projects\\Linear_equation_solver\\src\\main\\java\\output.txt";
        LinearEquationSolver solver = new LinearEquationSolver(new File(fileInput));
        solver.solve(new File(fileOutput));
    }
}