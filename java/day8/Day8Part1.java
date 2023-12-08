import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day8Part1 {
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

        int step = 0;
        String position = "AAA";
        Instruction currentInstruction = instructions.get("AAA");

        while (!position.equals("ZZZ")) {
            position = currentInstruction.getDirection(directions[step % directions.length]);
            currentInstruction = instructions.get(position);
            step++;
        }

        System.out.println(step);
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