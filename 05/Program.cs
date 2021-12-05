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

Dictionary<(int, int), int> CreateWorld(IEnumerable<Line> lines) {
    var world = new Dictionary<(int, int), int>();

    foreach (var line in lines) {
        var sorted = new[] {line.start, line.end}
            .OrderBy(x => x.x)
            .ToArray();

        var (x0, y0) = sorted[0];
        var (x1, y1) = sorted[1];

        if (x0 == x1) {
            if (y0 > y1) {
                Swap(ref x0, ref x1);
                Swap(ref y0, ref y1);
            }

            for (var y = y0; y <= y1; y++) {
                world.CountOccurance((x1, y));
            }
        } else if (y0 == y1) {
            for (var x = x0; x <= x1; x++) {
                world.CountOccurance((x, y0));
            }
        } else {
            var slope = y0 < y1 ? 1 : -1;
            for (var (x, y) = (x0, y0); x <= x1; x++,y+=slope) {
                world.CountOccurance((x, y));
            }
        }
    }

    return world;
}

void SolvePartOne(IEnumerable<Line> lines) {
    var onlyAxisAligned = lines.Where(l => l.start.x == l.end.x || l.start.y == l.end.y );
    var world = CreateWorld(onlyAxisAligned);

    var count = world.Values.Where(x => x > 1).Count();
    Console.WriteLine($"Part one: world has {count} intersections");
}

void SolvePartTwo(IEnumerable<Line> lines) {
    var world = CreateWorld(lines);

    var count = world.Values.Where(x => x > 1).Count();
    var intersects = world.Where(x => x.Value > 1);
    Console.WriteLine($"Part two: world has {count} intersections");
}

void Swap<T>(ref T a, ref T b) {
    T temp = a;
    a = b;
    b = temp;
}

var lines = GetInput();
SolvePartOne(lines);
SolvePartTwo(lines);

public record struct Line((int x, int y) start, (int x, int y) end);

public static class Extensions {
    public static void CountOccurance<K>(this Dictionary<K, int> dict, K key) {
            if (dict.ContainsKey(key)) {
                dict[key] += 1;
            } else {
                dict[key] = 1;
            }
    }
}