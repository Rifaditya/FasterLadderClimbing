# Changelog
 
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
