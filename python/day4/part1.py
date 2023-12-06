import re


class ScratchCard:
    winning_numbers: [str] = []
    own_numbers: [str] = []

    def __init__(self, winning_numbers, own_numbers):
        self.winning_numbers = winning_numbers
        self.own_numbers = own_numbers

    def __str__(self):
        return ''.join(self.winning_numbers) + ' | ' + ''.join(self.own_numbers)

    def score(self):
        correct = len(list(filter(lambda c: c in self.winning_numbers, self.own_numbers)))
        return pow(2, correct - 1) if correct > 0 else 0


if __name__ == "__main__":
    f = open("input.txt", "r")

    total = 0
    for x in f:
        base = re.sub(' +', ' ', x).split(':')
        numbers = base[1].strip().split('|')
        sc = ScratchCard(numbers[0].strip().split(' '), numbers[1].strip().split(' '))

        total += sc.score()

    print(total)

    f.close()
