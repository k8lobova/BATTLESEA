package com.company;
//для работы с изображениями

import javax.imageio.ImageIO;
//для работы с графикой
import javax.swing.*;
//для работы с окнами
import java.awt.*;
//для обработки событий мыши
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class PlayGame extends JPanel {
    private Placement player;
    private Placement enemy;
    private final int DXY = 60;
    private final int H = 23;
    private String number[] = {"А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "К"};
    private Game game;
    private int countOfPlayers;
    private int mX, mY; //коорд мыши

    private int players;
    private Timer timer;
    private BufferedImage hurt, miss, killed, paluba;
    private static boolean rasstanovka;
    private Ships playerShips = new Ships();
    private Ships enemyShips = new Ships();

    public PlayGame(Placement player, Placement enemy, int players) {
        addMouseListener(new Mouse());
        addMouseMotionListener(new Mouse());
        setFocusable(true);
        this.player = player;
        this.players = players;
        this.enemy = enemy;
        this.game = new Game(this.player, this.enemy);
        this.countOfPlayers = players;

        setSize(800, 520);
        try {
            hurt = ImageIO.read(getClass().getResource("image/hurt.png"));
            miss = ImageIO.read(getClass().getResource("image/miss.png"));
            killed = ImageIO.read(getClass().getResource("image/killed.png"));
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
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g.setColor(new Color(248, 248, 255));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("Times New Roman", 0, H - 5));
        g.setColor(new Color(330099));
        //выведение надписей
        if (countOfPlayers == 0) {
            g.drawString("Поле Игрока", DXY + 3 * H, DXY - H);
            g.drawString("Поле Компьютера", DXY + 15 * H, DXY - H);
            g.drawString("Ходов игрока: ", DXY + 23 * H, DXY + 13 * H - (H / 4));
            g.drawString(String.valueOf(game.kolHodPlayer), DXY + 28 * H, DXY + 13 * H - (H / 4));
            g.drawString("Ходов компьютера: ", DXY + 23 * H, DXY + 14 * H - (H / 4));
            g.drawString(String.valueOf(game.kolHodEnemy), DXY + 30 * H - (H / 4), DXY + 14 * H - (H / 4));

        } else {
            g.drawString("Поле Игрока 1", DXY + 3 * H, DXY - H);
            g.drawString("Поле Игрока 2", DXY + 16 * H, DXY - H);
            g.drawString("Ходов игрока 1: ", DXY + 23 * H, DXY + 13 * H - (H / 4));
            g.drawString(String.valueOf(game.kolHodPlayer), DXY + 28 * H + (H / 2), DXY + 13 * H - (H / 4));
            g.drawString("Ходов игрока 2: ", DXY + 23 * H, DXY + 14 * H - (H / 4));
            g.drawString(String.valueOf(game.kolHodEnemy), DXY + 28 * H + (H / 2), DXY + 14 * H - (H / 4));
        }


        //Выводим цифры и буквы
        for (int i = 1; i <= 10; i++) {
            //12345678910
            g.drawString(number[i - 1], DXY - H, DXY + i * H - (H / 4));
            g.drawString(number[i - 1], DXY + 12 * H, DXY + i * H - (H / 4));
            //абвгдежзик
            g.drawString(String.valueOf(i), DXY + (i - 1) * H + (H / 4), DXY - 3);
            g.drawString(String.valueOf(i), 13 * H + DXY + (i - 1) * H + (H / 4), DXY - 3);
        }


        //отрисовка игрового поля на основании массива
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                //корабли противника
                if ((enemy.getValuePlacement(i, j) > 0) && enemy.getValueHits(i, j) == Game.NOTSHOOT) {
                    //g.drawImage(paluba, DXY + 13 * H + H * i, DXY + H * j, H, H, null);

                    if (Logic.endGame != 0) {
                        g.drawImage(paluba, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                        // g.drawRect(DXY + 13 * H + H * i, DXY + H * j, H, H);
                    }
                }
                //Если это палуба раненого корабля, то выводим соотвествующее изображение
                else if (enemy.getValueHits(i, j) == Game.HURT) {
                    g.drawImage(hurt, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                } else if (enemy.getValueHits(i, j) == Game.KILLED) {
                    //рисуем палубу убитого корабля
                    g.drawImage(killed, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                } else if (enemy.getValueHits(i, j) == Game.MISS) {
                    //если выстрел мимо и это окружение убитого корабля
                    g.drawImage(miss, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                }

                //корабли игрока
                if (player.getValuePlacement(i, j) > 0 && player.getValueHits(i, j) == Game.NOTSHOOT) {
                    //палуба
                    if (players == 0 || Logic.endGame != 0) {
                        g.drawImage(paluba, DXY + H * i, DXY + H * j, H, H, null);
                        // g.drawRect(DXY + H * i, DXY + H * j, H, H);

                    }
                } else if (player.getValueHits(i, j) == Game.HURT) {
                    //ранен
                    g.drawImage(hurt, DXY + +H * i, DXY + H * j, H, H, null);
                } else if (player.getValueHits(i, j) == Game.KILLED) {
                    //убит
                    g.drawImage(killed, DXY + H * i, DXY + H * j, H, H, null);
                } else if (player.getValueHits(i, j) == Game.MISS) {
                    //мимо
                    g.drawImage(miss, DXY + H * i, DXY + H * j, H, H, null);
                }
            }
        }


        //линии
        for (int i = DXY; i <= DXY + 10 * H; i += H) {
            g2.setStroke(new BasicStroke(1));
            g.setColor(new Color(202, 202, 255));
            g.drawLine(DXY, i, DXY + 10 * H, i); // ----
            g.drawLine(i, DXY, i, DXY + 10 * H);
            g.drawLine(DXY + 13 * H, i, DXY + 23 * H, i); //бот ---
            g.drawLine(i + 13 * H, DXY, i + 13 * H, DXY + 10 * H);

            g2.setStroke(new BasicStroke(2));
            g.setColor(new Color(330099));
            g.drawRect(DXY, DXY, 10 * H, 10 * H);
            g.drawRect(DXY + 13 * H, DXY, 10 * H, 10 * H);
        }

        g.setFont(new Font("Times New Roman", 0, H));
        g.setColor(new Color(330099));

        //количество кораблей игрока
        g.drawRect(DXY, DXY + 11 * H, 4 * H, H);
        ((Graphics2D) g).drawString(String.valueOf(playerShips.countP4), DXY + 5 * H, DXY + 12 * H - (H / 4));
        g.drawRect(DXY, DXY + 12 * H + 10, 3 * H, H);
        ((Graphics2D) g).drawString(String.valueOf(playerShips.countP3), DXY + 4 * H, DXY + 13 * H + 10);
        g.drawRect(DXY, DXY + 13 * H + 20, 2 * H, H);
        ((Graphics2D) g).drawString(String.valueOf(playerShips.countP2), DXY + 3 * H, DXY + 14 * H + 20);
        g.drawRect(DXY, DXY + 14 * H + 30, H, H);
        ((Graphics2D) g).drawString(String.valueOf(playerShips.countP1), DXY + 2 * H, DXY + 15 * H + 30);

        g.drawRect(DXY + 13 * H, DXY + 11 * H, 4 * H, H);//4 палуб
        ((Graphics2D) g).drawString(String.valueOf(enemyShips.countP4), DXY + 18 * H, DXY + 12 * H - (H / 4));
        g.drawRect(DXY + 13 * H, DXY + 12 * H + 10, 3 * H, H);  //3
        ((Graphics2D) g).drawString(String.valueOf(enemyShips.countP3), DXY + 17 * H, DXY + 13 * H + 10);
        g.drawRect(DXY + 13 * H, DXY + 13 * H + 20, 2 * H, H);//2
        ((Graphics2D) g).drawString(String.valueOf(enemyShips.countP2), DXY + 16 * H, DXY + 14 * H + 20);
        g.drawRect(DXY + 13 * H, DXY + 14 * H + 30, 1 * H, H);
        ((Graphics2D) g).drawString(String.valueOf(enemyShips.countP1), DXY + 15 * H, DXY + 15 * H + 30);


        if (Logic.endGame == 0 && player.isEmpty() && rasstanovka || Logic.endGame == 0 && !rasstanovka) {
            g.setFont(new Font("Times New Roman", 0, H - 5));
            if (game.playerHod) {
                if (countOfPlayers == 0) {
                    g.setColor(Color.green);
                    g.drawString("Ход игрока", DXY + 23 * H, DXY + 12 * H - (H / 4));
                } else {
                    g.drawString("Ход игрока 1", DXY + 23 * H, DXY + 12 * H - (H / 4));
                }
            } else {
                if (countOfPlayers == 0) {
                    g.setColor(Color.red);
                    g.drawString("Ход компьютера", DXY + 23 * H, DXY + 12 * H - (H / 4));
                } else {
                    g.drawString("Ход игрока 2", DXY + 23 * H, DXY + 12 * H - (H / 4));
                }
            }
        }
        if (Logic.endGame == 1 || Logic.endGame == 2) {
            timer.stop();
        }
    }

    public void startGame() {
        timer.start();
        game.start();
    }

    public class Mouse implements MouseListener, MouseMotionListener {
        //клавиша мыши нажата
        @Override
        public void mousePressed(MouseEvent e) {
            // Если сделано одиночное нажатие левой клавишей мыши
            if ((e.getButton() == 1) && (e.getClickCount() == 1)) {
                mX = e.getX();
                mY = e.getY();
                if ((rasstanovka && player.isEmpty()) || !rasstanovka
                        && mX > (DXY + 13 * H) && mY > (DXY) && mX < (DXY + 23 * H) && mY < DXY + 10 * H) {
                    //если внутри поля бота и если не конец игры и ход игрока
                    if (game.playerHod && Logic.endGame == 0 && !game.enemyHod) {
                        //то вычисляем элемент массива:
                        int i = (mX - (DXY + 13 * H)) / H;
                        int j = (mY - DXY) / H;
                        if (Logic.testMasOut(i, j)) {
                            if (enemy.getValueHits(i, j) == Game.NOTSHOOT) {
                                game.playerHodit(i, j, playerShips, enemyShips, players);
                            }
                        }
                    }
                }
                if (players != 0) {
                    if ((rasstanovka && enemy.isEmpty()) || !rasstanovka
                            && mX > (DXY + 0 * H) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                        //если внутри поля бота и если не конец игры и ход игрока
                        if (game.enemyHod && Logic.endGame == 0 && !game.playerHod) {
                            //то вычисляем элемент массива:
                            int i = (mX - (DXY + 0 * H)) / H;
                            int j = (mY - DXY) / H;
                            if (Logic.testMasOut(i, j)) {
                                if (player.getValueHits(i, j) == Game.NOTSHOOT) {
                                    game.enemyHodit(i, j, playerShips, players);
                                }
                            }
                        }
                    }
                }


            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}