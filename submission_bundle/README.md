# Workshop 6 Submission Bundle

This folder contains the exact bundle requested by the instructor:

1. AI prompt
2. Generated source code
3. Test cases
4. Adequacy assessment

## Contents

- `AI_PROMPT.md`
- `GENERATED_SOURCE_CODE.md`
- `TEST_CASES.md`
- `ADEQUACY_ASSESSMENT.md`
- `generated/` (source and test files)
- `evidence/` (analyzer and Maven outputs)

## Quick verification commands

```powershell
py analysis/du_analyzer.py analysis/agent_generated_code.py
py analysis/lkw_analyzer.py analysis/agent_generated_code.py
mvn test
```
