IEnumerable<Line> GetInput() {
    string? line = Console.ReadLine();

    var ventLine = new List<Line>();

    while (line != null) {
        var coords = line.Split(" -> ")
            .SelectMany(x => x.Split(','))
            .Select(x => int.Parse(x))
            .Chunk(2)
            .Select(x => (x[0], x[1]))
            .ToArray();

        ventLine.Add(
            new Line(start: coords[0], end: coords[1])
        );

        line = Console.ReadLine();
    }

    return ventLine;
}

// (int, int) Min((int, int) a, (int, int) b) {
//     var (x0, y0) = a;
//     var (x1, y1) = b;
//     if ( (x0 + y0) > (x1 + y1) ) {
//         return b;
//     } else {
//         return a ;
//     }
// }

var lines = GetInput()
    .Where(l => l.start.x == l.end.x || l.start.y == l.end.y );

var world = new Dictionary<(int, int), int>();
foreach (var line in lines) {
    var sorted = new[] {line.start, line.end}
        .OrderBy(x => x.x + x.y)
        .ToArray();

    var (x0, y0) = sorted[0];
    var (x1, y1) = sorted[1];

    if (x0 == x1) {
        for (var y = y0; y <= y1; y++) {
            world.CountOccurance((x1, y));
        }
    } else {
        for (var x = x0; x <= x1; x++) {
            world.CountOccurance((x, y0));
        }
    }
}

var count = world.Values.Where(x => x > 1).Count();
Console.WriteLine($"World has {count} intersections");

record Line((int x, int y) start, (int x, int y) end);

public static class DictionaryExtension {
    public static void CountOccurance<K>(this Dictionary<K, int> dict, K key) {
            if (dict.ContainsKey(key)) {
                dict[key] += 1;
            } else {
                dict[key] = 1;
            }
    }
}