package edu.neu.coe.info6205.threesum;

import edu.neu.coe.info6205.util.Stopwatch;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import static org.junit.Assert.assertEquals;

public class ThreeSumTest {

    @Test
    public void testGetTriplesJ0() {
        int[] ints = new int[]{-2, 0, 2};
        ThreeSumQuadratic target = new ThreeSumQuadratic(ints);
        List<Triple> triples = target.getTriples(1);
        assertEquals(1, triples.size());
    }

    @Test
    public void testGetTriplesJ1() {
        int[] ints = new int[]{30, -40, -20, -10, 40, 0, 10, 5};
        Arrays.sort(ints);
        ThreeSumQuadratic target = new ThreeSumQuadratic(ints);
        List<Triple> triples = target.getTriples(3);
        assertEquals(2, triples.size());
    }

    @Test
    public void testGetTriplesJ2() {
        Supplier<int[]> intsSupplier = new Source(10, 15, 2L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSumQuadratic target = new ThreeSumQuadratic(ints);
        List<Triple> triples = target.getTriples(5);
        assertEquals(1, triples.size());
    }

    @Test
    public void testGetTriples0() {
        int[] ints = new int[]{30, -40, -20, -10, 40, 0, 10, 5};
        Arrays.sort(ints);
        System.out.println("ints: " + Arrays.toString(ints));
        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        System.out.println("triples: " + Arrays.toString(triples));
        assertEquals(4, triples.length);
        assertEquals(4, new ThreeSumCubic(ints).getTriples().length);
    }

    @Test
    public void testGetTriples1() {
        Supplier<int[]> intsSupplier = new Source(20, 20, 1L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        assertEquals(4, triples.length);
        System.out.println(Arrays.toString(triples));
        Triple[] triples2 = new ThreeSumCubic(ints).getTriples();
        System.out.println(Arrays.toString(triples2));
        assertEquals(4, triples2.length);
    }

    @Test
    public void testGetTriples2() {
        Supplier<int[]> intsSupplier = new Source(10, 15, 3L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadratic(ints);
        System.out.println(Arrays.toString(ints));
        Triple[] triples = target.getTriples();
        System.out.println(Arrays.toString(triples));
        assertEquals(1, triples.length);
        assertEquals(1, new ThreeSumCubic(ints).getTriples().length);
    }

    @Ignore // Slow
    public void testGetTriples3() {
        Supplier<int[]> intsSupplier = new Source(1000, 1000).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triplesQuadratic = target.getTriples();
        Triple[] triplesCubic = new ThreeSumCubic(ints).getTriples();
        int expected1 = triplesCubic.length;
        assertEquals(expected1, triplesQuadratic.length);
    }

    @Ignore // Slow
    public void testGetTriples4() {
        Supplier<int[]> intsSupplier = new Source(1500, 1000).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triplesQuadratic = target.getTriples();
        Triple[] triplesCubic = new ThreeSumCubic(ints).getTriples();
        int expected1 = triplesCubic.length;
        assertEquals(expected1, triplesQuadratic.length);
    }

    @Test
    public void testGetTriplesC0() {
        int[] ints = new int[]{30, -40, -20, -10, 40, 0, 10, 5};
        Arrays.sort(ints);
        System.out.println("ints: " + Arrays.toString(ints));
        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        System.out.println("triples: " + Arrays.toString(triples));
        assertEquals(4, triples.length);
        assertEquals(4, new ThreeSumCubic(ints).getTriples().length);
    }

    @Test
    public void testGetTriplesC1() {
        Supplier<int[]> intsSupplier = new Source(20, 20, 1L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadraticWithCalipers(ints);
        Triple[] triples = target.getTriples();
        assertEquals(4, triples.length);
        System.out.println(Arrays.toString(triples));
        Triple[] triples2 = new ThreeSumCubic(ints).getTriples();
        System.out.println(Arrays.toString(triples2));
        assertEquals(4, triples2.length);
    }

    @Test
    public void testGetTriplesC2() {
        Supplier<int[]> intsSupplier = new Source(10, 15, 3L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadraticWithCalipers(ints);
        System.out.println(Arrays.toString(ints));
        Triple[] triples = target.getTriples();
        System.out.println(Arrays.toString(triples));
        assertEquals(1, triples.length);
        assertEquals(1, new ThreeSumCubic(ints).getTriples().length);
    }

    @Test
    public void testGetTriplesC3() {
        Supplier<int[]> intsSupplier = new Source(1000, 1000).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadraticWithCalipers(ints);
        Triple[] triplesQuadratic = target.getTriples();
        Triple[] triplesCubic = new ThreeSumCubic(ints).getTriples();
        assertEquals(triplesCubic.length, triplesQuadratic.length);
    }

    @Test
    public void testGetTriplesC4() {
        Supplier<int[]> intsSupplier = new Source(1500, 1000).intsSupplier(10);
        int[] ints = intsSupplier.get();
        ThreeSum target = new ThreeSumQuadraticWithCalipers(ints);
        Triple[] triplesQuadratic = target.getTriples();
        Triple[] triplesCubic = new ThreeSumCubic(ints).getTriples();
        assertEquals(triplesCubic.length, triplesQuadratic.length);
    }

    @Test
    public void stopwatchQuadratic() {

        List<ThreeSum> listOfQuadraticCalipers = new ArrayList<>();
        List<ThreeSum> listOfQuadratic = new ArrayList<>();
        List<ThreeSum> listOfQuadrithmic = new ArrayList<>();
        List<ThreeSum> listOfCubic = new ArrayList<>();

        for (int i=250;i<=5000;i*=2) {
            Supplier<int[]> intsSupplier = new Source(i, 1000).intsSupplier(10);
            int[] ints = intsSupplier.get();
            listOfQuadraticCalipers.add(new ThreeSumQuadraticWithCalipers(ints));
            listOfQuadratic.add(new ThreeSumQuadratic(ints));
            listOfQuadrithmic.add(new ThreeSumQuadrithmic(ints));
            listOfCubic.add(new ThreeSumCubic(ints));
        }

        System.out.printf("----------------------------------------------------------%n");
        System.out.printf("| %-30s | %10s |%10s|\n", "Algorithm", "input size", "Time in ms");
        System.out.printf("----------------------------------------------------------%n");
        String algoName = "Quadratic with Calipers";
        int count = 250;
        for (ThreeSum target: listOfQuadraticCalipers) {
            try (Stopwatch sw = new Stopwatch()) {
                target.getTriples();
                System.out.printf("| %-30s | %10s |%10s|\n", algoName, count,sw.lap());
            }
            count*=2;
        }

        System.out.printf("----------------------------------------------------------%n");
        algoName = "Quadratic";
        count = 250;
        for (ThreeSum target: listOfQuadratic) {
            try (Stopwatch sw = new Stopwatch()) {
                target.getTriples();
                System.out.printf("| %-30s | %10s |%10s|\n", algoName, count,sw.lap());
            }
            count*=2;
        }

        System.out.printf("----------------------------------------------------------%n");
        algoName = "Quadrithmic";
        count = 250;
        for (ThreeSum target: listOfQuadrithmic) {
            try (Stopwatch sw = new Stopwatch()) {
                target.getTriples();
                System.out.printf("| %-30s | %10s |%10s|\n", algoName, count,sw.lap());
            }
            count*=2;
        }

        System.out.printf("----------------------------------------------------------%n");
        algoName = "Cubic";
        count = 250;
        for (ThreeSum target: listOfCubic) {
            try (Stopwatch sw = new Stopwatch()) {
                target.getTriples();
                System.out.printf("| %-30s | %10s |%10s|\n", algoName, count,sw.lap());
            }
            count*=2;
        }
        System.out.printf("----------------------------------------------------------%n");
    }
}