import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day11Part1 {
    public static void main(String[] args) throws IOException {
        Instant inst1 = Instant.now();
        List<List<Integer>> universe = new ArrayList<>();

        final AtomicInteger planet = new AtomicInteger(1);
        try (BufferedReader br = new BufferedReader(new FileReader("day11/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                universe.add(line.chars().map((s -> s == '#' ? (planet.getAndAdd(1)) : 0)).boxed().collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
            }
        }

        printUniverse(universe);
        List<List<Integer>> expandedUniverse = expandTheUniverse(universe);
        printUniverse(expandedUniverse);

        // Create the pairs
        int maxPlanet = planet.get();
        Set<Distance> distances = new HashSet<>();
        for (int i = 1; i < maxPlanet; i++) {
            for (int j = 1; j < maxPlanet; j++) {
                if (i == j) {
                    continue;
                }
                distances.add(new Distance(i, j));
            }
        }

        int sumdistance = 0;
        for (Distance d: distances) {
            int[] coordinatesOne = getCoordinates(expandedUniverse, d.planetOne);
            int[] coordinatesTwo = getCoordinates(expandedUniverse, d.planetTwo);

            int deltaX = Math.abs(coordinatesOne[0] - coordinatesTwo[0]);
            int deltaY = Math.abs(coordinatesOne[1] - coordinatesTwo[1]);

            int distance = Math.min(deltaX, deltaY) == 0 ? deltaY == 0 ? deltaX : deltaY : deltaX+deltaY;
            sumdistance += distance;
            System.out.println(d.planetOne + "->" + d.planetTwo + ":" + deltaX + "," + deltaY + "===" + distance);
        }

        System.out.println(sumdistance);
        Instant inst2 = Instant.now();
        System.out.println("Elapsed Time: " + Duration.between(inst1, inst2).toString());
    }

    private static int[] getCoordinates(List<List<Integer>> universe, int planet) {
        for (int y = 0; y < universe.size(); y++) {
            for (int x = 0; x < universe.get(y).size(); x++) {
                if (universe.get(y).get(x) == planet) {
                    return new int[]{x,y};
                }
            }
        }

        throw new RuntimeException("Planet " + planet + " does not exist in universe");
    }

    private static void printUniverse(List<List<Integer>> universe) {
        System.out.println("Universe looks like:");
        for (List<Integer> row : universe) {
            for (Integer col : row) {
                System.out.print(col);
            }

            System.out.println();
        }
    }

    public static List<List<Integer>> expandTheUniverse(List<List<Integer>> universe) {
        List<List<Integer>> expandedUniverse = new ArrayList<>(universe);
        for (int y = 0; y < expandedUniverse.size(); y++) {
            if (expandedUniverse.get(y).stream().mapToInt(Integer::intValue).sum() == 0) {
                expandedUniverse.add(y + 1, IntStream.range(0, expandedUniverse.get(y).size()).mapToObj(i -> 0).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
                y++; // Skip the next line because otherwise we expand into infinity
            }
        }

        for (int x = 0; x < expandedUniverse.getFirst().size(); x++) {
            int finalX = x;
            if (expandedUniverse.stream().mapToInt(row -> row.get(finalX)).sum() == 0) {
                for (int y = 0; y < expandedUniverse.size(); y++) {
                    expandedUniverse.get(y).add(finalX + 1, 0);
                }

                x++;
            }
        }

        return expandedUniverse;
    }

    static class Distance {
        private final int planetOne;
        private final int planetTwo;

        Distance(int planetOne, int planetTwo) {
            if (planetOne == planetTwo) {
                throw new RuntimeException("Distance between 2 planets should not be measured");
            }
            this.planetOne = Math.min(planetOne, planetTwo);
            this.planetTwo = Math.max(planetOne, planetTwo);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Distance distance1 = (Distance) o;
            return planetOne == distance1.planetOne && planetTwo == distance1.planetTwo;
        }

        @Override
        public int hashCode() {
            return Objects.hash(planetOne, planetTwo);
        }

        @Override
        public String toString() {
            return "Distance{" +
                    "planetOne=" + planetOne +
                    ", planetTwo=" + planetTwo +
                    '}';
        }
    }
}