package ru.netology;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
    public static Thread creator;

    public static void main(String[] args) {
        creator = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String str = generateText("abc", 100000);
                try {
                    queueA.put(str);
                    queueB.put(str);
                    queueC.put(str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        creator.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread threadA = getThread(queueA, 'a');
        Thread threadB = getThread(queueB, 'b');
        Thread threadC = getThread(queueC, 'c');


        threadA.start();
        threadB.start();
        threadC.start();
        try {
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
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

    public static Thread getThread(BlockingQueue<String> queue, char letter) {
        return new Thread(() -> {
            int i = getCount(queue, letter);
            System.out.println("Max count of '" + letter + "' is: " + i);
        });
    }

    public static int getCount(BlockingQueue<String> queue, char letter) {
        int max = 0;
        int res = 0;
        String resStr;
        try {
            while (creator.isAlive()) {
                resStr = queue.take();
                for (char c : resStr.toCharArray()) {
                    if (c == letter) res++;
                }
                if (max < res) max = res;
                res = 0;
            }
        } catch (InterruptedException e) {
            return -1;
        }
        return max;
    }
}