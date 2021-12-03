List<ushort> GetInput() {
    var inputs = new List<ushort>();

    string? input = Console.ReadLine();
    const int fromBase = 2;
    while (input != null) {
        var number = Convert.ToUInt16(input, fromBase);
        inputs.Add(number);
        input = Console.ReadLine();
    }

    return inputs;
}

int[] CountBits(IEnumerable<ushort> inputs) {
    const int bitCount = Globals.inputSize;
    var bitCounts = new int[bitCount];
    foreach (var reading in inputs) {
        for (var i = 0; i < bitCount; i++) {
            var bitIsSet = (reading & 1<<i) != 0;
            if (bitIsSet) {
                bitCounts[i] += 1;
            }
        }
    }

    return bitCounts;
}

int GetThreshold(int count) {
    if (count % 2 == 0) {
        return count / 2;
    } else {
        return count / 2 + 1; 
    }
}

void SolvePartOne(List<ushort> inputs) {
    var bitCounts = CountBits(inputs);

    var gamma = 0;
    var epsilon = 0;
    var threshold = GetThreshold(inputs.Count);
    for (var i = 0; i < bitCounts.Length; i++) {
        if (bitCounts[i] > threshold) {
            gamma ^= 1<<i;
        } else {
            epsilon ^= 1<<i;
        }
    }

    Console.WriteLine("gamma*epsilon=" + gamma*epsilon);
}

int FilterValues(IEnumerable<ushort> inputs, Func<bool, bool, bool> filter) {
    var candidates = inputs.ToArray();
    var bitIndex = 0;
    var idx = Globals.inputSize - 1;

    while (candidates.Length > 1) {
        var bitCount = 0;
        foreach (var candidate in candidates) {
            var bitIsSet = (candidate & 1<<(idx-bitIndex)) != 0;
            if (bitIsSet) {
                bitCount += 1;
            }
        }

        var threshold = GetThreshold(candidates.Length);
        var setIsMostCommon = bitCount >= threshold;

        candidates = candidates
            .Where(x => { 
                var bitIsSet = (x & 1<<(idx-bitIndex)) != 0;
                return filter(bitIsSet, setIsMostCommon);
            })
            .ToArray();

        bitIndex += 1;
    }

    return candidates[0];
}

void SolvePartTwo(List<ushort> inputs) {
    var oxygenRating = FilterValues(
        inputs,
        filter: (x,y) => !(x^y)
    );

    var co2Rating = FilterValues(
        inputs,
        filter: (x,y) => x != y
    );

    Console.WriteLine("Rating checksum: " + oxygenRating * co2Rating);
}

var inputs = GetInput();
SolvePartOne(inputs);
SolvePartTwo(inputs);

static class Globals {
    public const int inputSize = 12;
}