# AI Prompt Used for Generation

## Prompt

Generate a Java class `AdaptiveCruiseController` that takes `currentSpeed`, `radarDist`, `v2xSpeedLimit`, and `isObstacleDetected` as inputs and computes `targetVelocity`.

Logic requirements:

1. If an obstacle is detected, set velocity to `0`.
2. If no obstacle is detected, set velocity to the lower of `currentSpeed` and `v2xSpeedLimit`.

## Notes

- The workshop PDF phrase mentions `radarDist`, `v2xSpeedLimit`, and `isObstacleDetected`.
- `currentSpeed` is included here because the required comparison ("lower of current speed or V2X limit") needs it explicitly.
