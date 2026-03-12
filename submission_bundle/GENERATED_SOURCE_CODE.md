# Generated Source Code

The generated (agent-style) code artifacts included in this bundle are:

- `generated/AgentGeneratedAdaptiveCruiseController.java`
- `generated/agent_generated_code.py`

These represent the initial black-box output used for structural auditing.

## Why these files are included

- The Java file matches the workshop's ACC component context.
- The Python sample is used directly by the static analyzers for DU and LKW audits.

## Known structural issues intentionally preserved

- Repeated redefinitions (`margin`, `targetVelocity`, `speedCap`) before stable use.
- Definition-termination patterns useful for demonstrating All-Defs violations.
