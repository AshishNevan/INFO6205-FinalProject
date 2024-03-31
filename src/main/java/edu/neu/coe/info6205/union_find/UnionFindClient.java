package edu.neu.coe.info6205.union_find;
import java.util.Random;

public class UnionFindClient {
    public static int count(int n) {
        UF_HWQUPC uf = new UF_HWQUPC(n);
        int connections = 0;

        Random rand = new Random();
        while (uf.components() != 1) {
            int i = rand.nextInt(n), j = rand.nextInt(n);
            if (!uf.connected(i, j)) {
                uf.union(i, j);
                connections++;
            }
        }
        return connections;
    }

    public static void main(String[] args) {
        for (int n = 500; n <= 64000; n *= 2) {
            int connections = count(n);
            System.out.println("N = " + n + " Connections = " + connections);
        }
    }
}