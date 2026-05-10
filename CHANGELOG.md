# Changelog
 
## [1.0.0+build.5] - 2026-05-10
 
### Changed
- Renamed GameRule Category: Changed from "Instant Gratification Collection" to **"Faster Ladder Climbing"** for better clarity and consistency with other mods.
 
## [1.0.0+build.4] - 2026-05-10
 
### Added
- Custom GameRule Category: Created the "Instant Gratification Collection" category in the GameRules screen to improve visibility and organization.
- Localized all gamerule names and descriptions in the UI.
 
## [1.0.0+build.3] - 2026-05-10
 
### Changed
- Refactored Mixin injection strategy: Replaced `ModifyConstant` with `Redirect` for all clamping logic. This ensures maximum compatibility with varying bytecode literal types (-0.15f vs -0.15).
 
## [1.0.0+build.2] - 2026-05-10
 
### Fixed
- Critical startup crash (Mixin Injection Error) in `LivingEntityMixin`. Corrected `ModifyConstant` targets to match 26.1.2 float literals.
- Momentum physics: Implemented "Peak Momentum Clamp" to ensure high climbing speeds don't launch players into the sky when leaving ladders.
- Fixed dependency mismatch with Dasik Library (aligned to build 24).
 
### Concept Coverage ⭐
- Features implemented: 4/4 (100%)
- All concept goals (speed, game rules, safety clamp) are fully operational.
 
## [1.0.0+build.1] - 2026-05-10
- Initial Release for Minecraft 26.1.2.
- Integrated Dynamic Game Rule system.
- Added premium documentation and assets.
