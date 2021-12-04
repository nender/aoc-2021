(IEnumerable<int>, IEnumerable<Board>) GetInput() {
    var draws = Console.ReadLine()
        .Split(',')
        .Select(x => int.Parse(x));

    string? line = Console.ReadLine();

    var boards = new List<Board>();
    var values = new int[5][];
    var rowIndex = 0;

    while (line != null) {
        if (line == "") {
            line = Console.ReadLine();
            continue;
        }

        var row = line.Split(' ', options: StringSplitOptions.RemoveEmptyEntries)
            .Select(x => int.Parse(x))
            .ToArray();
        
        values[rowIndex] = row;
        rowIndex += 1;
        
        if (rowIndex == 5) {
            boards.Add(new Board(values));
            Array.Clear(values);
            rowIndex = 0;
        }

        line = Console.ReadLine();
    }

    return (draws, boards);
}

(var draws, var boards) = GetInput();
Console.WriteLine($"{draws.Count()} draws, {boards.Count()} boards");

struct Board {
    (int, bool)[][] values;

    public Board(int[][] values) {
        this.values = new (int, bool)[values.Length][];
        for (var i = 0; i < values.Length; i++) {
            this.values[i] = values[i].Select(x => (x, false)).ToArray();
        }
    }
}