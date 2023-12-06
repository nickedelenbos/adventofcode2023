import re
import locale
locale.setlocale(locale.LC_ALL, '')  # Use '' for auto, or force e.g. to 'en_US.UTF-8'


class Record:
    time = 0
    distance = 0


if __name__ == "__main__":
    f = open("input.txt", "r")

    record = Record()
    for x in f:
        i = 0
        sanitized = re.sub(' +', ' ', x.replace('\n', ''))
        if sanitized.startswith('Time:'):
            record.time = int(sanitized.split(':')[1].strip().replace(' ', ''))
        elif sanitized.startswith('Distance:'):
            record.distance = int(sanitized.split(':')[1].strip().replace(' ', ''))

    f.close()

    print(str(record.time) + '/' + str(record.distance))
    winning_combinations = 0
    for speed in range(1, record.time, 1):
        distance_reached = speed * (record.time - speed)
        if distance_reached > record.distance:
            winning_combinations += 1
            print('Reach ' + str(distance_reached) + ', speed ' + f'{speed:n}' + ' in game time ' + f'{record.time:n}')

    print(winning_combinations)
