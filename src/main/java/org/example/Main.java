package org.example;

public class Main {
    public static void main(String[] args) {
        GitHubAnalyzer analyzer = new GitHubAnalyzer();
        String username = "MostafaNady2";
        System.out.printf("=== GitHub Profile: %s ===\n\n", username);
        analyzer.analyze(username);

    }
}