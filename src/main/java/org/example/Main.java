package org.example;

public class Main {
    public static void main(String[] args) {
        if(args.length==0){
            System.out.println("Usage : java Main <github-username>");
            System.out.println("Example : java Main torvalds");
            return;
        }
        String username = args[0];
        GitHubAnalyzer analyzer = new GitHubAnalyzer();
        analyzer.analyze(username);
    }
}