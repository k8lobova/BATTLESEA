package com.company;

import javax.swing.*;

public class Logic {
    public static int endGame = 3;
    public static void testEndGame(Placement player, Placement enemy, int players) {
        if (endGame == 0) {
            int sumEnd = 20; //когда все корабли убиты
            int sumPlay = 0; // Сумма убитых палуб игрока
            int sumComp = 0; // Сумма убитых палуб компьютера
            if (endGame == 0) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        // Суммируем подбитые палубы
                        if (player.getValueHits(i, j) > 0) sumPlay++;
                        if (enemy.getValueHits(i, j) > 0) sumComp++;
                    }
                }
                if (sumComp == sumEnd) {
                    endGame = 1;
                    if (players == 0) {
                        JOptionPane.showMessageDialog(null,
                                "Поздравляем! Вы выиграли!",
                                "Вы выиграли", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Выиграл Игрок 1",
                                "Поздравляем!", JOptionPane.INFORMATION_MESSAGE);
                    }


                } else if (sumPlay == sumEnd) {
                    endGame = 2;
                    if (players == 0) {
                        JOptionPane.showMessageDialog(null,
                                "Вы проиграли! Попробуйте еще раз",
                                "Вы проиграли", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Выиграл Игрок 2",
                                "Поздравляем!", JOptionPane.INFORMATION_MESSAGE);
                        ;
                    }

                }
            }
        }

    }

    //перебираем все ячейки вокруг и устанавливает в них нужное значение.
    public static void Okr(int[][] mas, int i, int j, int val) {
        setOkr(mas, i - 1, j - 1, val);
        setOkr(mas, i - 1, j, val);
        setOkr(mas, i - 1, j + 1, val);
        setOkr(mas, i, j + 1, val);
        setOkr(mas, i, j - 1, val);
        setOkr(mas, i + 1, j + 1, val);
        setOkr(mas, i + 1, j, val);
        setOkr(mas, i + 1, j - 1, val);
    }

    // вспомогательный метод для установки окружения корабля, который контролирует выход за пределы массива
    private static void setOkr(int[][] mas, int i, int j, int val) {
        if (testMasOut(i, j) && mas[i][j] == 0) {
            mas[i][j] = val;
        }
    }

    public static boolean testMasOut(int i, int j) {
        if (((i >= 0) && (i <= 9)) && ((j >= 0) && (j <= 9))) {
            return true;
        } else return false;
    }


    public static boolean testNewPaluba(int[][] mas, int i, int j) {
        if (testMasOut(i, j) == false) return false;
        if (mas[i][j] == 0) return true;
        else return false;
    }
}