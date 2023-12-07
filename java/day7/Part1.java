import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Part1 {
    public static void main(String[] args) throws IOException {
        var hands = new TreeSet<Hand>();
        try (BufferedReader br = new BufferedReader(new FileReader("day7/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                hands.add(new Hand(split[0].chars().mapToObj(c -> (char) c).collect(Collectors.toList()), Integer.parseInt(split[1])));
            }
        }


        List<Hand> orderedHands = hands.stream().toList();
        var score = 0;
        for (int i = 0; i < hands.size(); i++) {
            var rank = i + 1;
            score += rank * orderedHands.get(i).getBid();
        }

        System.out.println(score);
    }

    static class Hand implements Comparable<Hand> {
        private final List<Character> cards;
        private int bid;

        public Hand(List<Character> cards, int bid) {
            this.cards = cards;
            this.bid = bid;
        }

        public int getBid() {
            return this.bid;
        }

        @Override
        public String toString() {
            return "Hand{" +
                    "cards=" + cards +
                    ", bid=" + bid +
                    '}';
        }

        @Override
        public int compareTo(Hand o) {
            int compare = this.getType().compareTo(o.getType());
            if (compare != 0) {
                return compare;
            }

            for (int i = 0; i < this.cards.size(); i++) {
                var myCard = this.cards.get(i);
                var otherCard = o.cards.get(i);

                if (myCard == otherCard) {
                    continue;
                }

                compare = cardToValue(myCard).compareTo(cardToValue(otherCard));
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }

        /**
         * 7 = Five of a kind, where all five cards have the same label: AAAAA
         * 6 = Four of a kind, where four cards have the same label and one card has a different label: AA8AA
         * 5 = Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
         * 4 = Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
         * 3 = Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
         * 2 = One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
         * 1 = High card, where all cards' labels are distinct: 23456
         *
         * @return the type of hand
         */
        private Integer getType() {
            long distinct = this.cards.stream().distinct().count();
            // Five of a kind
            if (distinct == 1) {
                return 7;
            }

            Map<Character, Long> grouped = this.cards.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
            // Four of a kind OR full house
            if (distinct == 2) {
                return grouped.containsValue(1L) ? 6 : 5;
            }

            // Three of a kind of two pair
            if (distinct == 3) {
                return grouped.containsValue(3L) ? 4 : 3;
            }

            // One pair
            if (distinct == 4) {
                return 2;
            }

            return 1;
        }

        private static Integer cardToValue(char card) {
            switch (card) {
                case 'A': return 13;
                case 'K': return 12;
                case 'Q': return 11;
                case 'J': return 10;
                case 'T': return 9;
                case '9': return 8;
                case '8': return 7;
                case '7': return 6;
                case '6': return 5;
                case '5': return 4;
                case '4': return 3;
                case '3': return 2;
                case '2': return 1;
            }

            throw new RuntimeException("Unknown card " + card);
        }
    }
}