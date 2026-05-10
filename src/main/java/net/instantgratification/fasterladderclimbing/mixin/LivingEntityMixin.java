package net.instantgratification.fasterladderclimbing.mixin;
 
import net.instantgratification.fasterladderclimbing.FasterLadderClimbingFabric;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
 
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }
 
    @Shadow public abstract boolean onClimbable();
    @Shadow protected float zza; // Forward movement
 
    /**
     * Optimized "Brute Force" Movement Logic (v26.1.2 Compatible).
     * By extending Entity, we gain access to getXRot() and other base methods 
     * without needing fragile @Shadow definitions that cause crashes.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void fasterladderclimbing$applyLadderBoost(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // 1. Eligibility Check
        if (!this.onClimbable() || this.isSpectator() || entity.isPassenger()) {
            return;
        }
 
        // 2. Movement check: Only boost if the entity is actually trying to move forward
        if (Math.abs(this.zza) < 0.1f) {
            return;
        }
 
        double multiplier = fasterladderclimbing$getMultiplier();
        if (multiplier <= 1.0) return;
 
        // 3. Directional Logic (Gradual Look-based climbing)
        // We can now call getXRot() directly because we extend Entity
        float pitch = this.getXRot();
        double boostY = 0;
        double extraSpeed = 0.2 * (multiplier - 1.0);
 
        // We use a gradual scale: the steeper the angle, the faster the boost.
        // Threshold: 15 degrees. Max intensity at 90 degrees.
        float absPitch = Math.abs(pitch);
        if (absPitch > 15.0f && this.zza > 0) {
            float intensity = (absPitch - 15.0f) / (90.0f - 15.0f);
            intensity = Math.min(intensity, 1.0f); // Cap at 1.0
            
            if (pitch < 0) {
                // Looking Up -> Ascend
                boostY = extraSpeed * intensity;
            } else {
                // Looking Down -> Descend
                boostY = -extraSpeed * intensity;
            }
        }
 
        // 4. Apply the movement
        if (boostY != 0) {
            this.move(MoverType.SELF, new Vec3(0, boostY, 0));
            
            Vec3 currentVelocity = this.getDeltaMovement();
            if (boostY > 0 && currentVelocity.y < 0) {
                this.setDeltaMovement(new Vec3(currentVelocity.x, 0, currentVelocity.z));
            }
        }
    }
 
    @Unique
    private double fasterladderclimbing$getMultiplier() {
        LivingEntity entity = (LivingEntity) (Object) this;
        int multiplierPercent = 100;
        try {
            multiplierPercent = DynamicGameRuleManager.getInt(entity.level(), FasterLadderClimbingFabric.CLIMBING_SPEED_MULTIPLIER);
        } catch (Exception ignored) {}
        
        if (multiplierPercent <= 0) multiplierPercent = 100;
        return multiplierPercent / 100.0;
    }
}
