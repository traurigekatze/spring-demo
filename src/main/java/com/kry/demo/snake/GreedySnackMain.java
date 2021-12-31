package com.kry.demo.snake;

import javax.swing.*;

public class GreedySnackMain extends JFrame {

    SnackWin snackWin;

    int width = 800, height = 600, LOC_X = 200, LOC_Y = 80;

    public GreedySnackMain() {
        super("GreedySnack_SL");
        snackWin = new SnackWin();
        add(snackWin);
        this.setSize(width, height);
        this.setVisible(true);
        this.setLocation(LOC_X, LOC_Y);
    }

    public static void main(String[] args) {
        new GreedySnackMain();
    }

}
