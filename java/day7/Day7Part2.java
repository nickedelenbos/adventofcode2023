import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day7Part2 {
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
            Hand hand = orderedHands.get(i);
            score += (i + 1) * hand.getBid(); // +1 because rank starts at 1 not 0
        }

        System.out.println(score);
    }

    static class Hand implements Comparable<Hand> {
        private final List<Character> cards;
        private final int bid;

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
                    ", virtualCards=" + getVirtualCards() +
                    ", bid=" + bid +
                    ", type=" + getType() +
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

        private List<Character> getVirtualCards() {
            List<Character> virtualHand = new ArrayList<>(this.cards);
            Map<Character, Long> grouped = virtualHand.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));

            OptionalLong maxValue = grouped.entrySet().stream().filter(c -> c.getKey() != 'J').map(Map.Entry::getValue).mapToLong(Long::valueOf).max();
            Optional<Character> substitute = grouped.entrySet().stream()
                    .filter(c -> c.getKey() != 'J')
                    .filter(entry -> entry.getValue().equals(maxValue.orElse(0L)))
                    .map(Map.Entry::getKey)
                    .sorted((Comparator.comparing(Hand::cardToValue)))
                    .reduce((first, second) -> second)
                    .stream().findFirst();

            if (substitute.isEmpty()) {
                // throw new RuntimeException("No cards in hand, all Jokers??? " + this.cards);
                substitute = Optional.of('A');
            }

            for (int i = 0; i < virtualHand.size(); i++) {
                Character card = virtualHand.get(i);
                if (card != 'J') {
                    continue;
                }

                virtualHand.set(i, substitute.get());
            }

            return virtualHand;
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
            List<Character> virtualHand = new ArrayList<>(this.cards);
            Map<Character, Long> grouped = virtualHand.stream()
                    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

            // Joker involved, make joker the most occuring card, if not make it the highest card
            if (virtualHand.stream().anyMatch(c -> c == 'J')) {
                OptionalLong maxValue = grouped.entrySet().stream()
                        .filter(c -> c.getKey() != 'J')
                        .map(Map.Entry::getValue)
                        .mapToLong(Long::valueOf).max();

                Optional<Character> substitute = grouped.entrySet().stream()
                        .filter(c -> c.getKey() != 'J')
                        .filter(entry -> entry.getValue().equals(maxValue.orElse(0L)))
                        .map(Map.Entry::getKey)
                        .sorted((Comparator.comparing(Hand::cardToValue)))
                        .reduce((first, second) -> second)
                        .stream().findFirst();

                if (substitute.isEmpty()) {
                    // throw new RuntimeException("No cards in hand, all Jokers??? " + this.cards);
                    substitute = Optional.of('J');
                }

                for (int i = 0; i < virtualHand.size(); i++) {
                    Character card = virtualHand.get(i);
                    if (card != 'J') {
                        continue;
                    }

                    virtualHand.set(i, substitute.get());
                }
            }

            // We got new cards, recalculate the groups
            grouped = virtualHand.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
            long distinct = virtualHand.stream().distinct().count();

            // Five of a kind
            if (distinct == 1) {
                return 7;
            }


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
            return switch (card) {
                case 'A' -> 13;
                case 'K' -> 12;
                case 'Q' -> 11;
                case 'T' -> 10;
                case '9' -> 9;
                case '8' -> 8;
                case '7' -> 7;
                case '6' -> 6;
                case '5' -> 5;
                case '4' -> 4;
                case '3' -> 3;
                case '2' -> 2;
                case 'J' -> 1;
                default -> throw new RuntimeException("Unknown card " + card);
            };

        }
    }
}