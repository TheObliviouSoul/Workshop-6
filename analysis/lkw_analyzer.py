import ast
import sys
from collections import defaultdict
from pathlib import Path


class LKWAnalyzer(ast.NodeVisitor):
    def __init__(self):
        self.active_definitions = {}
        self.terminations = []
        self.p_uses = []
        self.c_uses = []
        self._predicate_locations = set()

    def visit_Assign(self, node):
        for target in node.targets:
            for name in self._extract_names(target):
                self._register_definition(name, node.lineno)
        self.generic_visit(node)

    def visit_AnnAssign(self, node):
        for name in self._extract_names(node.target):
            self._register_definition(name, node.lineno)
        self.generic_visit(node)

    def visit_AugAssign(self, node):
        for name in self._extract_names(node.target):
            self._register_definition(name, node.lineno)
        self.generic_visit(node)

    def visit_If(self, node):
        self._capture_predicate_uses(node.test, node.lineno)
        self.generic_visit(node)

    def visit_While(self, node):
        self._capture_predicate_uses(node.test, node.lineno)
        self.generic_visit(node)

    def visit_IfExp(self, node):
        self._capture_predicate_uses(node.test, node.lineno)
        self.generic_visit(node)

    def visit_Name(self, node):
        if isinstance(node.ctx, ast.Load):
            key = (node.id, node.lineno)
            if key not in self._predicate_locations:
                self.c_uses.append((node.id, node.lineno))
            if node.id in self.active_definitions:
                self.active_definitions.pop(node.id)
        self.generic_visit(node)

    def _capture_predicate_uses(self, test_node, line):
        for name_node in ast.walk(test_node):
            if isinstance(name_node, ast.Name) and isinstance(name_node.ctx, ast.Load):
                self.p_uses.append((name_node.id, line))
                self._predicate_locations.add((name_node.id, name_node.lineno))

    def _extract_names(self, node):
        if isinstance(node, ast.Name):
            return [node.id]
        if isinstance(node, (ast.Tuple, ast.List)):
            names = []
            for element in node.elts:
                names.extend(self._extract_names(element))
            return names
        return []

    def _register_definition(self, variable, lineno):
        if variable in self.active_definitions:
            previous_line = self.active_definitions[variable]
            self.terminations.append((variable, previous_line, lineno))
        self.active_definitions[variable] = lineno


def analyze_file(path):
    tree = ast.parse(Path(path).read_text(encoding="utf-8"))
    analyzer = LKWAnalyzer()
    analyzer.visit(tree)
    return analyzer


def summarize_uses(name, uses):
    grouped = defaultdict(list)
    for variable, line in uses:
        grouped[variable].append(line)

    print(name)
    if not grouped:
        print("  - none")
        return
    for variable in sorted(grouped.keys()):
        lines = ", ".join(str(line) for line in sorted(grouped[variable]))
        print(f"  - {variable}: lines {lines}")


def main():
    if len(sys.argv) != 2:
        print("Usage: py analysis/lkw_analyzer.py <python_file>")
        sys.exit(1)

    source = sys.argv[1]
    analyzer = analyze_file(source)

    print(f"LKW structural analysis for: {source}")
    summarize_uses("P-uses (predicate)", analyzer.p_uses)
    summarize_uses("C-uses (computational)", analyzer.c_uses)

    if analyzer.terminations:
        print("Terminating definitions:")
        for variable, old_line, new_line in analyzer.terminations:
            print(f"  - {variable}: def@{old_line} killed by def@{new_line}")
    else:
        print("Terminating definitions: none")

    if analyzer.active_definitions:
        print("Definitions without observed use:")
        for variable, line in sorted(analyzer.active_definitions.items(), key=lambda item: item[1]):
            print(f"  - {variable}: def@{line}")
    else:
        print("Definitions without observed use: none")


if __name__ == "__main__":
    main()
