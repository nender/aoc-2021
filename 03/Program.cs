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

const int bitCount = 12;
var inputs = GetInput();
var inputCount = inputs.Count;

var bitCounts = new int[bitCount];
foreach (var reading in inputs) {
    for (var i = 0; i < bitCount; i++) {
        var bitIsSet = (reading & 1<<i) != 0;
        if (bitIsSet) {
            bitCounts[i] += 1;
        }
    }
}

var gamma = 0;
var epsilon = 0;
var threshold = inputCount / 2;
for (var i = 0; i < bitCounts.Length; i++) {
    if (bitCounts[i] > threshold) {
        gamma ^= 1<<i;
    } else {
        epsilon ^= 1<<i;
    }
}

Console.WriteLine("gamma*epsilon=" + gamma*epsilon);
