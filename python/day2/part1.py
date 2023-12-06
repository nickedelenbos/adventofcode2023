import re


class CubeSet:
    blue = -1
    red = -1
    green = -1

    def __init__(self, red, green, blue):
        self.red = red
        self.green = green
        self.blue = blue

    def __str__(self):
        return '<r:' + str(self.red) + ', g:' + str(self.green) + ', b:' + str(self.blue) + '>'

    def __repr__(self):
        return self.__str__()

    @staticmethod
    def create(cubes):
        r, g, b = 0, 0, 0
        for item in cubes.split(','):
            if item.endswith('red'):
                r = int(re.sub("[^0-9]", "", item))
            elif item.endswith('green'):
                g = int(re.sub("[^0-9]", "", item))
            elif item.endswith('blue'):
                b = int(re.sub("[^0-9]", "", item))
            else:
                raise Exception('Invalid color in set \'' + item + '\'', cubes)

        return CubeSet(r, g, b)


class Game:
    number = 0
    cube_sets: list[CubeSet] = []

    # parameterized constructor
    def __init__(self, number, cube_sets):
        self.number = number
        self.cube_sets = cube_sets

    def __str__(self):
        return str(self.number) + ', ' + ",".join([str(element) for element in self.cube_sets])

    def max_red(self):
        maximum = - 1
        for c in self.cube_sets:
            if c.red > maximum:
                maximum = c.red

        return maximum

    def max_green(self):
        maximum = - 1
        for c in self.cube_sets:
            if c.green > maximum:
                maximum = c.green

        return maximum

    def max_blue(self):
        maximum = - 1
        for c in self.cube_sets:
            if c.blue > maximum:
                maximum = c.blue

        return maximum

    def is_possible(self, red, green, blue):
        return (self.max_red() == -1 or self.max_red() <= red) and \
            (self.max_green() == -1 or self.max_green() <= green) and \
            (self.max_blue() == -1 or self.max_blue() <= blue)


f = open("input.txt", "r")

games = []
for x in f:
    sanitized = x.replace("\n", '')
    split = sanitized.split(':')

    sets = []
    for cube_set in split[1].split(';'):
        sets.append(CubeSet.create(cube_set))

    games.append(Game(int(split[0].replace('Game ', '')), sets))

f.close()

total = 0
for game in games:
    if game.is_possible(12, 13, 14):
        total = total + game.number

print(total)
