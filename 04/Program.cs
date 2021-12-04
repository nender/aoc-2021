(IEnumerable<int>, IEnumerable<Board>) GetInput() {
    var draws = Console.ReadLine()
        .Split(',')
        .Select(x => int.Parse(x));

    string? line = Console.ReadLine();

    var boards = new List<Board>();
    var numbers = new int[5][];
    var rowIndex = 0;

    while (line != null) {
        if (line == "") {
            line = Console.ReadLine();
            continue;
        }

        var row = line.Split(' ', options: StringSplitOptions.RemoveEmptyEntries)
            .Select(x => int.Parse(x))
            .ToArray();
        
        numbers[rowIndex] = row;
        rowIndex += 1;
        
        if (rowIndex == 5) {
            boards.Add(new Board(numbers));
            Array.Clear(numbers);
            rowIndex = 0;
        }

        line = Console.ReadLine();
    }

    return (draws, boards);
}

(var draws, var boards) = GetInput();
Console.WriteLine($"{draws.Count()} draws, {boards.Count()} boards");

foreach (var draw in draws) {
    foreach (var board in boards) {
        board.Mark(draw);
        if (board.HasBingo) {
            Console.WriteLine("Bingo:" + board.UnmarkedSum() * draw);
            return;
        }
    }
}

struct Board {
    (int, bool)[][] values;

    public Board(int[][] values) {
        this.values = new (int, bool)[values.Length][];
        for (var i = 0; i < values.Length; i++) {
            this.values[i] = values[i].Select(x => (x, false)).ToArray();
        }
    }

    public bool Mark(int number) {
        for (var i = 0; i < 5; i++) {
            for (var j = 0; j < 5; j++) {
                (var value, _) = values[i][j];
                if (value == number) {
                    values[i][j] = (value, true);
                    return true;
                }
            }
        }

        return false;
    }

    public bool HasBingo {
        get {
            var rowWin = values.Any(row =>
                row.Select(x => x.Item2)
                    .All(x => x)
            );

            if (rowWin) { return true; }

            var columnWin = Columns().Any(col =>
                col.Select(x => x.Item2)
                    .All(x => x)
            );

            if (columnWin) { return true; }

            return false;
        }
    }

    IEnumerable<(int, bool)[]> Columns() {
        for (var i = 0; i < 5; i++) {
            var column = new (int, bool)[5];
            for (var j = 0; j < 5; j++) {
                column[j] = values[j][i];
            }
            yield return column;
        }
    }

    public int UnmarkedSum() =>  
        values.SelectMany(row =>
            row.Where( x => !x.Item2)
                .Select(x => x.Item1)
        )
        .Sum();
}