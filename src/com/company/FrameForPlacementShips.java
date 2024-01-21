package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameForPlacementShips extends JFrame {

    private JMenuBar menuBar;

    private JMenu menuGame;
    private JMenuItem itemExit;
    private JMenuItem itemStart;

    private JButton itemStartAuto;
    private JButton itemStartRast;
    private JPanel panel;
    private JButton nextButton;

    private Placement player;

    private Placement enemy;

    FrameForPlacementShips(Placement player, Placement enemy, int players) {
        this.player = player;
        this.enemy = enemy;
        setTitle("Морской бой");

        PlacementShips pole;
        if (players == 0 || players == 1) {
            pole = new PlacementShips(player, players);
        } else {
            pole = new PlacementShips(enemy, players);
        }

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

        menuBar.add(itemStartAuto);
        menuBar.add(itemStartRast);
        setJMenuBar(menuBar);


        Container container = getContentPane();
        container.add(pole);
        setLocation(350, 200);

        setSize(pole.getSize());
        setResizable(false);
        setVisible(true);

        nextButton = pole.nextButton;

        if (players == 0) {
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FrameForPlayGame f2 = new FrameForPlayGame(player, enemy, 0);
                    f2.show();
                    dispose();
                }
            });
        }

        if (players == 1) {
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FrameForPlacementShips f1 = new FrameForPlacementShips(player, enemy, 2);
                    f1.show();
                    dispose();
                }
            });
        }


        if (players == 2) {
            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FrameForPlayGame f2 = new FrameForPlayGame(player, enemy, 1);
                    f2.show();
                    dispose();
                }
            });
        }

        itemStartAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pole.startAuto();
            }
        });
        itemStartRast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pole.startRasstanovka();
            }
        });


    }

}

