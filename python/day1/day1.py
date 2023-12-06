import re

f = open("day1.txt", "r")
total = 0
for x in f:
    numbers = re.sub('\\D', '', x)
    if len(numbers) == 0:
        raise Exception(x + ' does not contain numbers')
    elif len(numbers) == 1:
        val = int(numbers + numbers)
    else:
        val = int(numbers[0] + numbers[len(numbers)-1])

    print(x + ' makes ' + numbers + ' makes ' + str(val))
    total += val

f.close()
print(total)
