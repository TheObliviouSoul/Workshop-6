# Workshop 6 Phase Implementation Guide

This document explains how each phase of the workshop was implemented in this repository.

## Phase 1: Agentic Generation (Black Box Problem)

### Goal
Generate an AI-style Adaptive Cruise Control (ACC) module and treat it as untrusted output that must be structurally audited.

### Implementation
- AI-style reference code:
  - `src/main/java/ads/workshop/legacy/AgentGeneratedAdaptiveCruiseController.java`
  - `analysis/agent_generated_code.py`
- These versions intentionally include patterns that can produce data flow anomalies (redefinitions and overwritten values).

### Why this satisfies Phase 1
- The workshop requires starting from generated logic before proving structural integrity.
- The `legacy` and `analysis` files serve as the black-box output to audit in later phases.

## Phase 2: All-Definitions (Dead Data Audit)

### Goal
Detect definitions that are overwritten before use (terminating definitions / dead definitions).

### Implementation
- Static analyzer:
  - `analysis/du_analyzer.py`
- Target analyzed:
  - `analysis/agent_generated_code.py`

### How to run
```powershell
py analysis/du_analyzer.py analysis/agent_generated_code.py
```

### Expected evidence
- Output reports terminating definitions such as:
  - `margin`
  - `speed_cap`
  - `target_velocity`

### Why this satisfies Phase 2
- All-Defs requires each definition to reach at least one use through a definition-clear path.
- Reported terminating definitions demonstrate the exact violations the phase asks you to find.

## Phase 3: All-Uses (Logic Leak Test, P-Use and C-Use)

### Goal
Verify that critical definitions propagate to all intended uses and classify predicate vs computational uses.

### Implementation
- Extended structural analyzer:
  - `analysis/lkw_analyzer.py`
- Uses the same agent-generated target:
  - `analysis/agent_generated_code.py`
- Corrected production ACC logic:
  - `src/main/java/ads/workshop/AdaptiveCruiseController.java`
- All-Defs/All-Uses oriented tests:
  - `src/test/java/ads/workshop/AdaptiveCruiseControllerLkwTest.java`

### How to run
```powershell
py analysis/lkw_analyzer.py analysis/agent_generated_code.py
```

### Expected evidence
- P-uses are reported for branch conditions (`is_obstacle_detected`, `radar_dist`).
- C-uses are reported for arithmetic and return propagation.
- Terminating definitions are still detected in the generated sample, demonstrating logic-leak risk.

### Why this satisfies Phase 3
- The phase requires verifying propagation of definitions into all relevant uses.
- P-use/C-use categorization and tests directly demonstrate this.

## Phase 4: Antidecomposition (Weyuker Axiom 7)

### Goal
Show that tests adequate for the full program (`P`) can still be inadequate for an internal component (`Q`) unless component tests are added.

### Implementation
- Program `P`:
  - `src/main/java/ads/workshop/VehicleSpeedChecker.java`
- Component `Q`:
  - `src/main/java/ads/workshop/SensorParser.java`
- Nested tests proving system-level vs isolated component adequacy:
  - `src/test/java/ads/workshop/AntidecompositionTest.java`

### Why this satisfies Phase 4
- `ProgramP` tests validate high-level behavior.
- `ComponentQ` tests validate parser constraints and malformed input handling.
- This demonstrates antidecomposition exactly: adequacy at `P` does not guarantee adequacy at `Q`.

## Additional Coverage: All-DU-Paths in Loop Logic

### Goal
Exercise definition-use paths across loop re-entry to catch state drift issues.

### Implementation
- Loop-based module:
  - `src/main/java/ads/workshop/RadarTracker.java`
- Tests for direct, single-iteration, and multi-iteration paths:
  - `src/test/java/ads/workshop/RadarTrackerAllDuPathsTest.java`

### Why this matters
- Reinforces the workshop requirement for deeper path coverage beyond simple branch execution.

## Full Verification

Run the complete suite:
```powershell
mvn test
```

Expected result:
- `BUILD SUCCESS`
- `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`

## Related Summary File

- `WORKSHOP_SUBMISSION.md` contains a concise grading-style submission summary.
