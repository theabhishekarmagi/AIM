# Source provenance and licensing

This repository imports AURA, originally authored by Dinesh Kumar C and its contributors.
A fresh Git history does not imply original authorship of the imported implementation.

- Upstream: https://github.com/Dinesh210805/aura-android-use
- Imported revision: `b47cad99978dbc4f070d21c955467640e73bb5d0`
- Import metadata updated: 28 October 2026.

The application remains licensed under GNU AGPL v3 or later, with the specific additional
permission in `LICENSE-EXCEPTION.md`. Original copyright and attribution notices remain.
See `LICENSE`, `LICENSE-EXCEPTION.md`, and `THIRD_PARTY_NOTICES.md`. The desktop bridge
in `aura-mcp-connect/` has its own MIT license. A fresh repository does not remove these
obligations or grant proprietary distribution rights.

The imported Android, desktop bridge, and website implementation files are unchanged.
Repository metadata adjustments are limited to:

- Excluding `local.properties` and `.gradle/` from version control.
- Moving the original CLA workflow to `.github/upstream-cla.yml.disabled`, where GitHub
  Actions does not execute it. `CLA.md` is upstream reference, not an adopted agreement
  for contributions to this checkout; the contribution instructions say so explicitly.
- Adding this provenance record.

This source import does not include a working Firebase project, model-provider credentials,
or release-signing credentials. Configure independently controlled services before building
or operating a customized distribution; see the upstream build and `remote/README.md`
instructions. No Android build or device compatibility test is established by this import.
