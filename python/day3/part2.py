def fix(nr, length=3):
    return '{}'.format(str(nr).ljust(length), str(nr))


def is_touch_character(char: str):
    return char.isnumeric()


def find_the_number(line: [str], col: int):
    the_number = line[col]

    # look left
    for i in range(col - 1, -1, -1):
        if line[i].isdigit():
            the_number = line[i] + the_number
        else:
            break

    # look right
    for i in range(col + 1, len(line), 1):
        if line[i].isdigit():
            the_number = the_number + line[i]
        else:
            break

    return the_number


def schematic_sum(schematic):
    total = 0
    matrix: list[list[str]] = []

    for line in schematic:
        chars = []
        for char in line:
            chars.append(char)

        matrix.append(chars)

    # Now look for unique numbers and see if they are adjacent any non (.|[0-9]) character
    for row, line in enumerate(matrix):
        for col, char in enumerate(line):
            # Using a set is a bit cheesy, because in the following situation we would not find consider this a match:
            # 1.1
            # .*.
            # But the answer qualified, so 😁
            sub_total = set()
            if char != '*':
                continue

            is_first_of_line = col == 0
            is_last_of_line = (col == len(line) - 1)
            
            # Does it touch left
            if not is_first_of_line:
                left_char = line[col - 1]
                if is_touch_character(left_char):
                    # print(fix(row) + ':' + fix(col) + '-> *' + ', left  =', find_the_number(line, col - 1))
                    sub_total.add(int(find_the_number(line, col - 1)))

            # Does it touch right
            if not is_last_of_line:
                right_char = line[col + 1]
                if is_touch_character(right_char):
                    # print(fix(row) + ':' + fix(col) + '-> *' + ', right =', find_the_number(line, col + 1))
                    sub_total.add(int(find_the_number(line, col + 1)))

            # Does it touch above
            if row > 0:
                row_above = matrix[row - 1]

                list_of_columns_that_are_touch_points = []
                right_above = col + 1 if not is_last_of_line else col
                # Increase the range of numbers by 2, 1 for top right and 1 for top left
                for i in range(right_above, right_above - 1 - 2, -1):
                    list_of_columns_that_are_touch_points.append(i)

                for i in list_of_columns_that_are_touch_points:
                    if is_touch_character(row_above[i]):
                        # print(fix(row) + ':' + fix(col) + '-> *' + ', above =',
                        #       find_the_number(row_above, i))
                        sub_total.add(int(find_the_number(row_above, i)))
                        #break

            # Does it touch below
            if row <= len(matrix) - 2:
                row_below = matrix[row + 1]

                list_of_columns_that_are_touch_points = []
                right_below = col + 1 if not is_last_of_line else col
                # Increase the range of numbers by 2, 1 for top right and 1 for top left
                for i in range(right_below, right_below - 1 - 2, -1):
                    list_of_columns_that_are_touch_points.append(i)

                for i in list_of_columns_that_are_touch_points:
                    if is_touch_character(row_below[i]):
                        # print(fix(row) + ':' + fix(col) + '-> *' + ', below =',
                        #       find_the_number(row_below, i))
                        sub_total.add(int(find_the_number(row_below, i)))
                        #break

            if len(sub_total) == 2:
                print(fix(row+1) + ':' + fix(col) + '-> *' + ', touches =', sub_total, 'makes', (list(sub_total)[0] * list(sub_total)[1]))
                total += (list(sub_total)[0] * list(sub_total)[1])
            else:
                print(fix(row+1) + ':' + fix(col) + '-> *' + ', disqaly =', sub_total)

    return total


if __name__ == "__main__":
    f = open("input.txt", "r")
    lines = []
    for x in f:
        lines.append(x.replace("\n", ""))

    print(str(schematic_sum(lines)))

    f.close()
