List<Order> GetInput() {
    var inputs = new List<Order>();
    string? input = Console.ReadLine();

    while (input != null) {
        var terms = input.Split(" ");
        var direction = parseDirection(terms[0]);
        var magnitude = int.Parse(terms[1]);
        Order order = new(direction, magnitude);
        inputs.Add(order);
        input = Console.ReadLine();
    }

    return inputs;
}

Direction parseDirection(string Input) =>
    Input switch
    {
        "forward" => Direction.forward,
        "up" => Direction.up,
        "down" => Direction.down,
        _ => throw new ArgumentException()
    };

var input = GetInput();

var x = 0;
var y = 0;
foreach (var order in input) {
    switch (order.Direction) {
        case Direction.forward:
            x += order.Magnitude;
            break;
        case Direction.down:
            y += order.Magnitude;
            break;
        case Direction.up:
            y -= order.Magnitude;
            break;
    }
}
Console.WriteLine($"Final depth: {y}, distance: {x}");
Console.WriteLine($"Checksum: {x*y}");

record struct Order(Direction Direction, int Magnitude);
enum Direction {
    forward,
    up,
    down
}