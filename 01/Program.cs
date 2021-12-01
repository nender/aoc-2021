List<int> GetInput() {
    var inputs = new List<int>();
    string? input = Console.ReadLine();

    while (input != null) {
        var number = int.Parse(input);
        inputs.Add(number);
        input = Console.ReadLine();
    }

    return inputs;
}

void SolvePart1(List<int> input) {
    var counter = 0;
    for (var i = 1; i < input.Count; i++) {
        if (input[i] > input[i-1]) {
            counter += 1;
        }
    }
    Console.WriteLine("Problem 1 solution:" + counter);
}

void SolvePart2(List<int> input) {
    var counter = 0;
    for (var i = 2; i < input.Count-1; i++) {
        var sum1 = input[i]+input[i-1]+input[i-2];
        var sum2 = input[i+1]+input[i]+input[i-1];
        if (sum2 > sum1) {
            counter += 1;
        }
    }
    Console.WriteLine("Problem 2 solution:" + counter);
}

var input = GetInput();
SolvePart1(input);
SolvePart2(input);