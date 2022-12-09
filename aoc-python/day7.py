import os, re, time, contextlib
from collections import deque

def get_commands():
    file_dir = os.path.dirname(os.path.realpath('__file__'))
    filename = os.path.join(file_dir, '../resources/day-07.txt')
    f = open(filename, "r")

    commands = deque([line.strip() for line in f.readlines()])
    return commands

class Node():
    def __init__(self, name, parent) -> None:
        self.name = name
        self.parent = parent
        self.files = []
        self.children = []

    def total_size(self):
        return sum(self.files) + sum(x.total_size() for x in self.children)

    def __repr__(self) -> str:
        return f"<node {self.name}, {len(self.files)} files, {len(self.children)} children>"

def generate_nodes(commands):
    nodes = []
    current_command = ""
    current_node = None
    while commands:
        current_command = commands.popleft()

        if "$ cd .." in current_command:
            current_node = current_node.parent

        elif "$ cd" in current_command:
            path = current_command[5:]

            if current_node is None:
                current_node = Node(path, None)
                nodes.append(current_node)
                continue

            new_node = Node(path, current_node)
            nodes.append(new_node)

            current_node.children.append(new_node)

            current_node = new_node

        elif re.search("\d+", current_command) is not None:
            size = int(re.search("\d+", current_command).group())
            current_node.files.append(size)

    return nodes

def calculate_dir_size(nodes):
    return sum(x.total_size() 
                    for x in nodes 
                    if x.total_size() <= 100000)

def is_large_enough(node, root_node):
    total_space = 70_000_000
    target_space = 30_000_000
    used_space = root_node.total_size()
    return (total_space - (used_space - node.total_size())) > target_space

def get_dir_to_delete(nodes):
    potential_dirs = [x.total_size() 
                        for x in nodes 
                        if is_large_enough(x, nodes[0])]
    return min(potential_dirs)

@contextlib.contextmanager
def timer():
    start = time.perf_counter()
    yield 
    stop = time.perf_counter()
    result = (stop - start) / 1000
    print("Time to execute:", result, "ms")

if __name__ == '__main__':
    commands = get_commands()
    nodes = generate_nodes(commands)

    with timer():
        print("part 1: ", calculate_dir_size(nodes))

    with timer():
        print("part 2: ", get_dir_to_delete(nodes))