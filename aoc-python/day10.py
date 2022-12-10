import os, re, time, contextlib
import textwrap
from collections import deque

def get_input():
    file_dir = os.path.dirname(os.path.realpath('__file__'))
    filename = os.path.join(file_dir, '../resources/day-10.txt')
    f = open(filename, "r")

    commands = [line.strip() for line in f.readlines()]
    return commands

def cmd_amount(line):
    return int(line.split()[1])

def calc_signal_strength(input):
    queue = deque(input)

    cmd = None
    cmd_remaining_cycles = 0
    cycle = 0 
    x = 1
    total_signal_strength = 0
    while queue:
        if cmd_remaining_cycles == 0:
            if cmd is not None and "addx" in cmd:
                x += cmd_amount(cmd)

            cmd = queue.popleft()
            cmd_remaining_cycles = 2 if "addx" in cmd else 1
        
        cycle += 1
        cmd_remaining_cycles -= 1

        if (cycle - 20) % 40 == 0:
            total_signal_strength += cycle * x

    return total_signal_strength

def display_crt(input):
    queue = deque(input)

    cmd = None
    cmd_remaining_cycles = 0
    cycle = 1 
    x = 1
    crt = ""
    while queue:

        if cmd_remaining_cycles == 0:
            if cmd is not None and "addx" in cmd:
                x += cmd_amount(cmd)

            cmd = queue.popleft()
            cmd_remaining_cycles = 2 if "addx" in cmd else 1

        sprite_values = [x - 1, x, x + 1]
        should_write = ((cycle % 40) - 1) in sprite_values

        crt += "#" if should_write else '.'
        
        cycle += 1
        cmd_remaining_cycles -= 1

    for row in textwrap.wrap(crt, 40):
        print(row)

@contextlib.contextmanager
def timer():
    start = time.perf_counter()
    yield 
    stop = time.perf_counter()
    result = (stop - start) / 1000
    print("Time to execute:", result, "ms")


if __name__ == '__main__':
    commands = get_input()

    with timer():
        print("part 1: ", calc_signal_strength(commands))

    with timer():
        print("part 2: ", display_crt(commands))