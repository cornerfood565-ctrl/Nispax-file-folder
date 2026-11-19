# Architecture

The app follows a lightweight modular-ready MVVM/Clean Architecture direction.

- Presentation: Jetpack Compose screens and UI state.
- Domain: planned use cases and repository contracts.
- Data: Android Storage Access Framework and local persistence.
- Platform: Android document providers and content resolver.

The first alpha intentionally keeps the runnable foundation in the app module. Features will be extracted into independent Gradle modules as their boundaries stabilize.
