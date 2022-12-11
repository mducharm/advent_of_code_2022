import math
import inspect
import os
import re
import time
import contextlib
from typing import Callable, List


def get_input(txt_name):
    file_dir = os.path.dirname(os.path.realpath('__file__'))
    filename = os.path.join(file_dir, f'../resources/{txt_name}.txt')
    f = open(filename, "r")

    input = [line.strip() for line in f.readlines()]
    return input


def parse_starting_items(input: str) -> List[int]:
    nums = input.split(":")[1].split(",")
    return [int(x) for x in nums]


def parse_operation(input: str) -> Callable[[int], int]:
    match input.split():
        case [*_, "old", "*", "old"]:
            return lambda x: x**2
        case [*_, "old", "*", num]:
            return lambda x: x * int(num)
        case [*_, "old", "+", num]:
            return lambda x: x + int(num)


def parse_monkeys(input):

    starting_items = [parse_starting_items(x) for x in input if "Starting items" in x]
    operations = [parse_operation(x) for x in input if "Operation" in x]
    tests = [int(x.split()[-1]) for x in input if "Test" in x]
    if_true = [int(x.split()[-1]) for x in input if "If true" in x]
    if_false = [int(x.split()[-1]) for x in input if "If false" in x]

    total_monkeys = len(starting_items)

    return {
        "total_monkeys": total_monkeys,
        "items": starting_items,
        "operations": operations,
        "tests": tests,
        "if_true": if_true,
        "if_false": if_false,
        "inspected_count": list(0 for _ in range(total_monkeys))
    }


def advance_round(monkeys, calc_worry_level):

    for id in range(monkeys["total_monkeys"]):
        op = monkeys["operations"][id]
        test = monkeys["tests"][id]
        current_monkey_items = monkeys["items"][id]
        for item in current_monkey_items:
            new_worry_level = calc_worry_level(op(item))
            condition = "if_true" if new_worry_level % test == 0 else "if_false"
            new_monkey_id = monkeys[condition][id]
            monkeys["items"][new_monkey_id].append(new_worry_level)

            monkeys["inspected_count"][id] += 1

        monkeys["items"][id] = []


def determine_monkey_business(rounds, monkeys, calc_worry_level) -> int:
    for _ in range(rounds):
        advance_round(monkeys, calc_worry_level)

    return math.prod(sorted(monkeys["inspected_count"])[-2:])

@contextlib.contextmanager
def timer():
    start = time.perf_counter()
    yield
    stop = time.perf_counter()
    result = round((stop - start) / 1000, 10)
    print("Time to execute:", result, "ms")


if __name__ == '__main__':

    # part 1 example
    example_monkeys = parse_monkeys(get_input("day-11-example"))
    pt1_example = determine_monkey_business(20, example_monkeys, lambda x: x // 3)
    assert pt1_example == 10605, f"Result was {pt1_example} instead of {10605}"

    # part 1 
    monkeys = parse_monkeys(get_input("day-11"))
    with timer():
        print("part 1: ", determine_monkey_business(20, monkeys, lambda x: x // 3))

    # part 2 example
    example_monkeys = parse_monkeys(get_input("day-11-example"))
    m = math.prod(example_monkeys["tests"])
    pt2_example = determine_monkey_business(10_000, example_monkeys, lambda x: x % m)
    assert pt2_example == 2713310158, f"Result was {pt2_example} instead of {2713310158}"

    # part 2 
    monkeys = parse_monkeys(get_input("day-11"))
    m = math.prod(monkeys["tests"])
    with timer():
        print("part 2: ", determine_monkey_business(10_000, monkeys, lambda x: x % m))
