import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class Part2 {
    public static void main(String[] args) throws IOException {
        var seeds = new ArrayList<Long>();
        // Linked is essential, since the almanac order is important
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

            long start = System.nanoTime();
            var i = 0;
            AtomicLong lowest = new AtomicLong(Long.MAX_VALUE);

            while (i < seeds.size()) {
                LongStream.range(seeds.get(i), seeds.get(i) + seeds.get(i + 1)).parallel().forEach(
                        seed -> {
                            final long location = Part2.determineLocation(seed, mappings);
                            lowest.getAndUpdate((val) -> val == Long.MAX_VALUE || val > location ? location : val);
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
        long startValue = seed;

        for (Map.Entry<String, List<List<Long>>> entry : mappings.entrySet()) {
            for (List<Long> mapping : entry.getValue()) {
                /* 0 = source, 1 = destination, 2 = length */
                var mappingSource = mapping.get(0);
                var mappingDestination = mapping.get(1);
                var mappingSourceEnd = mappingSource + mapping.get(2) - 1;

                if (startValue >= mappingSource && startValue <= mappingSourceEnd) {
                    startValue += (mappingDestination - mappingSource);
                    break;
                }
            }
        }

        return startValue;
    }
}