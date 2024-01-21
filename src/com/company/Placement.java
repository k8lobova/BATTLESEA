package com.company;

public class Placement {

    public static final int EMPTY = 0;
    public static final int SUR = -1;
    private int[][] matrix; //как стоят палубы
    private int[][] matrixForHits; //стреляли по нам

    public Placement() {
        this.matrix = new int[10][10];
        this.matrixForHits = new int[10][10];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void setValueHits(int i, int j, int value) {
        matrixForHits[i][j] = value;
    }

    public int[][] getMatrixForHits() {
        return matrixForHits;
    }

    public int getValuePlacement(int i, int j) {
        return matrix[i][j];
    }

    public int getValueHits(int i, int j) {
        return matrixForHits[i][j];
    }

    public boolean isEmpty() {
        for (int[] m : matrix) {
            for (int v : m) {
                if (v != EMPTY) return false;
            }
        }
        return true;
    }

}
