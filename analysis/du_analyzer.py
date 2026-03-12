import ast
import sys
from pathlib import Path


class DUAnalyzer(ast.NodeVisitor):
    def __init__(self):
        self.active_definitions = {}
        self.terminated = []
        self.uses = []

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

    def visit_Name(self, node):
        if isinstance(node.ctx, ast.Load) and node.id in self.active_definitions:
            definition_line = self.active_definitions.pop(node.id)
            self.uses.append((node.id, definition_line, node.lineno))
        self.generic_visit(node)

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
            self.terminated.append((variable, previous_line, lineno))
        self.active_definitions[variable] = lineno


def analyze_file(path):
    tree = ast.parse(Path(path).read_text(encoding="utf-8"))
    analyzer = DUAnalyzer()
    analyzer.visit(tree)
    return analyzer


def main():
    if len(sys.argv) != 2:
        print("Usage: py analysis/du_analyzer.py <python_file>")
        sys.exit(1)

    source = sys.argv[1]
    analyzer = analyze_file(source)

    print(f"DU analysis for: {source}")
    if analyzer.terminated:
        print("Terminating definitions detected:")
        for variable, old_line, new_line in analyzer.terminated:
            print(f"  - {variable}: def@{old_line} killed by def@{new_line}")
    else:
        print("No terminating definitions detected.")

    if analyzer.active_definitions:
        print("Definitions with no observed use:")
        for variable, line in sorted(analyzer.active_definitions.items(), key=lambda item: item[1]):
            print(f"  - {variable}: def@{line} not used")
    else:
        print("All tracked definitions reached at least one use.")


if __name__ == "__main__":
    main()
