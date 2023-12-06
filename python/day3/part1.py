def fix(nr, length=3):
    return '{}'.format(str(nr).ljust(length), str(nr))


def is_touch_character(char: str):
    return char != '.' and not char.isnumeric()


def schematic_sum(schematic):
    total = 0
    matrix: list[list[str]] = []

    for line in schematic:
        chars = []
        for char in line:
            chars.append(char)

        matrix.append(chars)

    # Now look for unique numbers and see if they are adjacent any non (.|[0-9]) character
    number = None
    for row, line in enumerate(matrix):
        for col, char in enumerate(line):
            number_is_first_of_line = number is not None and col == len(number) - 1
            number_is_last_of_line = (col == len(line) - 1) and char.isnumeric()

            if number is None and not char.isnumeric():
                continue
            elif not char.isnumeric() or number_is_last_of_line:  # Found a number, lets do 🧙
                if number_is_last_of_line:  # End of line, so add this one still
                    number = number + char if number is not None else char

                touched = False

                # Does it touch left
                if not number_is_first_of_line:
                    left_char = line[col - len(number)] if number_is_last_of_line else line[col - len(number) - 1]
                    if is_touch_character(left_char):
                        print('\033[92m' + fix(row) + ':' + fix(col) + '->' + fix(number, 4) + ', left  =', left_char)
                        total += int(number)
                        touched = True

                # Does it touch right
                if not touched and not number_is_last_of_line:
                    if is_touch_character(line[col]):
                        print('\033[92m' + fix(row) + ':' + fix(col) + '->' + fix(number, 4) + ', right =', line[col])
                        total += int(number)
                        touched = True

                # Does it touch above
                if not touched and row > 0:
                    row_above = matrix[row - 1]

                    list_of_columns_that_are_touch_points = []
                    right_above = col if not number_is_last_of_line else col - 1
                    # Increase the range of numbers by 2, 1 for top right and 1 for top left
                    for i in range(right_above, right_above - len(number) - 2, -1):
                        list_of_columns_that_are_touch_points.append(i)

                    for i in list_of_columns_that_are_touch_points:
                        if is_touch_character(row_above[i]):
                            print('\033[92m' + fix(row) + ':' + fix(col) + '->' + fix(number, 4) + ', above =', row_above[i])
                            total += int(number)
                            touched = True
                            break

                # Does it touch below
                if not touched and row < len(matrix) - 2:
                    row_below = matrix[row + 1]

                    list_of_columns_that_are_touch_points = []
                    right_below = col if not number_is_last_of_line else col - 1
                    # Increase the range of numbers by 2, 1 for top right and 1 for top left
                    for i in range(right_below, right_below - len(number) - 2, -1):
                        list_of_columns_that_are_touch_points.append(i)

                    for i in list_of_columns_that_are_touch_points:
                        if is_touch_character(row_below[i]):
                            print('\033[92m' + fix(row) + ':' + fix(col) + '->' + fix(number, 4) + ', below =', row_below[i])
                            total += int(number)
                            touched = True
                            break

                # Number ended, so reset
                if not touched:
                    print('\033[91m' + fix(row) + ':' + fix(col) + '->' + fix(number, 4) + ', none  = ')
                number = None
            elif char.isnumeric():
                number = number + char if number is not None else char

        # Numbers don't span over newlines
        number = None
    return total


if __name__ == "__main__":
    f = open("input.txt", "r")
    lines = []
    for x in f:
        lines.append(x.replace("\n", ""))

    print('\033[92m' + str(schematic_sum(lines)))

    f.close()
