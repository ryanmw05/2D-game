package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("A JFrame");
        frame.setSize(1920, 1080);
        frame.setLocation(500,500);
        frame.add(new JLabel(new ImageIcon("src/Character/Sprites.png")));
        frame.setVisible(true);
    }
}