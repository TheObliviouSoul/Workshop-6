# Complexity, Minimum Test Cases, Dead Data, and Logic Leak Assessment

## 1. Code Complexity Assessment

Complexity was assessed with McCabe-style cyclomatic complexity:

- `M = 1 + number of decision nodes` (`if`, `else if`, `for`, `while`).

| Component / Method | Decision Nodes | Cyclomatic Complexity (M) | Risk Level |
|---|---:|---:|---|
| `AgentGeneratedAdaptiveCruiseController.calculateTargetVelocity` | 2 (`if`, `else if`) | 3 | Medium |
| `AdaptiveCruiseController.calculateTargetVelocity` | 1 (`if`) | 2 | Low |
| `RadarTracker.updateDistance` | 2 (`for`, `if`) | 3 | Medium |
| `SensorParser.parseSpeedKph` | 3 explicit `if` (+ exception path) | 4 to 5 | Medium |
| `VehicleSpeedChecker.isOverspeeding` | 1 (`if`) | 2 | Low |

Interpretation:

- The generated ACC version is more error-prone than the corrected one because it combines unnecessary variables with multiple redefinitions.
- `RadarTracker` and `SensorParser` have moderate path complexity and need path-aware tests.

## 2. Minimum Test Cases Assessment

Minimum tests are reported for two levels:

- Basis-path minimum (from complexity).
- Practical minimum for workshop adequacy (All-Defs, All-Uses, exception paths, boundary behavior).

| Target | Basis-Path Minimum | Practical Minimum | Existing Tests |
|---|---:|---:|---:|
| Corrected ACC (`AdaptiveCruiseController`) | 2 | 4 (obstacle stop, radar stop, min picks current, min picks V2X) | 5 |
| Generated ACC (legacy) | 3 | 3 (one per branch) | Static-analysis target |
| `RadarTracker.updateDistance` | 3 | 3 (no loop, loop no reset, loop with reset/reentry) | 3 |
| `SensorParser.parseSpeedKph` | 4 to 5 | 5 (null, empty, malformed, out-of-range, valid) | 4 direct + indirect exception test |
| `VehicleSpeedChecker.isOverspeeding` | 2 | 3 (true, false, invalid->exception) | 3 (2 in Program P + 1 exception in Component Q) |

Conclusion:

- Current suite meets or exceeds practical minimums for workshop criteria.

## 3. Dead Data Assessment

Primary evidence:

- `evidence/du_analyzer_output.txt`
- `evidence/lkw_analyzer_output.txt`

Detected terminating definitions in generated code (`analysis/agent_generated_code.py`):

- `target_velocity: def@3 killed by def@7`
- `margin: def@2 killed by def@9`
- `target_velocity: def@7 killed by def@10`
- `margin: def@9 killed by def@12`
- `speed_cap: def@4 killed by def@13`
- `target_velocity: def@10 killed by def@14`

Assessment:

- The generated version contains dead/overwritten definitions before stable use.
- This is an All-Definitions adequacy failure pattern in the raw generated logic.

## 4. Logic Leak Assessment

A logic leak exists when safety-relevant data is present but does not influence intended control decisions.

### Leak in generated ACC

In `AgentGeneratedAdaptiveCruiseController`:

- `v2xSpeedLimit` is assigned to `speedCap` and then overwritten by `currentSpeed` on the only branch that computes velocity.
- Result: V2X limit does not meaningfully constrain final velocity in normal flow.
- `margin` introduces an extra `+1.0` velocity bias unrelated to the stated requirement.

Impact:

- Violates intended safety logic: output should be `min(currentSpeed, v2xSpeedLimit)` when not stopping.
- Creates mismatch between requirement intent and actuator command.

### Corrected ACC status

In `AdaptiveCruiseController`:

- Stop conditions are explicit (`obstacle` or `radar <= threshold`).
- Non-stop path uses `Math.min(currentSpeed, v2xSpeedLimit)` after sanitization.
- No redundant intermediate variable overwrites affecting control semantics.

Assessment:

- Logic leak present in generated version.
- Logic leak resolved in corrected production version.
