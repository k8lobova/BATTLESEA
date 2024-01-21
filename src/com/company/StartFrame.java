package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame {
    private JPanel panel;
    private JButton playForOneButton;
    private JButton playForTwoButton;

    public StartFrame() {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Морской бой");
        panel.setPreferredSize(new Dimension(300, 150));
        pack();
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        playForOneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameForPlacementShips f1 = new FrameForPlacementShips(new Placement(), new Placement(), 0);
                f1.setVisible(true);
                dispose();

            }
        });


        playForTwoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrameForPlacementShips f1 = new FrameForPlacementShips(new Placement(), new Placement(), 1);
                f1.setVisible(true);
                dispose();
            }
        });
    }


    public static void main(String[] args) {
        new StartFrame();
    }

}
