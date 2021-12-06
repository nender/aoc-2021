IEnumerable<int> GetInput() =>
    Console.ReadLine()
        .Split(',')
        .Select(x => int.Parse(x));

var fish = GetInput().ToList();

foreach (var _ in Enumerable.Range(0, 80)) {
    var newFish = 0;
    for (var i = 0; i < fish.Count; i++) {
        var counter = fish[i];
        if (counter > 0) {
            fish[i] = counter - 1;
        } else {
            fish[i] = 6;
            newFish += 1;
        }
    }

    fish.AddRange(Enumerable.Repeat(8, newFish));
}

Console.WriteLine($"After 80 days there are {fish.Count} fish");