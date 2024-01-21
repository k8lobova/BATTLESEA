package com.company;

public class Game {

    public static final int NOTSHOOT = 0;
    public static final int MISS = -1;
    public static final int HURT = 1;
    public static final int KILLED = 2;

    public int kolHodPlayer;
    public int kolHodEnemy;
    private final int pause = 500;
    public boolean playerHod;
    public boolean enemyHod;
    private Placement player, enemy;

    public Game(Placement player, Placement enemy) {
        this.enemy = enemy;
        this.player = player;
    }

    public void start() {
        kolHodEnemy = 0;
        kolHodPlayer = 0;
        playerHod = true; //мой ход
        enemyHod = false;
        Logic.endGame = 0;
    }

    public void playerHodit(int i, int j, Ships playerShip, Ships enemyShip, int players) {
        kolHodPlayer++;
        if (enemy.getValuePlacement(i, j) > Placement.EMPTY) {
            enemy.setValueHits(i, j, HURT);
            testKilled(enemy, i, j, enemyShip);
            Logic.testEndGame(player, enemy, players);
            if (players != 0) {
                playerHod = true;
                enemyHod = false;
            }
        } else {
            enemy.setValueHits(i, j, MISS);
            if (players != 0) {
                playerHod = false;
                enemyHod = true;
            }
        }
        if (players == 0) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //если промах
                    if (enemy.getValuePlacement(i, j) <= 0) {
                        playerHod = false;
                        enemyHod = true;
                        //передаем ход компьютеру
                        // Ходит компьютер - пока попадает в цель
                        while (enemyHod == true) {
                            try {
                                Thread.sleep(pause);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            enemyHod = compHodit(player, playerShip, players);
                        }
                        playerHod = true;//передаем ход игроку после промаха компьютера
                    }
                }
            });
            thread.start();
        }
    }

    public void enemyHodit(int i, int j, Ships playerShip, int players) {
        kolHodEnemy++;
        if (player.getValuePlacement(i, j) > 0) {
            player.setValueHits(i, j, HURT);
            testKilled(player, i, j, playerShip);
            Logic.testEndGame(player, enemy, players);
            enemyHod = true;
            playerHod = false;
        } else {
            player.setValueHits(i, j, MISS);
            enemyHod = false;
            playerHod = true;
        }

    }

    private boolean compHodit(Placement player, Ships ships, int players) {
        if (Logic.endGame == 0 && enemyHod) {
            kolHodEnemy++;
            //if (enemyHod) kolHodEnemy++;
            // Признак попадания в цель
            boolean popal = false;
            // Признак выстрела в раненый корабль
            boolean ranen = false;
            //признак направления корабля
            boolean horiz = false;
            _for1:
            // break метка
            // Пробегаем все игровое поле игрока
            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                    //если находим раненую палубу
                    if (player.getValueHits(i, j) == HURT) {
                        ranen = true;
                        //ищем подбитую палубу слева и справа
                        if ((Logic.testMasOut(i - 1, j) && player.getValueHits(i - 1, j) == HURT)
                                ||(Logic.testMasOut(i + 1, j) && player.getValueHits(i + 1, j) == HURT)) {
                            horiz = true;
                            //ищем подбитую палубу сверху и снизу
                        } else if ((Logic.testMasOut(i, j - 1) && player.getValueHits(i, j - 1) == HURT)
                                || (Logic.testMasOut(i, j + 1) && player.getValueHits(i, j + 1) == HURT)) {
                            horiz = false;
                        }
                        //если не удалось определить направление корабля, то выбираем произвольное направление для обстрела
                        else for (int x = 1; x <= 100; x++) {
                                int napr = (int) (Math.random() * 4);
                                if (napr == 0 && Logic.testMasOut(i - 1, j) && player.getValueHits(i - 1, j) == NOTSHOOT) {
                                    //проверяем, убили или нет
                                    Shot(i - 1, j, player, ships);
                                    if ((player.getValueHits(i - 1, j) == HURT) || (player.getValueHits(i - 1, j) == KILLED))
                                        popal = true;
                                    //прерываем цикл
                                    break _for1;
                                } else if (napr == 1 && Logic.testMasOut(i + 1, j) && player.getValueHits(i + 1, j) == NOTSHOOT) {
                                    Shot(i + 1, j, player, ships);
                                    if ((player.getValueHits(i + 1, j) == HURT) || (player.getValueHits(i + 1, j) == KILLED))
                                        popal = true;
                                    break _for1;
                                } else if (napr == 2 && Logic.testMasOut(i, j - 1) && player.getValueHits(i, j - 1) == NOTSHOOT) {
                                    Shot(i, j - 1, player, ships);
                                    if ((player.getValueHits(i, j - 1) == HURT) || (player.getValueHits(i, j - 1) == KILLED))
                                        popal = true;
                                    break _for1;
                                } else if (napr == 3 && Logic.testMasOut(i, j + 1) && player.getValueHits(i, j + 1) == NOTSHOOT) {
                                    Shot(i, j + 1, player, ships);
                                    if ((player.getValueHits(i, j + 1) == HURT) || (player.getValueHits(i, j + 1) == KILLED))
                                        popal = true;
                                    break _for1;
                                }
                            }

                        //если определили направление, то производим выстрел только в этом напрвлении
                        if (horiz) { //по горизонтали
                            if (Logic.testMasOut(i - 1, j) && player.getValueHits(i - 1, j) == NOTSHOOT) {
                                Shot(i - 1, j, player, ships);
                                if ((player.getValueHits(i - 1, j) == HURT) || (player.getValueHits(i - 1, j) == KILLED))
                                    popal = true;
                                break _for1;
                            } else if (Logic.testMasOut(i + 1, j) && player.getValueHits(i + 1, j) == NOTSHOOT) {
                                Shot(i + 1, j, player, ships);
                                if ((player.getValueHits(i + 1, j) == HURT) || (player.getValueHits(i + 1, j) == KILLED))
                                    popal = true;
                                break _for1;
                            }
                        }//по вертикали
                        else if (Logic.testMasOut(i, j - 1) && player.getValueHits(i, j - 1) == NOTSHOOT) {
                            Shot(i, j - 1, player, ships);
                            if ((player.getValueHits(i, j - 1) == HURT) || (player.getValueHits(i, j - 1) == KILLED))
                                popal = true;
                            break _for1;
                        } else if (Logic.testMasOut(i, j + 1) && player.getValueHits(i, j + 1) == NOTSHOOT) {
                            Shot(i, j + 1, player, ships);
                            if ((player.getValueHits(i, j + 1) == HURT) || (player.getValueHits(i, j + 1) == KILLED))
                                popal = true;
                            break _for1;
                        }
                    }

            //если нет ранненых кораблей*/
            if (ranen == false) {
                // делаем 100 случайных попыток выстрела
                // в случайное место
                for (int l = 1; l <= 100; l++) {
                    // Находим случайную позицию на игровом поле
                    int i = (int) (Math.random() * 10);
                    int j = (int) (Math.random() * 10);
                    //Проверяем, что можно сделать выстрел
                    if (player.getValueHits(i, j) == NOTSHOOT) {
                        //делаем выстрел
                        Shot(i, j, player, ships);
                        if (player.getValueHits(i, j) == HURT)  popal = true;
                        break;
                    }
                }
            }
            // проверяем конец игры
            Logic.testEndGame(this.player, enemy, players);
            // возвращаем результат
            return popal;
        } else return false;
    }

    private void Shot(int i, int j, Placement player, Ships ships){
        if (player.getValuePlacement(i, j) > 0) {
            player.setValueHits(i, j, HURT);
            testKilled(player, i, j, ships);
        } else {
            player.setValueHits(i, j, MISS);
        }
    }

    private void testKilled(Placement player, int i, int j, Ships ships) {
        if (player.getValuePlacement(i, j) == 1) { //Если однопалубный
            player.setValueHits(i, j, KILLED);
            ships.countP1--;
            setOkrKilled(player.getMatrixForHits(), i, j);
        } else if (player.getValuePlacement(i, j) >= 2) {
            testKilledForOneShip(player, i, j, ships);
        }
    }
    private void testKilledForOneShip(Placement player, int i, int j, Ships ships) {

        int kolPalub = player.getValuePlacement(i, j);
        //Количество раненых палуб
        /*int kolRanen = -1;
        int curPalub = kolPalub - 1;
        //Выполняем подсчет раненых палуб
        for (int x = i - curPalub ; x <= i + curPalub; x++) {
            if (Logic.testMasOut(x, j) && (player.getValueHits(x, j) == HURT) && player.getValuePlacement(x, j) == kolPalub)
                kolRanen++;
        }
        for (int y = j - curPalub; y <= j + curPalub; y++) {
            if (Logic.testMasOut(i, y) && (player.getValueHits(i, y) == HURT) && player.getValuePlacement(i, y) == kolPalub)
                kolRanen++;
        }*/
        int kolRanen = -3;
        int curPalub = kolPalub - 1;
        //Выполняем подсчет раненых палуб
        for (int x = i ; x >= i - curPalub; x--) {
            if (Logic.testMasOut(x, j) && (player.getValueHits(x, j) == HURT))
                kolRanen++;
            else break;
        }
        for (int x = i ; x <= i + curPalub; x++) {
            if (Logic.testMasOut(x, j) && (player.getValueHits(x, j) == HURT))
                kolRanen++;
            else break;

        }
        for (int y = j; y >= j - curPalub; y--) {
            if (Logic.testMasOut(i, y) && (player.getValueHits(i, y) == HURT))
                kolRanen++;
            else break;
        }
        for (int y = j ; y <= j + curPalub; y++) {
            if (Logic.testMasOut(i, y) && (player.getValueHits(i, y) == HURT))
                kolRanen++;
            else break;
        }

        // Если количество раненых палуб совпадает с количеством палуб
        if (kolRanen == kolPalub) {
            /*for (int x = i - curPalub ; x <= i + curPalub; x++) {
                if (Logic.testMasOut(x, j) && (player.getValueHits(x, j) == HURT) && player.getValuePlacement(x, j) == kolPalub)
                    if (Logic.testMasOut(x, j) && player.getValueHits(x, j) == HURT) {
                        // помечаем палубой убитого корабля
                        player.setValueHits(x, j, KILLED);
                        setOkrKilled(player.getMatrixForHits(), x, j);
                    }
            }

            for (int y = j - curPalub ; y <= j + curPalub; y++) {
                if (Logic.testMasOut(i, y) && (player.getValueHits(i, y) == HURT) && player.getValuePlacement(i, y) == kolPalub)
                    if (Logic.testMasOut(i, y) && player.getValueHits(i, y) == HURT) {
                        player.setValueHits(i, y, KILLED);
                        setOkrKilled(player.getMatrixForHits(), i, y);
                    }
            }*/
            for (int x = i ; x >= i - curPalub; x--) {
                if (Logic.testMasOut(x, j) && player.getValueHits(x, j) == HURT) {
                    // помечаем палубой убитого корабля
                    player.setValueHits(x, j, KILLED);
                    setOkrKilled(player.getMatrixForHits(), x, j);
                }

            }
            for (int x = i ; x <= i + curPalub; x++) {
                if (Logic.testMasOut(x, j) && player.getValueHits(x, j) == HURT) {
                    // помечаем палубой убитого корабля
                    player.setValueHits(x, j, KILLED);
                    setOkrKilled(player.getMatrixForHits(), x, j);
                }

            }
            for (int y = j; y >= j - curPalub; y--) {
                if (Logic.testMasOut(i, y) && player.getValueHits(i, y) == HURT) {
                    player.setValueHits(i, y, KILLED);
                    setOkrKilled(player.getMatrixForHits(), i, y);
                }
            }
            for (int y = j ; y <= j + curPalub; y++) {
                if (Logic.testMasOut(i, y) && player.getValueHits(i, y) == HURT) {
                    player.setValueHits(i, y, KILLED);
                    setOkrKilled(player.getMatrixForHits(), i, y);
                }
            }

            if (kolPalub == ships.p4) {
                ships.countP4--;
            } else if (kolPalub == ships.p3) {
                ships.countP3--;
            } else if (kolPalub == ships.p2) {
                ships.countP2--;
            }
        }
    }

    private void setOkrKilled(int[][] mas, int i, int j) {
        setOkrForOneNapr(mas, i - 1, j - 1); // сверху слева
        setOkrForOneNapr(mas, i - 1, j); // сверху
        setOkrForOneNapr(mas, i - 1, j + 1); // сверху справа
        setOkrForOneNapr(mas, i, j + 1); // справа
        setOkrForOneNapr(mas, i + 1, j + 1); // снизу справа
        setOkrForOneNapr(mas, i + 1, j); // снизу
        setOkrForOneNapr(mas, i + 1, j - 1); // снизу слева
        setOkrForOneNapr(mas, i, j - 1); // слева
    }

    public void setOkrForOneNapr(int mas[][], int i, int j) {
        if (Logic.testMasOut(i, j)) {
            if (mas[i][j] == NOTSHOOT) {
                mas[i][j] = MISS;
            }
        }
    }

}