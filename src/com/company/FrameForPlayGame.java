package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameForPlayGame extends JFrame {
    private JMenuBar menuBar;
    private JMenu menuGame;
    private JMenuItem itemExit;
    private JMenuItem itemStart;

    FrameForPlayGame(Placement player, Placement enemy, int players) {
        setTitle("Морской бой");
        if (players == 0) {
            AutoPlacement.setPalubaAuto(enemy, new Ships());
        }

        PlayGame pole = new PlayGame(player, enemy, players);
        pole.startGame();
        menuBar = new JMenuBar();
        menuGame = new JMenu("Игра");

        itemExit = new JMenuItem("Выход");
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        itemStart = new JMenuItem("Новая игра");
        itemStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartFrame f3 = new StartFrame();
                f3.show();
                dispose();
            }
        });

        menuGame.add(itemStart);
        menuGame.add(itemExit);
        menuBar.add(menuGame);

        setJMenuBar(menuBar);

        Container container = getContentPane();
        container.add(pole);
        setLocation(350, 200);
        setSize(pole.getSize());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

}

