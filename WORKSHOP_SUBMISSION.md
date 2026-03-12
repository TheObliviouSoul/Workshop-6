# COEN 448 Workshop 6 Submission

## Scope
This submission completes all workshop phases from `workshop-LKW.pdf` using:
- Java implementation + JUnit 5 tests for structural criteria.
- Python static-analysis scripts for DU anomaly detection and p-use/c-use categorization.

## Phase 1: Agentic Generation (Black Box)
The workshop asks for an AI-generated ACC module first.  
I preserved a representative agent-style output here:
- `src/main/java/ads/workshop/legacy/AgentGeneratedAdaptiveCruiseController.java`

Observed issues in this generated version:
- `speedCap` is initialized from `v2xSpeedLimit` then overwritten before use on one path (logic leak risk).
- `margin` and `targetVelocity` are repeatedly redefined, creating terminating-definition patterns.

## Phase 2: All-Definitions (Dead Data Audit)
Static analyzer:
- `analysis/du_analyzer.py`

Target analyzed:
- `analysis/agent_generated_code.py`

Command:
```powershell
py analysis/du_analyzer.py analysis/agent_generated_code.py
```

Result summary:
- Multiple terminating definitions were detected (including `speed_cap` and `margin` redefinitions).
- This demonstrates All-Defs violations in generated logic structure.

## Phase 3: All-Uses (Logic Leak Test)
Extended analyzer:
- `analysis/lkw_analyzer.py`

Command:
```powershell
py analysis/lkw_analyzer.py analysis/agent_generated_code.py
```

Result summary:
- Predicate uses detected for decision variables (`is_obstacle_detected`, `radar_dist`).
- Computational uses detected for arithmetic/output variables.
- Terminating definitions were again reported, highlighting incomplete definition propagation.

## Phase 4: Corrected Safety-Critical Module
Corrected ACC module:
- `src/main/java/ads/workshop/AdaptiveCruiseController.java`

Implemented behavior:
1. Stop immediately if obstacle is detected.
2. Stop if radar distance is inside emergency stop boundary.
3. Otherwise command the lower of current speed and V2X speed limit.
4. Clamp invalid negative speed inputs to safe non-negative values.

## Phase 5: All-DU-Paths and Loop State Integrity
Loop-based tracking module:
- `src/main/java/ads/workshop/RadarTracker.java`

Tests covering direct, single-iteration, and multi-iteration paths:
- `src/test/java/ads/workshop/RadarTrackerAllDuPathsTest.java`

This demonstrates why one-iteration testing is insufficient for loop-driven data flow.

## Phase 6: Weyuker Axiom 7 (Antidecomposition)
Program `P` (system-level):
- `src/main/java/ads/workshop/VehicleSpeedChecker.java`

Component `Q` (isolated tool):
- `src/main/java/ads/workshop/SensorParser.java`

Nested tests showing P-level adequacy vs Q-level dedicated checks:
- `src/test/java/ads/workshop/AntidecompositionTest.java`

## Verification Evidence
Build and tests:
```powershell
mvn test
```

Execution result:
- `BUILD SUCCESS`
- `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`

## Files Added
- `pom.xml`
- `WORKSHOP_SUBMISSION.md`
- `analysis/agent_generated_code.py`
- `analysis/du_analyzer.py`
- `analysis/lkw_analyzer.py`
- `src/main/java/ads/workshop/AdaptiveCruiseController.java`
- `src/main/java/ads/workshop/RadarTracker.java`
- `src/main/java/ads/workshop/SensorParser.java`
- `src/main/java/ads/workshop/VehicleSpeedChecker.java`
- `src/main/java/ads/workshop/legacy/AgentGeneratedAdaptiveCruiseController.java`
- `src/test/java/ads/workshop/AdaptiveCruiseControllerLkwTest.java`
- `src/test/java/ads/workshop/RadarTrackerAllDuPathsTest.java`
- `src/test/java/ads/workshop/AntidecompositionTest.java`
