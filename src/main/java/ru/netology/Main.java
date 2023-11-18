package ru.netology;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static final ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static final ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static final ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        Thread creator = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String str = generateText("abc", 100_0);
                try {
                    queueA.put(str);
                    queueB.put(str);
                    queueC.put(str);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        creator.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread threadA = new Thread(() -> {
            int max = 0;
            int res = 0;
            String resStr = null;
            String resMaxStr = null;
                while (queueA.peek() != null) {
                    try {
                        resStr = queueA.take();
                        res = resStr.chars()
                                .filter(x -> x == 'a')
                                .boxed()
                                .toArray().length;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (max < res) {
                        max = res;
                        resMaxStr = resStr;
                    }
                }
            System.out.print("максимальное количество букв a: " + max);
            System.out.println();
        });
        Thread threadB = new Thread(() -> {
            int max = 0;
            int res = 0;
            String resStr = null;
            String resMaxStr = null;
                while (queueA.peek() != null) {
                    try {
                        resStr = queueB.take();
                        res = resStr.chars()
                                .filter(x -> x == 'b')
                                .boxed()
                                .toArray().length;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (max < res) {
                        max = res;
                        resMaxStr = resStr;
                    }
                }

            System.out.print("максимальное количество букв б: " + max);
            System.out.println();
        });
        Thread threadC = new Thread(() -> {
            int max = 0;
            int res = 0;
            String resStr = null;
            String resMaxStr = null;
            while (queueA.peek() != null) {
                try {
                    resStr = queueC.take();
                    res = resStr.chars()
                            .filter(x -> x == 'c')
                            .boxed()
                            .toArray().length;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (max < res) {
                    max = res;
                    resMaxStr = resStr;
                }
            }

            System.out.print("максимальное количество букв c: " + max);
            System.out.println();
        });

        threadA.start();
        threadB.start();
        threadC.start();
        try {
            threadA.join();
            threadB.join();
            threadC.join();
            creator.join();
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int findLetter(String str, char letter) {
        return str.chars()
                .filter(x -> x == letter)
                .boxed()
                .toArray().length;
    }
}