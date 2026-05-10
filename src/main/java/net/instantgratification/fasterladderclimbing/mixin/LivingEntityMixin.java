package net.instantgratification.fasterladderclimbing.mixin;
 
import net.instantgratification.fasterladderclimbing.FasterLadderClimbingFabric;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
 
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
 
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }
 
    /**
     * Surgical Speed Injection (Protocol 2.2).
     * Instead of brute-forcing movement in the tick, we modify the vanilla 
     * 'handleOnClimbable' method which calculates the ladder velocity.
     * This ensures 100% compatibility with vanilla gravity, friction, and shifting.
     */
    @Inject(method = "handleOnClimbable", at = @At("RETURN"), cancellable = true)
    private void fasterladderclimbing$applySurgicalBoost(Vec3 vanillaDelta, CallbackInfoReturnable<Vec3> cir) {
        // If we're not on a climbable, the method isn't doing anything interesting.
        // If we're standing still (Shift or no input), delta will be 0.
        if (vanillaDelta.lengthSqr() < 0.0001) return;
 
        double multiplier = fasterladderclimbing$getMultiplier();
        if (multiplier <= 1.0) return;
 
        // 1. Calculate Look Intensity
        // We use a linear scale from the player's pitch. 
        // Looking straight ahead (0°) still gives a base boost (20% of max).
        // Looking straight up or down (90°) gives the full 100% boost.
        float absPitch = Math.abs(this.getXRot());
        double intensity = 0.2 + (absPitch / 90.0) * 0.8;
 
        // 2. Apply Scaled Multiplier
        // finalMultiplier = 1.0 + (extraSpeed * intensity)
        double finalMultiplier = 1.0 + (multiplier - 1.0) * intensity;
        
        // Return the vanilla vector scaled by our custom speed
        cir.setReturnValue(vanillaDelta.scale(finalMultiplier));
    }
 
    @Unique
    private double fasterladderclimbing$getMultiplier() {
        LivingEntity entity = (LivingEntity) (Object) this;
        int multiplierPercent = 150; // Default to 150 (1.5x) if something goes wrong
        try {
            multiplierPercent = DynamicGameRuleManager.getInt(entity.level(), FasterLadderClimbingFabric.CLIMBING_SPEED_MULTIPLIER);
        } catch (Exception ignored) {}
        
        if (multiplierPercent <= 0) multiplierPercent = 100;
        return multiplierPercent / 100.0;
    }
}
