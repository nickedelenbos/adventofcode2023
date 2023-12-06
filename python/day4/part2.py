import re
from copy import deepcopy


class ScratchCard:
    winning_numbers: [str] = []
    own_numbers: [str] = []

    def __init__(self, winning_numbers, own_numbers):
        self.winning_numbers = winning_numbers
        self.own_numbers = own_numbers

    def __str__(self):
        return ''.join(self.winning_numbers) + ' | ' + ''.join(self.own_numbers)

    def score(self):
        return len(list(filter(lambda c: c in self.winning_numbers, self.own_numbers)))


if __name__ == "__main__":
    f = open("input.txt", "r")

    cards: dict = {}
    for x in f:
        base = re.sub(' +', ' ', x).split(':')
        numbers = base[1].strip().split('|')
        sc = ScratchCard(numbers[0].strip().split(' '), numbers[1].strip().split(' '))

        card_number = int(base[0].replace('Card ', ''))

        if card_number not in cards:
            cards[card_number] = []

        cards[card_number].append(sc)

    f.close()

    total_amount_of_cards = 0

    current_card_number = 1
    while current_card_number < len(cards.items()) + 1:
        print('Processing card number ' + str(current_card_number))
        numbered_cards = cards.get(current_card_number)
        c = 0
        while c < len(numbered_cards):
            card = numbered_cards[c]
            for card_number_won in range(current_card_number + 1, current_card_number + 1 + card.score()):
                cards[card_number_won].append(deepcopy(cards[card_number_won][0]))
            c += 1
            total_amount_of_cards += 1

        current_card_number += 1

    print(total_amount_of_cards)
