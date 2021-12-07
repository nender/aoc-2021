IEnumerable<int> GetInput() =>
    Console.ReadLine()
        .Split(',')
        .Select(x => int.Parse(x));

var crabs = GetInput();
var minCrab = crabs.Min();
var maxCrab = crabs.Max();

var minCost = int.MaxValue;
var minIndex = int.MaxValue;
for (var i = minCrab; i <= maxCrab; i++) {
    var cost = crabs.Select(x => Math.Abs(x - i))
        .Sum();
    
    if (cost < minCost) {
        minCost = cost;
        minIndex = i;
    }
}

Console.WriteLine($"Min cost is: {minCost}");