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


record struct Order(Direction Direction, int Magnitude);
enum Direction {
    forward,
    up,
    down
}