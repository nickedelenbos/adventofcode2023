import functools
import operator
import re


class Record:
    time = 0
    distance = 0


if __name__ == "__main__":
    f = open("input.txt", "r")

    records = {}
    for x in f:
        i = 0
        sanitized = re.sub(' +', ' ', x.replace('\n', ''))
        if sanitized.startswith('Time:'):
            for time in sanitized.split(':')[1].strip().split(' '):
                records[i] = Record()
                records[i].time = int(time)
                i += 1
        elif sanitized.startswith('Distance:'):
            for distance in sanitized.split(':')[1].strip().split(' '):
                records[i].distance = int(distance)
                i += 1

    combinations = []
    for key, record in records.items():
        winning_combinations = 0
        for speed in range(1, record.time, 1):
            distance_reached = speed * (record.time - speed)
            if distance_reached > record.distance:
                winning_combinations += 1
                print('reached ' + str(distance_reached) + ' with speed ' + str(speed) + ' in game time ' + str(record.time))

        combinations.append(winning_combinations)

    f.close()
    print(functools.reduce(operator.mul, combinations))