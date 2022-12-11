import math
import os
import re
import time
import contextlib

def get_input():
    file_dir = os.path.dirname(os.path.realpath('__file__'))
    filename = os.path.join(file_dir, '../resources/day-11-example.txt')
    f = open(filename, "r")

    input = [line.strip() for line in f.readlines()]
    main, current_monkey = [], []
    for line in input:
        if line == "":
            main.append(current_monkey)
            current_monkey = []
        else:
            current_monkey.append(line)

    main.append(current_monkey)
    return main


class Monkey():
    def __init__(self) -> None:
        self.items = []
        self.operation = ""
        self.divisible_by = 0
        self.test_ids = {}
        self.monkey_if_divisible = None
        self.monkey_if_not_divisible = None
        self.inspected_item_count = 0

    def inspect_items(self):
        # print("inspecting the following items", self.items)
        # print()
        for item in self.items:
            # print("inspecting",idx, item, "for monkey", self.id)
            self.inspected_item_count += 1
            new_worry_level = eval(self.operation, {"old": item}) // 3
            # print("new worry level", new_worry_level)
            if new_worry_level % self.divisible_by == 0:
                self.monkey_if_divisible.items.append(new_worry_level)
                # print(" ", "given to monkey", self.monkey_if_divisible.id)
            else: 
                self.monkey_if_not_divisible.items.append(new_worry_level)
                # print(" ", "given to monkey", self.monkey_if_not_divisible.id)

        self.items = []

    def __repr__(self) -> str:
        return f"<Monkey {self.id}: {self.items}, true: {self.monkey_if_divisible.id}, false: {self.monkey_if_not_divisible.id}>"


def parse_as_monkey(input):
    m = Monkey()
    m.id = re.search("\d+", input[0]).group()
    m.items = [int(x) for x in input[1].split(":")[1].split(",")]
    m.operation = input[2].split(" = ")[1]
    m.operation = input[2].split(" = ")[1]
    m.divisible_by = int(input[3].split("divisible by ")[1])
    m.test_ids = {
        "true": int(input[4].split("throw to monkey ")[1]),
        "false": int(input[5].split("throw to monkey ")[1])
        }

    return m

def get_monkeys(input):
    monkeys = [parse_as_monkey(x) for x in input]

    for m in monkeys:
        m.monkey_if_divisible = monkeys[m.test_ids["true"]]
        m.monkey_if_not_divisible = monkeys[m.test_ids["false"]]

    return monkeys

def determine_monkey_business(rounds, monkeys):

    for _ in range(rounds):
        for m in monkeys:
            m.inspect_items()

    all_inspected_item_counts = [m.inspected_item_count for m in monkeys]

    for m in monkeys:
        print(m.id, m.inspected_item_count)

    return math.prod(sorted(all_inspected_item_counts)[-2:])

@contextlib.contextmanager
def timer():
    start = time.perf_counter()
    yield
    stop = time.perf_counter()
    result = (stop - start) / 1000
    print("Time to execute:", result, "ms")


if __name__ == '__main__':
    input = get_input()

    monkeys = get_monkeys(input)

    with timer():
        print("part 1: ", determine_monkey_business(rounds=20, monkeys=monkeys))

