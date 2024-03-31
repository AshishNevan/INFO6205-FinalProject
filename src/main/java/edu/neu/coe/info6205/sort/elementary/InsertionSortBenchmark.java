package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class InsertionSortBenchmark {
    public static void main(String[] args) throws Exception {
        int nRuns = 100;
        int[] arrayOfN = new int[]{1000,2000,4000,8000,16000};

        Consumer<Integer[]> InsertionSort = (Integer[] xs) -> new InsertionSort<Integer>().sort(xs);

        for(int n: arrayOfN) {
            System.out.println("N: "+n);

            Benchmark_Timer<Integer[]> InsertionSortRandomBM = new Benchmark_Timer<>("Random Order of size "+n, InsertionSort);
            double meanRunTime = InsertionSortRandomBM.runFromSupplier(createRandomSupplier.apply(n),nRuns);

            Benchmark_Timer<Integer[]> InsertionSortReverseSortedBM = new Benchmark_Timer<>("Reverse Sorted Order of size "+n, InsertionSort);
            double meanRunTime3 = InsertionSortReverseSortedBM.runFromSupplier(createReverseSortedSupplier.apply(n),nRuns);

            Benchmark_Timer<Integer[]> InsertionSortSortedBM = new Benchmark_Timer<>("Sorted Order of size "+n, InsertionSort);
            double meanRunTime2 = InsertionSortSortedBM.runFromSupplier(createSortedSupplier.apply(n),nRuns);

            Benchmark_Timer<Integer[]> InsertionSortPartiallySortedBM = new Benchmark_Timer<>("Partially Sorted Order of size "+n, InsertionSort);
            double meanRunTime4 = InsertionSortPartiallySortedBM.runFromSupplier(createPartiallySortedSupplier.apply(n),nRuns);

            System.out.println("Runtime on Random Order of size "+n+":\t"+meanRunTime);
            System.out.println("Sorted Order of size "+n+":\t"+meanRunTime2);
            System.out.println("Reverse Order of size "+n+":\t"+meanRunTime3);
            System.out.println("Partially Sorted Order of size "+n+":\t"+meanRunTime4);
        }
    }

    public static Function<Integer, Supplier<Integer[]>> createRandomSupplier = (Integer n) -> () -> {
        Integer[] xs = new Integer[n];
        Random random = new Random(0L);
        for (int i = 0 ;i < n; i++) {
            xs[i] = random.nextInt(10000);
        }
        return xs;
    };

    public static Function<Integer, Supplier<Integer[]>> createSortedSupplier = (Integer n) -> () -> {
        Integer[] xs = new Integer[n];
        for (int i = 0 ;i < n; i++) {
            xs[i] = i;
        }
        return xs;
    };

    public static Function<Integer, Supplier<Integer[]>> createReverseSortedSupplier = (Integer n) -> () -> {
        Integer[] xs = new Integer[n];
        for (int i = 0 ;i < n; i++) xs[n - 1 - i] = i;
        return xs;
    };

    public static Function<Integer, Supplier<Integer[]>> createPartiallySortedSupplier = (Integer n) -> () -> {
        Integer[] xs = new Integer[n];
        for (int i = 0 ;i < n/2; i++) {
            xs[i] = i;
        }
        Random random = new Random(0L);
        for (int i = n/2; i < n; i++) {
            xs[i] = random.nextInt();
        }
        return xs;
    };

}
