import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class Part2 {
    public static void main(String[] args) throws IOException {
        var seeds = new ArrayList<Long>();
        var mappings = new LinkedHashMap<String, List<List<Long>>>();

        String category = null;
        try (BufferedReader br = new BufferedReader(new FileReader("day5/input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                var sanitized = line.replaceAll("\\s+", " ");

                if (sanitized.startsWith("seeds: ")) {
                    seeds.addAll(
                            Arrays.stream(
                                    sanitized.replaceAll("seeds: ", "").split(" ")
                            ).map(Long::parseLong).toList());
                }

                if (sanitized.trim().isEmpty()) {
                    continue;
                }

                if (!Character.isDigit(sanitized.charAt(0))) {
                    category = sanitized.replace(" map:", "");
                    mappings.putIfAbsent(category, new ArrayList<>());
                } else {
                    List<Long> parts = Arrays.stream(sanitized.split(" ")).map(Long::parseLong).toList();
                    long[] orderedParts = new long[]{parts.get(1), parts.get(0), parts.get(2)};
                    mappings.get(category).add(Arrays.stream(orderedParts).boxed().toList());
                }
            }

            var total_amount_of_seeds = 0l;
            var i = 0;

            while (i < seeds.size()) {
                total_amount_of_seeds += LongStream.range(seeds.get(i), seeds.get(i) + seeds.get(i + 1)).count();
                i += 2;
            }

            long start = System.nanoTime();
            i = 0;
            AtomicLong lowest = new AtomicLong(Long.MAX_VALUE);

            while (i < seeds.size()) {
                LongStream.range(seeds.get(i), seeds.get(i) + seeds.get(i + 1)).parallel().forEach(
                        seed -> {
                            long source_value = Part2.determineLocation(seed, mappings);
                            synchronized (lowest) {
                                long unsafeLowest = lowest.get();
                                if (unsafeLowest == Long.MAX_VALUE) {
                                    lowest.set(source_value);
                                } else if (unsafeLowest > source_value) {
                                    lowest.set(source_value);
                                }
                            }
                        }
                );

                i += 2;
            }

            long endTime = System.nanoTime();
            double timeTakenInMillis = (endTime - start) / 1_000_000_000.0;
            double roundedTime = Math.round(timeTakenInMillis * 1000.0) / 1000.0;
            System.out.println("Lowest = " + lowest + " took " + roundedTime + "s");

        }
    }

    public static Long determineLocation(final long seed, HashMap<String, List<List<Long>>> mappings) {
        AtomicLong startValue = new AtomicLong(seed);

        mappings.forEach((category, rangeList) -> {
            for (List<Long> mapping : rangeList) {
                var mappingSource = mapping.get(0);
                var mappingDestination = mapping.get(1);
                var mappingLength = mapping.get(2);

                var mappingSourceEnd = mappingSource + mappingLength - 1;
                long sv = startValue.get();
                if (sv >= mappingSource && sv <= mappingSourceEnd) {
                    startValue.set(sv + (mappingDestination - mappingSource));
                    break;
                }
            }
        });
        return startValue.get();
    }
}