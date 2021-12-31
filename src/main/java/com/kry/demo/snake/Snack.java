package com.kry.demo.snake;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snack {

    static final int DIR [][] = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    private List<Node> nodes = new ArrayList<>();

    private int curDir;

    public Snack() {
        curDir = 3;
        nodes.add(new Node(350, 250));
    }

    int length() {
        return nodes.size();
    }

    int getDir() {
        return curDir;
    }

    void draw(Graphics graphics) {
        graphics.setColor(Color.black);
        for (int i = 0; i < nodes.size(); i++) {
            graphics.fillRect(nodes.get(i).getX(), nodes.get(i).getY(), 10, 10);
        }
    }

    boolean dead() {
        for (int i = 1; i < nodes.size(); i++) {
            if (nodes.get(0).getX() == nodes.get(i).getX() && nodes.get(0).getY() == nodes.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    Node headMove() {
        Node head = nodes.get(0);
        // 向上/下移动时， curDir 为 0/1, X 轴不变（即 snackWin.Size * DIR[curDir][0]的值为0），Y轴变化
        // 向左/右移动时, curDir 为 2/3, Y 轴不变（即 snackWin.Size * DIR[curDir][1]的值为0），X轴变化
        int x = head.getX() + SnackWin.SIZE * DIR[curDir][0];
        int y = head.getY() + SnackWin.SIZE * DIR[curDir][1];

        // 用一个矩形代表蛇的一个节点，矩形的长宽为10，故设置Size也为10
        // 当头部的位置大于游戏区域的宽度证明它到达了最右边，之后它需要从左边出现，即X坐标变为最左边的X坐标位置
        if (x > SnackWin.GAME_LOC_X + SnackWin.GAME_WIDTH - SnackWin.SIZE) {
            x = SnackWin.GAME_LOC_X;
        }
        if (x < SnackWin.GAME_LOC_X) {
            x = SnackWin.GAME_WIDTH + SnackWin.GAME_LOC_X - SnackWin.SIZE;
        }

        if (y > SnackWin.GAME_LOC_Y + SnackWin.GAME_HEIGHT - SnackWin.SIZE) {
            y = SnackWin.GAME_LOC_Y;
        }
        if (y < SnackWin.GAME_LOC_Y) {
            y = SnackWin.GAME_HEIGHT + SnackWin.GAME_LOC_Y - SnackWin.SIZE;
        }

        Node tmp = new Node(x, y);
        return tmp;
    }

    void move() {
        Node head = headMove();
        Node newNode = new Node();
        boolean eat = false;

        // 通过判断头部的X/Y坐标位置和食物的X/Y坐标位置，由于头部大小是10，故距离小于10就代表吃到了
        if (Math.abs(head.getX() - SnackWin.rx) < 10 && Math.abs(head.getY() - SnackWin.ry) < 10) {
            eat = true;
            newNode = new Node(nodes.get(nodes.size() - 1));
            SnackWin.rx = (int)(Math.random() * (SnackWin.GAME_WIDTH - 10) + SnackWin.GAME_LOC_X);
            SnackWin.ry = (int)(Math.random() * (SnackWin.GAME_HEIGHT - 10) + SnackWin.GAME_LOC_Y);
        }

        for (int i = nodes.size() - 1; i > 0; i--) {
            nodes.set(i, nodes.get(i - 1));
        }
        nodes.set(0, head);

        if (dead()) {
            JOptionPane.showMessageDialog(null, "snake eating itself", "message", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        if (eat) {
            nodes.add(newNode);
            SnackWin.score++;
            SnackWin.speed--;

        }
    }

    void changeDir(int dir) {
        this.curDir = dir;
    }

}

class Node {
    private int x, y;

    public Node() {
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node(Node tmp) {
        this.x = tmp.x;
        this.y = tmp.y;
    }

    public String position() {
        return x + "-" + y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}