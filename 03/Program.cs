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
    const int bitCount = 12;
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

void SolvePartOne(List<ushort> inputs) {
    var bitCounts = CountBits(inputs);

    var gamma = 0;
    var epsilon = 0;
    var threshold = inputs.Count / 2;
    for (var i = 0; i < bitCounts.Length; i++) {
        if (bitCounts[i] > threshold) {
            gamma ^= 1<<i;
        } else {
            epsilon ^= 1<<i;
        }
    }

    Console.WriteLine("gamma*epsilon=" + gamma*epsilon);
}

void SolvePartTwo(List<ushort> inputs) {
    var candidates = inputs.ToArray();
    var bitIndex = 0;

    while (candidates.Length > 1) {
        var bitCount = 0;
        foreach (var candidate in candidates) {
            var bitIsSet = (candidate & 1<<bitIndex) != 0;
            if (bitIsSet) {
                bitCount += 1;
            }
        }

        var threshold = candidates.Length / 2;
        var filter = bitCount > threshold ? 1 : 0;

        candidates = candidates
            .Where(x => (x & 1<<bitIndex) == filter)
            .ToArray();

        bitIndex += 1;
    }

    var value = candidates[0];
}

var inputs = GetInput();
SolvePartOne(inputs);
SolvePartTwo(inputs);
