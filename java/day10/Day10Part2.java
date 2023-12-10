import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.*;

public class Day10Part2 {

    public static final int DRAW_FACTOR = 20;

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
        Neighbours n = Neighbours.getNeighbours(maze, start.get());
        List<Position> neighbours = Arrays.asList(n.north, n.east, n.south, n.west);
        Path2D mainLoopPathPaint = new Path2D.Double();
        Path2D mainLoopPath = new Path2D.Double();
        boolean first = true;

        for (Position route : neighbours) {
            dir++;
            if (route != null && route.getOriginalChar() != '.') {
                // Check if this actually validly connects back to start
                boolean validStartPoint = (dir == 1 && route.pointsSouth()) || (dir == 2 && route.pointsWest()) || (dir == 3 && route.pointsNorth()) || (dir == 4 && route.pointsEast());
                if (!validStartPoint) {
                    continue;
                }

                Position previous = start.get();
                if (first) {
                    mainLoopPathPaint.moveTo(10 + previous.col * DRAW_FACTOR, 10 + previous.row * DRAW_FACTOR);
                    mainLoopPath.moveTo(previous.col, previous.row);
                }
                Position newCurrent = route;

                int score = 1;
                while (newCurrent != null && (newCurrent.getScore() == 0 || score < newCurrent.getScore()) && !newCurrent.isStart()) {
                    if (first) {
                        mainLoopPathPaint.lineTo(10 + newCurrent.col * DRAW_FACTOR, 10 + newCurrent.row * DRAW_FACTOR);
                        mainLoopPath.lineTo(newCurrent.col, newCurrent.row);
                    }
                    newCurrent.setScore(score++);

                    Position destination = Neighbours.getNeighbours(maze, newCurrent).getNext(previous, newCurrent);
                    previous = newCurrent;
                    newCurrent = destination;
                }

                if (first) {
                    mainLoopPathPaint.closePath();
                    mainLoopPath.closePath();
                }
                first = false;
            }

        }

        int trapped = 0;
        for (List<Position> rows : maze) {
            for (Position col : rows) {
                if (mainLoopPath.contains(col.col, col.row) && (col.isDot() || col.isNonMainLoopCharacter())) {
                    trapped++;
                }
            }
        }

        drawMaze(mainLoopPathPaint, mainLoopPath, maze);

        System.out.println("Trapped characters:" + trapped);

        Instant inst2 = Instant.now();
        System.out.println("Elapsed Time: " + Duration.between(inst1, inst2).toString());
    }

    private static void drawMaze(Path2D mainLoopPathPaint, Path2D mainLoopPath, List<List<Position>> maze) {
        JFrame frame = new JFrame("Path Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLUE);
                g2d.draw(mainLoopPathPaint);

                Font boldFont = new Font("Arial", Font.BOLD, 12);
                g2d.setFont(boldFont);

                for (List<Position> rows : maze) {
                    for (Position col : rows) {
                        if (!mainLoopPath.contains(col.col, col.row) && col.getScore() == 0 && !col.isStart()) {
                            g2d.setColor(Color.RED);
                        } else if (col.getScore() > 0 || col.isStart()) {
                            g2d.setColor(Color.BLACK);
                        } else {
                            g2d.setColor(Color.ORANGE);
                        }
                        g2d.drawString(String.valueOf(col.getOriginalChar()), col.col * DRAW_FACTOR + 10, col.row * DRAW_FACTOR + 15);
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 400); // Adjust size as needed
            }
        };


        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public record Neighbours(Position north, Position east, Position south, Position west) {

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

        public boolean isNonMainLoopCharacter() {
            return !isStart() && !isDot() && getScore() == 0;
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

        public boolean isDot() {
            return originalChar == '.';
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
            return "Position{" + "row=" + row + ", col=" + col + ", score=" + score + ", originalChar=" + originalChar + '}';
        }
    }
}