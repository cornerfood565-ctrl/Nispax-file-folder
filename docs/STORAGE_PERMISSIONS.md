# Storage access policy

Nispax uses Android's Storage Access Framework (SAF) for user-selected folders.

- Persist read/write URI grants when the provider supports them.
- Treat document URIs as opaque identifiers; never assume they are filesystem paths.
- Handle providers that do not support rename, delete, seek, or reliable file sizes.
- Handle removable media disconnection and revoked grants.
- Do not request MANAGE_EXTERNAL_STORAGE for the core app.
- Do not claim access to protected Android/data or Android/obb directories.
