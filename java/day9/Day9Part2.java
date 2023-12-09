import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day9Part2 {
    public static void main(String[] args) throws IOException {
        Instant inst1 = Instant.now();
        List<List<Integer>> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("day9/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                values.add(Arrays.stream(line.split(" ")).map((Integer::parseInt)).toList());
            }

            System.out.println(values.stream().map(Day9Part2::solve).mapToInt(Day9Part2.Pair::addValue).sum());
        }
        Instant inst2 = Instant.now();
        System.out.println("Elapsed Time: "+ Duration.between(inst1, inst2).toString());
    }

    public static Pair solve(List<Integer> list) {
        if (list.stream().allMatch(n -> n.equals(list.getFirst()))) {
            return new Pair(1, list.getFirst());
        } else {
            Pair p = solve(IntStream.range(0, list.size() - 1).mapToObj(i -> list.get(i + 1) - list.get(i)).toList());
            return new Pair(p.count + 1, list.getFirst() - p.addValue);
        }
    }

    public record Pair(int count, int addValue) { }
}