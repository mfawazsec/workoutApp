# Claude Code Guidelines

## Workflow Orchestration

### Plan Node Default
- For non-trivial tasks, write a detailed plan before starting implementation.
- Stop and re-plan if unexpected issues arise mid-task.
- Use plan mode to verify approach before making changes.
- Write detailed specifications so intent is clear and auditable.

### Subagent Strategy
- Use subagents to maintain clean context for complex problems.
- Offload research and exploration tasks to subagents to preserve main context.
- Delegate focused execution (e.g., isolated file changes, test runs) to subagents when appropriate.

### Self-Improvement Loop
- After corrections, update `tasks/lessons.md` with what went wrong and how to prevent it.
- Write explicit rules to avoid repeating the same mistakes.
- Review `tasks/lessons.md` at the start of each project session.
- Iterate on lessons — keep them concise, accurate, and actionable.

### Verification Before Done
- Prove task completion before declaring it done (run tests, check output).
- Diff changes against the main branch to confirm scope is correct.
- Seek user approval before marking work complete on significant changes.
- Always run relevant tests; do not skip verification steps.

### Demand Elegance (Balanced)
- For non-trivial changes, seek the simplest and most elegant solution.
- Challenge your own implementation — ask if there's a cleaner way.
- Avoid clever hacks; prefer readable, maintainable code.
- Elegance should not come at the cost of correctness or clarity.

### Autonomous Bug Fixing
- Fix bugs directly from reports without waiting for external guidance.
- Point to logs, errors, or failing tests to diagnose root causes.
- Fix CI failures autonomously when the cause is clear from output.
- Never apply temporary patches — find and fix the actual root cause.

---

## Task Management

| Step | Action |
|------|--------|
| **Plan First** | Write a plan to `tasks/todo.md` with checkable items before starting. |
| **Verify Plan** | Check in with the user before beginning implementation. |
| **Track Progress** | Mark items complete as you finish them — do not batch completions. |
| **Explain Changes** | Provide a high-level summary at each meaningful step. |
| **Document Results** | Add a review section to `tasks/todo.md` when the task is done. |
| **Capture Lessons** | Update `tasks/lessons.md` after any corrections or course changes. |

---

## Core Principles

### Simplicity First
- Make every change as simple as possible.
- Minimize the blast radius — touch only what is necessary.
- Avoid adding features, abstractions, or error handling beyond what was asked.

### No Laziness
- Always find the root cause; never apply a workaround when a real fix exists.
- Hold yourself to senior developer standards — the work should be correct, not just functional.
- Read code before modifying it; understand before changing.

### Minimal Impact
- Changes should only touch code directly relevant to the task.
- Avoid introducing side effects or unintended behavior in unrelated areas.
- When in doubt, do less and confirm with the user.
