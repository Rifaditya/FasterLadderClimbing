package net.instantgratification.fasterladderclimbing.mixin;
 
import net.instantgratification.fasterladderclimbing.FasterLadderClimbingFabric;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
 
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
 
    @Shadow public abstract boolean onClimbable();
 
    @Unique
    private boolean fasterladderclimbing$wasOnClimbableLastTick = false;
 
    // Verified against: LivingEntity.java (26.1.2 Release)
 
    /**
     * Scales the climbing speed when jumping or colliding with a ladder.
     * Original constant: 0.2
     */
    @Redirect(method = "handleRelativeFrictionAndCalculateMovement", at = @At(value = "NEW", target = "(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
    private Vec3 fasterladderclimbing$scaleAscendingSpeed(double x, double y, double z) {
        LivingEntity entity = (LivingEntity) (Object) this;
        int multiplierPercent = DynamicGameRuleManager.getInt(entity.level(), FasterLadderClimbingFabric.CLIMBING_SPEED_MULTIPLIER);
        
        double multiplier = multiplierPercent / 100.0;
        double scaledY = 0.2 * multiplier;
        
        // Safety Cap: Prevent shooting into the sky (max 0.8 blocks/tick)
        scaledY = Math.min(scaledY, 0.8);
        
        return new Vec3(x, scaledY, z);
    }
 
    @Unique
    private double fasterladderclimbing$getMultiplier() {
        LivingEntity entity = (LivingEntity) (Object) this;
        return DynamicGameRuleManager.getInt(entity.level(), FasterLadderClimbingFabric.CLIMBING_SPEED_MULTIPLIER) / 100.0;
    }
 
    /**
     * Scales the horizontal clamping in handleOnClimbable.
     */
    @Redirect(method = "handleOnClimbable", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(DDD)D"))
    private double fasterladderclimbing$redirectHorizontalClamp(double value, double min, double max) {
        double multiplier = fasterladderclimbing$getMultiplier();
        return net.minecraft.util.Mth.clamp(value, min * multiplier, max * multiplier);
    }
 
    /**
     * Scales the vertical downward clamping (sliding speed) in handleOnClimbable.
     */
    @Redirect(method = "handleOnClimbable", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"))
    private double fasterladderclimbing$redirectVerticalMax(double a, double b) {
        // b is the -0.15 constant
        double multiplier = fasterladderclimbing$getMultiplier();
        return Math.max(a, b * multiplier);
    }
 
    /**
     * Implements the "Peak Clamp" to ensure speed only works while on ladder.
     * Prevents momentum carry-over after leaving the ladder.
     */
    @Inject(method = "handleRelativeFrictionAndCalculateMovement", at = @At("RETURN"), cancellable = true)
    private void fasterladderclimbing$applyPeakClamp(Vec3 input, float friction, CallbackInfoReturnable<Vec3> cir) {
        boolean currentlyOnClimbable = this.onClimbable();
        Vec3 result = cir.getReturnValue();
 
        if (!currentlyOnClimbable && fasterladderclimbing$wasOnClimbableLastTick) {
            // We just left the ladder. If our velocity is high, damp it immediately to prevent launching.
            if (result.y > 0.2) {
                cir.setReturnValue(new Vec3(result.x, 0.2, result.z));
            }
        }
 
        fasterladderclimbing$wasOnClimbableLastTick = currentlyOnClimbable;
    }
}
