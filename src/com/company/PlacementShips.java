package com.company;
//для работы с изображениями

import javax.imageio.ImageIO;
//для работы с графикой
import javax.swing.*;
//для работы с окнами
import java.awt.*;
//для обработки событий мыши
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class PlacementShips extends JPanel {
    private Placement player;
    private final int DXY = 60;
    private final int H = 23;
    private String number[] = {"А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "К"};
    private int mX, mY; //коорд мыши
    private Timer timer;
    private int players;

    private BufferedImage paluba;
    private Rectangle2D line4, line3, line2, line1;
    private boolean isSelectP4 = false;
    private boolean isSelectP3 = false;
    private boolean isSelectP2 = false;
    private boolean isSelectP1 = false;

    private Ships ships = new Ships();
    private boolean vert = true; //направление расстановки
    private JButton checkNapr;
    public JButton nextButton;
    private static boolean rasstanovka;

    public PlacementShips(Placement player, int players) {
        this.player = player;
        addMouseListener(new Mouse());
        addMouseMotionListener(new Mouse());
        setFocusable(true);
        this.players = players;
        setSize(650, 400);
        try {
            paluba = ImageIO.read(getClass().getResource("image/paluba.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //таймер отрисовки
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        setLayout(null);
        checkNapr = new JButton("Повернуть");
        checkNapr.setBackground(new Color(248, 248, 255));
        checkNapr.setBounds(DXY + 14 * H, DXY + 9 * H, 7 * H, H);
        checkNapr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vert) vert = false;
                else vert = true;
            }
        });
        add(checkNapr);
        checkNapr.setVisible(false);

        nextButton = new JButton("Далее");
        nextButton.setBounds(DXY + 14 * H, DXY + 9 * H, 7 * H, H);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g.setColor(new Color(248, 248, 255));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("Times New Roman", 0, H - 5));
        g.setColor(new Color(330099));
        if (rasstanovka) {
            g2.setStroke(new BasicStroke(2));
            if (vert) {
                line4 = new Rectangle2D.Double(DXY + 14 * H, DXY, 4 * H, H);
                line3 = new Rectangle2D.Double(DXY + 14 * H, DXY + 2 * H, 3 * H, H);
                line2 = new Rectangle2D.Double(DXY + 14 * H, DXY + 4 * H, 2 * H, H);
                line1 = new Rectangle2D.Double(DXY + 14 * H, DXY + 6 * H, 1 * H, H);
            } else {
                line4 = new Rectangle2D.Double(DXY + 14 * H, DXY, H, 4 * H);
                line3 = new Rectangle2D.Double(DXY + 16 * H, DXY, H, 3 * H);
                line2 = new Rectangle2D.Double(DXY + 18 * H, DXY, H, 2 * H);
                line1 = new Rectangle2D.Double(DXY + 20 * H, DXY, H, 1 * H);
            }
            if (ships.countP4 != 0) ((Graphics2D) g).draw(line4);
            if (ships.countP3 != 0) ((Graphics2D) g).draw(line3);
            if (ships.countP2 != 0) ((Graphics2D) g).draw(line2);
            if (ships.countP1 != 0) ((Graphics2D) g).draw(line1);

            if (!ships.isOver()) {
                g.drawString("Расставьте корабли", DXY + 14 * H, DXY - H);
                checkNapr.setVisible(true);
            } else {
                checkNapr.setVisible(false);
            }
        }
        //надпись
        if (players == 0) {
            g.drawString("Игрок", DXY + 4 * H, DXY - H);
        } else {
            g.drawString("Игрок " + players, DXY + 4 * H, DXY - H);
        }
        //цифры и буквы
        for (int i = 1; i <= 10; i++) {
            //12345678910
            g.drawString(number[i - 1], DXY - H, DXY + i * H - (H / 4));
            //абвгдежзик
            g.drawString(String.valueOf(i), DXY + (i - 1) * H + (H / 4), DXY - 3);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (player.getValuePlacement(i, j) > 0 && player.getValueHits(i, j) == Placement.EMPTY) {
                        g.drawImage(paluba, DXY + H * i, DXY + H * j, H, H, null);
                }
            }
        }

        ///линии
        for (int i = DXY; i <= DXY + 10 * H; i += H) {
            g2.setStroke(new BasicStroke(1));
            g.setColor(new Color(202, 202, 255));
            g.drawLine(DXY, i, DXY + 10 * H, i); // ----
            g.drawLine(i, DXY, i, DXY + 10 * H);

            g2.setStroke(new BasicStroke(2));
            g.setColor(new Color(330099));
            g.drawRect(DXY, DXY, 10 * H, 10 * H);
        }

        g.setFont(new Font("Times New Roman", 0, H));
        g.setColor(new Color(330099));
    }


    // ручная установка кораблей
    public boolean setPaluba(int i, int j, int kolPal, boolean napr) {
        int[][] mas = player.getMatrix();
        //признак установки палубы
        boolean flag = false;
        // Если можно расположить палубу
        if (Logic.testNewPaluba(mas, i, j)) {
            if (!napr) { // горизонтально
                if (Logic.testNewPaluba(mas, i, j + (kolPal - 1)))
                    flag = true;
            } else if (napr) { // вертикально
                if (Logic.testNewPaluba(mas, i + (kolPal - 1), j))
                    flag = true;
            }
        }
        if (flag) {
            //Помещаем в ячейку число палуб
            mas[i][j] = kolPal;
            Logic.Okr(mas, i, j, Placement.SUR);
            if (!napr) { // горизонтально
                for (int k = kolPal - 1; k >= 1; k--) {
                    mas[i][j + k] = kolPal;
                    Logic.Okr(mas, i, j + k, Placement.SUR);
                }
            } else if (napr) { // вертикально
                for (int k = kolPal - 1; k >= 1; k--) {
                    mas[i + k][j] = kolPal;
                    Logic.Okr(mas, i + k, j, Placement.SUR);
                }
            }
        }
        return flag;
    }

    public void startAuto() {
        rasstanovka = false;
        checkNapr.setVisible(false);
        timer.start();
        AutoPlacement.setPalubaAuto(player, ships);
        add(nextButton);

    }

    public void startRasstanovka() {
        rasstanovka = true;
        player.setMatrix(new int[10][10]);
        timer.start();
        ships.countP4 = 1;
        ships.countP3 = 2;
        ships.countP2 = 3;
        ships.countP1 = 4;
        add(nextButton);

    }

    public class Mouse implements MouseListener, MouseMotionListener {
        //клавиша мыши нажата
        @Override
        public void mousePressed(MouseEvent e) {
            // Если сделано одиночное нажатие левой клавишей мыши
            if (rasstanovka) {
                if (line4.contains(e.getPoint())) {
                    isSelectP4 = true;
                } else if (line3.contains(e.getPoint())) {
                    isSelectP3 = true;
                } else if (line2.contains(e.getPoint())) {
                    isSelectP2 = true;
                } else if (line1.contains(e.getPoint())) {
                    isSelectP1 = true;
                }
            }

        }

        // клавиша мыши отпущена
        @Override
        public void mouseReleased(MouseEvent e) {

            if (rasstanovka) {
                mX = e.getX();
                mY = e.getY();
                int i = (mX - DXY) / H;
                int j = (mY - DXY) / H;
                if (ships.countP4 != 0 && isSelectP4 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP4 = false;
                    if (setPaluba(i, j, 4, vert)) {
                        ships.countP4--;
                    }

                } else if (ships.countP3 != 0 && isSelectP3 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP3 = false;
                    if (setPaluba(i, j, 3, vert)) {
                        ships.countP3--;
                    }

                } else if (ships.countP2 != 0 && isSelectP2 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP2 = false;
                    if (setPaluba(i, j, 2, vert)) {
                        ships.countP2--;
                    }

                } else if (ships.countP1 != 0 && isSelectP1 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP1 = false;
                    if (setPaluba(i, j, 1, vert)) {
                        ships.countP1--;
                    }
                }
            }
        }

        //перемещение мыши
        @Override
        public void mouseDragged(MouseEvent e) {
            if (rasstanovka) {
                mX = e.getX();
                mY = e.getY();
                int i = (mX - (DXY)) / H;
                int j = (mY - DXY) / H;
                Graphics g = getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2));
                g.setColor(new Color(330099));
                if (isSelectP4) {
                    if (mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H){
                        if (vert) g.drawRect(DXY + H * i, DXY + H * j, H * 4, H);
                        else g.drawRect(DXY + H * i, DXY + H * j, H, H * 4);
                    }
                }
                if (isSelectP3) {
                    if (mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H){
                        if (vert) g.drawRect(DXY + H * i, DXY + H * j, H * 3, H);
                        else g.drawRect(DXY + H * i, DXY + H * j, H, H * 3);
                    }
                }
                if (isSelectP2) {
                    if (mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H){
                        if (vert) g.drawRect(DXY + H * i, DXY + H * j, H * 2, H);
                        else g.drawRect(DXY + H * i, DXY + H * j, H, H * 2);
                    }
                }
                if (isSelectP1) {
                    if (mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H){
                        g.drawRect(DXY + H * i, DXY + H * j, H, H);
                    }
                }

            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

}