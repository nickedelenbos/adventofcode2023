import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Day10Part1 {
    public static void main(String[] args) throws IOException {
        Instant inst1 = Instant.now();

        List<List<Position>> maze = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("day10/input.txt"))) {
            String line;
            int rowNr = 0;
            int colNr;
            while ((line = br.readLine()) != null) {
                colNr = 0;
                List<Position> list = new ArrayList<>();
                for (char ch : line.toCharArray()) {
                    list.add(new Position(ch, rowNr, colNr));
                    colNr++;
                }

                rowNr++;
                maze.add(list);
            }
        }

        Optional<Position> start = maze.stream().flatMap(List::stream).filter(Position::isStart).findFirst();

        if (start.isEmpty()) {
            throw new RuntimeException("No start found");
        }

        int dir = 0;
        for (Position route : Neighbours.getNeighbours(maze, start.get())) {
            dir++;
            if (route != null && route.getOriginalChar() != '.') {
                // Check if this actually validly connects back to start
                boolean validStartPoint = (dir == 1 && route.pointsSouth()) || (dir == 2 && route.pointsWest()) || (dir == 3 && route.pointsNorth()) || (dir == 4 && route.pointsEast());
                if (!validStartPoint) {
                    continue;
                }
                Position previous = start.get();
                Position newCurrent = route;

                int score = 1;
                while (newCurrent != null && (newCurrent.getScore() == 0 || score < newCurrent.getScore()) && !newCurrent.isStart()) {
                    newCurrent.setScore(score++);

                    Position destination = Neighbours.getNeighbours(maze, newCurrent).getNext(previous, newCurrent);
                    previous = newCurrent;
                    newCurrent = destination;
                }
            }
        }

        System.out.println(maze.stream().flatMap(List::stream).map(Position::getScore).max(Integer::compareTo).orElse(-1));
        Instant inst2 = Instant.now();
        System.out.println("Elapsed Time: " + Duration.between(inst1, inst2).toString());
    }

    public record Neighbours(Position north, Position east, Position south,
                             Position west) implements Iterable<Position> {

        static Neighbours getNeighbours(List<List<Position>> maze, Position pos) {
            Position north = pos.getRow() > 0 ? maze.get(pos.getRow() - 1).get(pos.getCol()) : null;
            Position east = pos.getCol() < maze.get(pos.getRow()).size() - 1 ? maze.get(pos.getRow()).get(pos.getCol() + 1) : null;
            Position south = pos.getRow() < maze.size() - 1 ? maze.get(pos.getRow() + 1).get(pos.getCol()) : null;
            Position west = pos.getCol() > 0 ? maze.get(pos.getRow()).get(pos.getCol() - 1) : null;

            return new Neighbours(north, east, south, west);
        }

        public Position getNext(Position prev, Position cur) {
            if (north != null && north.isValidPosition() && !prev.equals(north) && cur.pointsNorth()) {
                return north;
            }

            if (east != null && east.isValidPosition() && !prev.equals(east) && cur.pointsEast()) {
                return east;
            }

            if (south != null && south.isValidPosition() && !prev.equals(south) && cur.pointsSouth()) {
                return south;
            }

            if (west != null && west.isValidPosition() && !prev.equals(west) && cur.pointsWest()) {
                return west;
            }

            return null;
        }

        @Override
        public String toString() {
            return "Neighbours{n=" + north + ", e=" + east + ", s=" + south + ", w=" + west + '}';
        }

        @Override
        public Iterator<Position> iterator() {
            return new ArrayList<>(Arrays.asList(north, east, south, west)).iterator();
        }
    }

    public static class Position {
        private final int row, col;
        private int score;
        private final char originalChar;

        public Position(char originalChar, int row, int col) {
            this.originalChar = originalChar;
            this.row = row;
            this.col = col;

            this.score = 0;
        }

        /*
         * | is a vertical pipe connecting north and south.
         * - is a horizontal pipe connecting east and west.
         * L is a 90-degree bend connecting north and east.
         * J is a 90-degree bend connecting north and west.
         * 7 is a 90-degree bend connecting south and west.
         * F is a 90-degree bend connecting south and east.
         */

        public boolean pointsNorth() {
            return this.originalChar == '|' || this.originalChar == 'L' || this.originalChar == 'J';
        }

        public boolean pointsEast() {
            return this.originalChar == '-' || this.originalChar == 'L' || this.originalChar == 'F';
        }

        public boolean pointsSouth() {
            return this.originalChar == '|' || this.originalChar == '7' || this.originalChar == 'F';
        }

        public boolean pointsWest() {
            return this.originalChar == '-' || this.originalChar == 'J' || this.originalChar == '7';
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public boolean isValidPosition() {
            return originalChar != '.';
        }

        public boolean isStart() {
            return originalChar == 'S';
        }

        public char getOriginalChar() {
            return originalChar;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "row=" + row +
                    ", col=" + col +
                    ", score=" + score +
                    ", originalChar=" + originalChar +
                    '}';
        }
    }
}