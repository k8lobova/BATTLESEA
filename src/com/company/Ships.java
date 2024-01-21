package com.company;

public class Ships {

    public int p1, p2, p3, p4, countP1, countP2, countP3, countP4;

    public Ships() {
        this.p1 = 1;
        this.p2 = 2;
        this.p3 = 3;
        this.p4 = 4;
        this.countP1 = 4;
        this.countP2 = 3;
        this.countP3 = 2;
        this.countP4 = 1;
    }

    public boolean isOver() {
        return (countP1 == 0 && countP2 == 0 && countP3 == 0 && countP4 == 0);
    }
}
