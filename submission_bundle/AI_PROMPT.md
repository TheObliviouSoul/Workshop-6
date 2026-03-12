# AI Prompts Used for This Workshop

This file records the prompts used to produce the generated code and supporting workshop artifacts.

## Prompt 1: Initial ACC code generation

Generate a Java class `AdaptiveCruiseController` that takes `currentSpeed`, `radarDist`, `v2xSpeedLimit`, and `isObstacleDetected` as inputs and computes `targetVelocity`.

Logic requirements:

1. If an obstacle is detected, set velocity to `0`.
2. If no obstacle is detected, set velocity to the lower of `currentSpeed` and `v2xSpeedLimit`.

## Prompt 2: DU anomaly detection script

Generate a Python script using `ast` that scans a source file and reports variables that are redefined before their previous definition is used (terminating definitions / dead definitions).

## Prompt 3: LKW p-use / c-use analyzer

Generate a Python AST analyzer that:

1. Identifies predicate uses (`p-use`) in `if`/`while` conditions.
2. Identifies computational uses (`c-use`) for loaded variables in expressions.
3. Reports terminating definitions and definitions with no observed use.

## Prompt 4: Structural test generation

Generate JUnit 5 tests for:

1. All-Definitions and All-Uses on the ACC module.
2. All-DU-Paths for loop-based radar tracking logic.
3. Weyuker Axiom 7 antidecomposition with nested tests for Program `P` and Component `Q`.

## Prompt 5: Adequacy assessment write-up

Generate a concise adequacy report that includes:

1. Static analysis findings (dead data and logic leak indicators).
2. Dynamic test execution summary.
3. Mapping from LKW criteria to implemented tests.

## Notes

- The workshop PDF wording mentions `radarDist`, `v2xSpeedLimit`, and `isObstacleDetected`.
- `currentSpeed` is included because the requirement compares current speed against the V2X limit.
