import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day13Part1 {
    public static void main(String[] args) throws IOException {
        Instant inst1 = Instant.now();
        List<List<List<Character>>> fields = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("day13/input.txt"))) {
            String line;
            List<List<Character>> field = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    fields.add(field);
                    field = new ArrayList<>();
                } else {
                    field.add(line.chars().boxed().map(c -> (char) c.intValue()).toList());
                }
            }

            fields.add(field);
        }

        int sum = 0;

        int index = 0;
        for (List<List<Character>> field : fields) {
            boolean hasMatch = false;
            for (int row = 1; row < field.size() && !hasMatch; row++) {
                List<List<Character>> above = IntStream.range(0, row).mapToObj(field::get).toList();
                List<List<Character>> below = IntStream.range(row, field.size()).mapToObj(field::get).toList();

                if (Math.max(above.size(), below.size()) == 0) {
                    continue;
                }

                List<List<Character>> aboveReversed = new ArrayList<>(above);
                Collections.reverse(aboveReversed);
                for (int i = 0; i < Math.min(aboveReversed.size(), below.size()); i++) {
                    if (!aboveReversed.get(i).equals(below.get(i))) {
                        hasMatch = false;
                        break;
                    } else {
                        hasMatch = true;
                    }
                }

                if (hasMatch) {
                    System.out.println(index + ": Has horizontal match at " + row);
                    sum += (row * 100);
                }
            }

            if (hasMatch) {
                index++;
                continue;
            }

            // Vertical match
            for (int column = 1; column < field.getFirst().size()  && !hasMatch; column++) {
                List<List<Character>> left = IntStream.range(0, column)
                        .mapToObj(curCol -> field.stream().map(l -> l.get(curCol)).toList()).toList();

                List<List<Character>> right = IntStream.range(column, field.getFirst().size())
                        .mapToObj(curCol -> field.stream().map(l -> l.get(curCol)).toList()).toList();


                if (Math.max(left.size(), right.size()) == 0) {
                    continue;
                }

                List<List<Character>> leftReversed = new ArrayList<>(left);
                Collections.reverse(leftReversed);

                hasMatch = true;
                for (int i = 0; i < Math.min(leftReversed.size(), right.size()); i++) {
                    if (!leftReversed.get(i).equals(right.get(i))) {
                        hasMatch = false;
                        break;
                    }
                }

                if (hasMatch) {
                    System.out.println(index + ": Has vertical match at " + column);
                    sum += column;
                }
            }

            if (!hasMatch) {
                System.out.println(index + ": Has no match");
            }

            index++;
        }

        System.out.println(sum);
        Instant inst2 = Instant.now();
        System.out.println("Elapsed Time: "+ Duration.between(inst1, inst2).toString());
    }
}