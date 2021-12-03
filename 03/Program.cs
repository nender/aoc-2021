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
    var idx = Globals.inputSize - 1;

    while (candidates.Length > 1) {
        var bitCount = 0;
        foreach (var candidate in candidates) {
            var bitIsSet = (candidate & 1<<(idx-bitIndex)) != 0;
            if (bitIsSet) {
                bitCount += 1;
            }
        }

        // todo: fix corner case where ties should go to 1 bit / handle odd case properly
        var threshold = candidates.Length / 2;
        var setIsMostCommon = bitCount > threshold;

        candidates = candidates
            .Where(x => { 
                var bitIsSet = (x & 1<<(idx-bitIndex)) != 0;
                return !(bitIsSet^setIsMostCommon);
            })
            .ToArray();

        bitIndex += 1;
    }

    var value = candidates[0];
}

var inputs = GetInput();
SolvePartOne(inputs);
SolvePartTwo(inputs);

static class Globals {
    public const int inputSize = 5;
}