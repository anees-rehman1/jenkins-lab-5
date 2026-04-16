package com.example.app;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello Jenkins CI/CD!");
        System.out.println("Application version: 1.0.0");
    }
    
    public String getGreeting() {
        return "Hello Jenkins CI/CD!";
    }
    
    public int add(int a, int b) {
        return a + b;
    }
    
    public int multiply(int a, int b) {
        return a * b;
    }
}
