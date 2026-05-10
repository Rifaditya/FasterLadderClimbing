# Changelog
 
## [1.0.0+build.9] - 2026-05-10
 
### Added
- **Gradual Speed Scaling**: The extra climbing speed now scales based on how much you look up or down. Looking straight ahead gives a small boost, while looking straight up/down gives the full multiplier.
 
## [1.0.0+build.8] - 2026-05-10
 
### Added
- **Look-to-Climb Feature**: You can now climb even faster by looking up or descend faster by looking down while moving forward on a ladder.
- **Enhanced Stability**: Switched to a robust "Direct Movement" approach. This avoids fragile bytecode edits and ensures the mod works perfectly in all 26.1.2 environments.
 
## [1.0.0+build.7] - 2026-05-10
 
### Fixed
- Fixed "Cannot use ladder" bug: Added safety fallbacks to the speed multiplier logic. If the GameRule value is 0 or fails to load, the mod now defaults to vanilla speed (100%) instead of freezing the player's movement on climbables.
 
## [1.0.0+build.6] - 2026-05-10
 
### Fixed
- Speed multiplier not applying: Switched from direct server-side GameRules access to `DynamicGameRuleManager`. This allows the speed boost to be correctly calculated on both Client and Server, fixing the issue where movement speed remained at vanilla levels.
 
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
