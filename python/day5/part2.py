import re
import locale
import time

locale.setlocale(locale.LC_ALL, '')  # Use '' for auto, or force e.g. to 'en_US.UTF-8'


def determine_location(seed_input, mapping_dict):
    start_value = int(seed_input)

    for cat, range_list in mapping_dict.items():
        for mapping in range_list:
            mapping_source, mapping_destination, mapping_length = mapping
            mapping_source_end = mapping_source + mapping_length - 1
            if mapping_source <= start_value <= mapping_source_end:
                start_value += (mapping_destination - mapping_source)
                break

    return start_value


if __name__ == "__main__":
    f = open("input.txt", "r")

    seeds: [int] = []
    mappings: dict[str, list[list[int]]] = {}

    category = None
    for x in f:
        sanitized = re.sub(' +', ' ', x.replace('\n', '')).strip()

        if sanitized.startswith('seeds:'):
            seeds.extend(sanitized.replace('seeds: ', '').split(' '))
            continue

        if sanitized.strip() == '':
            continue

        if not sanitized[0].isnumeric():
            category = sanitized.replace(' map:', '')
            mappings[category] = []
        else:
            destination = int(sanitized.split(' ')[0])
            source = int(sanitized.split(' ')[1])
            length = int(sanitized.split(' ')[2])

            mappings[category].append([source, destination, length])

    print_debug_output = False

    total_amount_of_seeds = 0
    i = 0
    while i < len(seeds):
        total_amount_of_seeds += len(range(int(seeds[i]), int(seeds[i]) + int(seeds[i + 1]), 1))
        i += 2

    start = time.time()
    i = 0
    cnt = 0
    lowest = None
    last_cat = None
    last_print_at = None

    while i < len(seeds):
        seed_range = range(int(seeds[i]), int(seeds[i]) + int(seeds[i + 1]), 1)
        for seed in seed_range:
            cnt += 1
            if cnt % 1300000 == 0:
                print('Progress {:0.3f} ({:n}/{:n}, took {:0.0f}s)'.format(
                    (cnt / total_amount_of_seeds) * 100, cnt, total_amount_of_seeds, time.time() - start))
            source_value = determine_location(int(seed), mappings)

            if lowest is None:
                lowest = source_value
            elif lowest > source_value:
                lowest = source_value

        i += 2

    print('\n Lowest ' + ' = ' + str(lowest))
