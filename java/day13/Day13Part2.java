import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day13Part2 {
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
            List<List<List<Character>>> cleanedFields = cleanedSmudges(field);
            int cleanedFieldIndex = 0;
            int[] originalResult = findReflection(field);
            printField(field);

            for (List<List<Character>> cleanedField : cleanedFields) {
                int[] cleanedResult = findReflection(cleanedField, originalResult);

               if (cleanedResult[0] > 0 && cleanedResult[0] != originalResult[0]) {
                    printField(field);
                    printField(cleanedField);
                    System.out.println(index + "/" + cleanedFieldIndex + ": Has horizontal match at " + cleanedResult[0]);
                    sum += (cleanedResult[0] * 100);
                   break;
                } else if (cleanedResult[1] > 0 && cleanedResult[1] != originalResult[1]) {
                    System.out.println(index + "/" + cleanedFieldIndex + ": Has vertical match at " + cleanedResult[1]);
                    sum += cleanedResult[1];
                   break;
                }

                cleanedFieldIndex++;
            }
            index++;
        }

        // 22691 TOO LOW
        System.out.println(sum);
        Instant inst2 = Instant.now();
        System.out.println("Elapsed Time: "+ Duration.between(inst1, inst2).toString());
    }

    private static int[] findReflection(List<List<Character>> field) {
        return findReflection(field, new int[]{0,0});
    }

    private static int[] findReflection(List<List<Character>> field, int[] forbidden) {
        for (int row = 1; row < field.size(); row++) {
            List<List<Character>> above = IntStream.range(0, row).mapToObj(field::get).toList();
            List<List<Character>> below = IntStream.range(row, field.size()).mapToObj(field::get).toList();

            if (Math.max(above.size(), below.size()) == 0) {
                continue;
            }

            List<List<Character>> aboveReversed = new ArrayList<>(above);
            Collections.reverse(aboveReversed);

            boolean hasMatch = false;
            for (int i = 0; i < Math.min(aboveReversed.size(), below.size()); i++) {
                if (!aboveReversed.get(i).equals(below.get(i))) {
                    hasMatch = false;
                    break;
                } else {
                    hasMatch = true;
                }
            }

            boolean isAllowedMatch = Math.max(forbidden[0], forbidden[1]) == 0 || forbidden[0] != row;
            if (hasMatch &&  isAllowedMatch) {
                return new int[]{row, 0};
            }
        }

        // Vertical match
        for (int column = 1; column < field.getFirst().size(); column++) {
            List<List<Character>> left = IntStream.range(0, column)
                    .mapToObj(curCol -> field.stream().map(l -> l.get(curCol)).toList()).toList();

            List<List<Character>> right = IntStream.range(column, field.getFirst().size())
                    .mapToObj(curCol -> field.stream().map(l -> l.get(curCol)).toList()).toList();


            if (Math.max(left.size(), right.size()) == 0) {
                continue;
            }

            List<List<Character>> leftReversed = new ArrayList<>(left);
            Collections.reverse(leftReversed);


            boolean hasMatch = false;
            for (int i = 0; i < Math.min(leftReversed.size(), right.size()); i++) {
                if (!leftReversed.get(i).equals(right.get(i))) {
                    hasMatch = false;
                    break;
                } else {
                    hasMatch = true;
                }
            }

            boolean isAllowedMatch = Math.max(forbidden[0], forbidden[1]) == 0 || forbidden[1] != column;
            if (hasMatch && isAllowedMatch) {
               return new int[]{0, column};
            }
        }

        return new int[]{0,0};
    }

    private static void printField(List<List<Character>> field) {
        for (List<Character> rows : field) {
            for (Character col : rows) {
                System.out.print(col);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<List<List<Character>>> cleanedSmudges(List<List<Character>> field) {
        List<List<List<Character>>> clean = new ArrayList<>();
        for (int row = 0; row < field.size(); row++) {
            for (int col = 0; col < field.get(row).size(); col++) {
                List<List<Character>> cleanedMirror = clone(field);
                cleanedMirror.get(row).set(col, field.get(row).get(col) == '.' ? '#' : '.');
                clean.add(cleanedMirror);
            }
        }

        return clean;
    }

    public static List<List<Character>> clone(List<List<Character>> list) {
        List<List<Character>> clonedList = new ArrayList<>();

        for (List<Character> inner : list) {
            List<Character> innerClone = new ArrayList<>(inner);
            clonedList.add(innerClone);
        }

        return clonedList;
    }
}