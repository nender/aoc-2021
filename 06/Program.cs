IEnumerable<byte> GetInput() =>
    Console.ReadLine()
        .Split(',')
        .Select(x => byte.Parse(x));


void SolvePartOne(IEnumerable<byte> input) {
    var fish = input.ToList();

    foreach (var _ in Enumerable.Range(0, 80)) {
        var newFish = 0;
        for (var i = 0; i < fish.Count; i++) {
            var counter = fish[i];
            if (counter > 0) {
                fish[i] = --counter;
            } else {
                fish[i] = 6;
                newFish += 1;
            }
        }
        fish.AddRange(Enumerable.Repeat((byte)8, newFish));
    }

    Console.WriteLine($"After 80 days there are {fish.Count} fish");
}

void SolvePartTwo(IEnumerable<byte> input) {
    var fish = new long[9];
    foreach (var f in input) {
        fish[f] += 1;
    }

    var shiftedFish = new long[9];
    foreach (var _ in Enumerable.Range(0, 256)) {
        Array.Copy(fish, 1, shiftedFish, 0, fish.Length - 1);
        shiftedFish[6] += fish[0];
        shiftedFish[8] += fish[0];

        Array.Copy(shiftedFish, fish, fish.Length);
        Array.Clear(shiftedFish);
    }

    Console.WriteLine($"After 256 days there are {fish.Sum()} fish");
}

var input = GetInput();
SolvePartOne(input);
SolvePartTwo(input);