/**
 * author: Omar Khaled Al Haj 20190351
 * author: Joseph Diaa Saied  20190155
 */

package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

class HuffmanNode {
    int data;
    char c;
    HuffmanNode left;
    HuffmanNode right;
}

class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.data - y.data;
    }
}

class First {
    public static boolean first = true;
}

public class Main {

    public static void printCode(HuffmanNode root, String s, BufferedWriter writer, HashMap<Character, String> encoding) throws IOException {
        if (root.left == null && root.right == null) {
            if (First.first) {
                writer.write(root.c + " " + s);
                First.first = false;
            } else {
                writer.write(", " + root.c + " " + s);
            }
            encoding.put(root.c, s);
            return;
        }
        printCode(root.left, s + "1", writer, encoding);
        printCode(root.right, s + "0", writer, encoding);
    }

    public static void compress(String inFile, String outFile1, String outFile2) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inFile));
        Scanner sc = new Scanner(br);
        String input = sc.nextLine();
        HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
        for (int i = 0; i < input.length(); i++) {
            char key = input.charAt(i);
            freq.merge(key, 1, Integer::sum);
        }
        int n = freq.size();
        PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>(n, new MyComparator());

        Iterator it = freq.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            HuffmanNode hn = new HuffmanNode();
            hn.c = (char) pair.getKey();
            hn.data = (int) pair.getValue();
            hn.left = null;
            hn.right = null;
            q.add(hn);
        }

        HuffmanNode root = null;
        while (q.size() > 1) {
            HuffmanNode x = q.peek();
            q.poll();
            HuffmanNode y = q.peek();
            q.poll();
            HuffmanNode f = new HuffmanNode();
            f.data = x.data + y.data;
            f.c = '-';
            f.left = x;
            f.right = y;
            root = f;
            q.add(f);
        }
        HashMap<Character, String> encoding = new HashMap<Character, String>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile1));
        printCode(root, "", writer, encoding);
        writer.close();
        StringBuilder temporal = new StringBuilder();
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(outFile2));
        for (int i = 0; i < input.length(); i++) {
            temporal.append(encoding.get(input.charAt(i)));
        }
        writer2.write(String.valueOf(temporal));
        writer2.close();
        for (Map.Entry<Character, Integer> set : freq.entrySet()) {
            System.out.println(set.getKey() + " " + set.getValue());
        }
        System.out.println(1.0 * (input.length() * 8) / temporal.length());
    }

    public static void decompress(String dict, String code, String output) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dict));
        Scanner sc = new Scanner(br);
        String input = sc.nextLine();
        StringBuilder result = new StringBuilder();
        HashMap<String, String> decoding = new HashMap<String, String>();
        String[] tokens = input.split(", ");
        for (int i = 0; i < tokens.length; i++) {
            String c = "#";
            String s = "";
            String[] tempTokens = tokens[i].split(" ");
            if (tempTokens.length == 1) {
                if (tempTokens[0].equals("")) continue;
                else {
                    c = ",";
                    s = tempTokens[0];
                }
            } else if (tempTokens.length == 3) {
                c = " ";
                s = tempTokens[2];
            } else {
                c = tempTokens[0];
                s = tempTokens[1];
            }
            decoding.put(s, c);
        }
        BufferedReader br2 = new BufferedReader(new FileReader(code));
        Scanner sc2 = new Scanner(br2);
        String input2 = sc2.nextLine();
        String buff = "";
        for (int i = 0; i < input2.length(); i++) {
            buff += input2.charAt(i);
            if (decoding.get(buff) != null) {
                result.append(decoding.get(buff));
                buff = "";
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        writer.write(String.valueOf(result));
        writer.close();
    }

    public static void GUI() {
        JFrame frame = new JFrame("Standard Huffman");

        JPanel panel = new JPanel();

        Label l1 = new Label ("Input/Output path: ");
        l1.setBounds(20, 40, 150, 30);
        l1.setFont(new Font("Verdana", Font.BOLD, 16));
        panel.add(l1);

        Label l2 = new Label ("Dictionary Path: ");
        l2.setBounds(20, 90, 150, 30);
        l2.setFont(new Font("Verdana", Font.BOLD, 16));
        panel.add(l2);

        Label l3 = new Label ("Output Path: ");
        l3.setBounds(20, 140, 150, 30);
        l3.setFont(new Font("Verdana", Font.BOLD, 16));
        panel.add(l3);

        final JTextField t1, t2, t3;
        t1 = new JTextField("");
        t1.setBounds(190, 40, 250, 30);
        t2 = new JTextField("");
        t2.setBounds(190, 90, 250, 30);
        t3 = new JTextField("");
        t3.setBounds(190, 140, 250, 30);

        JButton compress1 = new JButton("Compress");
        JButton decompress1 = new JButton("Decompress");
        compress1.setBounds(190, 190, 110, 25);
        decompress1.setBounds(330, 190, 110, 25);
        compress1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputPath = t1.getText();
                String dictionaryPath = t2.getText();
                String outputPath = t3.getText();
                try {
                    compress(inputPath, dictionaryPath, outputPath);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        decompress1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputPath = t1.getText();
                String dictionaryPath = t2.getText();
                String outputPath = t3.getText();
                try {
                    decompress(dictionaryPath, outputPath, inputPath);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        panel.add(t1);
        panel.add(t2);
        panel.add(t3);
        panel.add(compress1);
        panel.add(decompress1);
        frame.add(panel);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        GUI();
//        compress("input.in", "output.out", "output2.out");
//        decompress("output.out", "output2.out", "input.in");
    }
}