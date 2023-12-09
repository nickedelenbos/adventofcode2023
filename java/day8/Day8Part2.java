import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Day8Part2 {
    public static void main(String[] args) throws IOException {
        Map<String, Instruction> instructions = new HashMap<>();
        char[] directions = new char[0];
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("day8/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (i++ == 0) {
                    directions = line.toCharArray();
                    continue;
                }

                // AAA = (BBB, CCC)
                String[] split = line.split("=");
                Instruction instruction = new Instruction();
                String[] leftRight = split[1].split(",");
                instruction.left = leftRight[0].replaceAll("\\(", "").trim();
                instruction.right = leftRight[1].replaceAll("\\)", "").trim();

                instructions.put(split[0].trim(), instruction);
            }
        }

        Map<String, Instruction> startNodes = instructions.entrySet().stream()
                .filter(k -> k.getKey().endsWith("A"))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (x, y) -> y, LinkedHashMap::new));

        Map<Integer, Long> numbers = new HashMap<>();
        int nodeNr = 0;
        for (String startNode : startNodes.keySet()) {
            String cur = startNode;
            long step = 0;
            while (!cur.endsWith("Z")) {
                cur = instructions.get(cur).getDirection(directions[(int) (step % directions.length)]);
                step++;
            }

            numbers.put(nodeNr, step);
            System.out.println((nodeNr++) + " in " + step);
        }

        System.out.println(lcm(numbers.values().stream().toList()));
    }

    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    private static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    private static long lcm(List<Long> arr) {
        long result = arr.getFirst();
        for (int i = 1; i < arr.size(); i++) {
            result = lcm(result, arr.get(i));
        }
        return result;
    }

    static class Instruction {
        public String left;
        public String right;

        public String getDirection(char direction) {
            if (direction == 'L') {
                return left;
            } else {
                return right;
            }
        }
    }

}