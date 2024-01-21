package com.company;


public class AutoPlacement {

    public static void setPalubaAuto(Placement player, Ships ships) {
        player.setMatrix(new int[10][10]);
        setPalubaAutoForOneShip(player.getMatrix(), ships.p4);
        for (int i = 1; i <= 2; i++) {
            setPalubaAutoForOneShip(player.getMatrix(), ships.p3);
        }
        for (int i = 1; i <= 3; i++) {
            setPalubaAutoForOneShip(player.getMatrix(), ships.p2);
        }
        for (int i = 1; i <= 4; i++) {
            setPalubaAutoForOneShip(player.getMatrix(), ships.p1);
        }
    }

    public static void setPalubaAutoForOneShip(int[][] mas, int kolPal) {
        int i, j;
        while (true) {
            boolean flag = false;
            i = (int) (Math.random() * 10);
            j = (int) (Math.random() * 10);
            int napr = (int) (Math.random() * 4); // 0 - вверх. 1 - вправо. 2 - вниз. 3 - влево

            // Если можно расположить палубу
            if (Logic.testNewPaluba(mas, i, j)) {
                if (napr == 0) { //вверх
                    if (Logic.testNewPaluba(mas, i - (kolPal - 1), j))  //если можно расположить палубу вверх, то flag = true
                        flag = true;
                } else if (napr == 1) { // вправо
                    if (Logic.testNewPaluba(mas, i, j + (kolPal - 1)))
                        flag = true;
                } else if (napr == 2) { // вниз
                    if (Logic.testNewPaluba(mas, i + (kolPal - 1), j))
                        flag = true;
                } else if (napr == 3) { // влево
                    if (Logic.testNewPaluba(mas, i, j - (kolPal - 1)))
                        flag = true;
                }
            }
            if (flag == true) {
                if (napr == 0) {// вверх
                    for (int k = 0; k < kolPal; k++) {
                        mas[i - k][j] = kolPal;
                    }
                    for (int k = 0; k < kolPal; k++) {
                        Logic.Okr(mas, i - k, j, Placement.SUR);
                    }
                } else if (napr == 1) { // вправо
                    for (int k = 0; k < kolPal; k++) {
                        mas[i][j + k] = kolPal;
                    }
                    for (int k = 0; k < kolPal; k++) {
                        Logic.Okr(mas, i, j + k, Placement.SUR);
                    }
                } else if (napr == 2) { // вниз
                    for (int k = 0; k < kolPal; k++) {
                        mas[i + k][j] = kolPal;
                    }
                    for (int k = 0; k < kolPal; k++) {
                        Logic.Okr(mas, i + k, j, Placement.SUR);
                    }
                } else if (napr == 3) { // влево
                    for (int k = 0; k < kolPal; k++) {
                        mas[i][j - k] = kolPal;
                    }
                    for (int k = 0; k < kolPal; k++) {
                        Logic.Okr(mas, i, j - k, Placement.SUR);
                    }
                }
                //прерываем цикл
                break;
            }
        }
    }
}
