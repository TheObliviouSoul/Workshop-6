# Adequacy Assessment

This assessment combines static structural analysis and dynamic test execution evidence.

## 1. All-Definitions Audit

Evidence file:

- `evidence/du_analyzer_output.txt`

Observed result:

- Terminating definitions were detected in generated code:
  - `target_velocity`
  - `margin`
  - `speed_cap`

Interpretation:

- The generated module contains definition-kill patterns.
- This demonstrates why the raw AI output is structurally inadequate under All-Defs until corrected.

## 2. All-Uses Audit (P-Uses and C-Uses)

Evidence file:

- `evidence/lkw_analyzer_output.txt`

Observed result:

- P-uses identified for:
  - `is_obstacle_detected`
  - `radar_dist`
- C-uses identified for:
  - `current_speed`
  - `v2x_speed_limit`
  - `speed_cap`
  - `margin`
  - `target_velocity`
- Terminating definitions remain present in generated logic.

Interpretation:

- Predicate and computational use classes are both present.
- The terminating definitions indicate propagation quality problems in the generated version.

## 3. Dynamic Test Adequacy Evidence

Evidence file:

- `evidence/mvn_test_output.txt`

Observed result:

- `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`
- `BUILD SUCCESS`

Interpretation:

- The corrected implementation and test suite execute successfully.
- Structural test scenarios for All-Defs, All-Uses, All-DU-Paths, and Antidecomposition are all runnable and passing.

## Final adequacy statement

The submitted bundle demonstrates:

1. Initial structural inadequacy in AI-generated code (via static analysis).
2. Explicit test design mapped to LKW criteria and Weyuker antidecomposition.
3. Passing executable tests for the corrected implementation.
