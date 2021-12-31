package com.kry.demo.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SnackWin extends JPanel implements ActionListener, KeyListener {

    // 数字表示方向
    static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    // width, height: 游戏区域宽高
    // loc_x, loc_y: 游戏区域左上角位置坐标
    // size: 每次移动的位置大小以及增加的长度
    static final int GAME_LOC_X = 50, GAME_LOC_Y = 50, GAME_WIDTH = 700, GAME_HEIGHT = 500, SIZE = 10;

    // rx, ry: 食物坐标
    static int rx, ry, score = 0, speed = 5;

    boolean start_game;

    JButton start, stop, quit;

    Snack snack;

    public SnackWin() {
        snack = new Snack();
        rx = (int) (Math.random() * (GAME_WIDTH - 10) + GAME_LOC_X);
        ry = (int) (Math.random() * (GAME_HEIGHT - 10) + GAME_LOC_Y);

        start = new JButton("开始");
        stop = new JButton("暂停");
        quit = new JButton("退出");

        setLayout(new FlowLayout(FlowLayout.LEFT));

        this.add(start);
        this.add(stop);
        this.add(quit);

        start.addActionListener(this);
        stop.addActionListener(this);
        quit.addActionListener(this);

        this.addKeyListener(this);

        new Thread(new SnackThread()).start();
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        // 设置画笔颜色
        graphics.setColor(Color.white);
        // 用白色 填充一个矩形区域
        graphics.fillRect(GAME_LOC_X, GAME_LOC_Y, GAME_WIDTH, GAME_HEIGHT);
        // drawRect用于画矩形；X、Y为左上角的位置，width和height为矩形的宽和高
        graphics.drawRect(GAME_LOC_X, GAME_LOC_Y, GAME_WIDTH, GAME_HEIGHT);
        // 用黑色化一个矩形
        graphics.setColor(Color.black);
        // 绘制字符串
        graphics.drawString("Score: " + score + "        Speed: " + speed + "      速度最大为100" , 250, 25);
        // 用绿色填充一个长宽为10的矩形 表示 食物
        graphics.setColor(Color.green);
        graphics.fillRect(rx, ry, 10, 10);
        // 画蛇
        snack.draw(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            start_game = true;
            start.setEnabled(false);
            stop.setEnabled(true);
        } else if (e.getSource() == stop) {
            start_game = false;
            start.setEnabled(true);
            stop.setEnabled(false);
        } else {
            System.exit(0);
        }
        this.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!start_game) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (snack.length() != 1 && snack.getDir() == DOWN) {
                    break;
                }
                snack.changeDir(UP);
                break;
            case KeyEvent.VK_DOWN:
                if (snack.length() != 1 && snack.getDir() == UP) {
                    break;
                }
                snack.changeDir(DOWN);
                break;
            case KeyEvent.VK_LEFT:
                if (snack.length() != 1 && snack.getDir() == RIGHT) {
                    break;
                }
                snack.changeDir(LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                if (snack.length() != 1 && snack.getDir() == LEFT) {
                    break;
                }
                snack.changeDir(RIGHT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    class SnackThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(Math.max(100 - speed, 0));
                    repaint();
                    if (start_game) {
                        snack.move();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}