import re

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

    lowest = None
    last_cat = None
    for seed in seeds:
        print('\nSeed ' + str(seed), end=', ')
        source_value = int(seed)

        found_range = False
        for cat, range_list in mappings.items():
            found = False
            last_cat = cat.split('-')[2]
            for mapping in range_list:
                mapping_source, mapping_destination, mapping_length = mapping
                mapping_source_end = mapping_source + mapping_length - 1
                if mapping_source <= source_value <= mapping_source_end:
                    source_value += (mapping_destination - mapping_source)
                    print(last_cat + ' ' + str(source_value), end=', ')
                    found = True
                    break

            if not found:
                print(last_cat + ' ' + str(source_value), end=', ')

        if lowest is None:
            lowest = source_value
        elif lowest > source_value:
            lowest = source_value

    print('\n Lowest ' + last_cat + ' = ' + str(lowest))

