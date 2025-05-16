## Branching Strategy (GitFlow)

### Branch Naming Conventions

- **main**  
  – Always contains production-ready code.  
- **develop**  
  – Integration branch for ongoing development.  
- **feature/***<name>***  
  – New features or enhancements.  
  – Branch off `develop`, e.g. `feature/add-health-endpoint`.  
- **release/***<version>***  
  – Preparation for a new release.  
  – Branch off `develop`, e.g. `release/1.2.0`.  
- **hotfix/***<issue>***  
  – Urgent fixes to production.  
  – Branch off `main`, e.g. `hotfix/fix-nullpointer`.  
- **support/***<version>***  
  – Maintenance on older releases (optional).  
  – Branch off `main`, e.g. `support/1.1.x`.

### Merge Policies

1. **Pull Requests Only**  
   - All changes must be merged via pull request (PR).  
   - No direct pushes to `main` or `develop`.

2. **Review & Approvals**  
   - At least **one** approving review required before merge.  
   - The author may not approve their own PR.

3. **Status Checks**  
   - CI build must pass (compile + tests + static analysis).  
   - Coverage and lint checks must all be green.

4. **Fast-Forward Merges**  
   - Prefer fast-forward or “Squash and merge” to keep history clean.  
   - For release and hotfix merges, include the version tag in the commit message.

5. **Cleaning Up**  
   - Delete feature/release/hotfix branches after merge.  
   - Tag `main` after merging a `release/` or `hotfix/` branch, e.g. `v1.2.0`.

---

*Example Workflow:*  
```bash
# Start a feature
git checkout develop
git checkout -b feature/new-endpoint

# Push & open PR
git push -u origin feature/new-endpoint
# → Open PR targeting `develop`

# After approval & CI:
# Merge PR → develop, delete branch

# Create a release
git checkout develop
git checkout -b release/1.2.0
# Bump version, finalize
git push -u origin release/1.2.0
# → Open PR targeting `main`
# After merge: tag main (`v1.2.0`), merge back into develop, delete release branch

