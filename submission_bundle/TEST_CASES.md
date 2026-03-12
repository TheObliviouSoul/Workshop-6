# Test Cases

The test suite is provided as source files in `generated/`:

- `generated/AdaptiveCruiseControllerLkwTest.java`
- `generated/RadarTrackerAllDuPathsTest.java`
- `generated/AntidecompositionTest.java`

## Coverage mapping

## All-Definitions and All-Uses (ACC)

File: `generated/AdaptiveCruiseControllerLkwTest.java`

- Obstacle branch path reaches return use.
- Radar distance emergency-stop path reaches return use.
- Current speed lower-than-limit path.
- V2X limit lower-than-current path.
- Negative input sanitization path.

## All-DU-Paths (loop/state paths)

File: `generated/RadarTrackerAllDuPathsTest.java`

- Direct path with no loop iteration.
- Single-iteration path without reset.
- Multi-iteration path with reset and re-entry.

## Antidecomposition (Program P vs Component Q)

File: `generated/AntidecompositionTest.java`

- System-level tests for `VehicleSpeedChecker` behavior (Program P).
- Isolated parser integrity tests for `SensorParser` (Component Q).
- Invalid component output propagating to Program P exception path.
