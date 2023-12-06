def string_to_numbers(string):
    response = ''
    for idx, char in enumerate(string):
        if char.isnumeric():
            response = response + char
        elif string[0:idx+1].endswith(tuple(['one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine'])):
            for k, v in {'one': '1', 'two': '2', 'three': '3', 'four': '4', 'five': '5', 'six': '6', 'seven': '7',
                         'eight': '8', 'nine': '9'}.items():
                if string[0:idx + 1].endswith(k):
                    response = response + v

    return response


def numbers_to_answer(string):
    if len(string) == 0:
        raise Exception(x + ' does not contain numbers')
    elif len(string) == 1:
        return int(string + string)
    else:
        return int(string[0] + string[len(string) - 1])


if __name__ == "__main__":
    f = open("day1.txt", "r")
    total = 0
    line_number = 1
    for x in f:
        sanitized = x.replace("\n", '')
        numbers = string_to_numbers(sanitized)
        val = numbers_to_answer(numbers)

        if line_number == 992 or True:
            print('{}'.format(str(line_number).ljust(5), str(line_number)) + ': ' + sanitized + ' makes ' + numbers + ' makes '
                  + str(val) + ' total goes ' + str(total) + '/' + str(total+val))

        total += val
        line_number += 1

    f.close()
    print(total)

