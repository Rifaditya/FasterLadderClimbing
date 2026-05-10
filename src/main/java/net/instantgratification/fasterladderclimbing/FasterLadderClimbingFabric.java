package net.instantgratification.fasterladderclimbing;
 
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.resources.Identifier;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class FasterLadderClimbingFabric implements ModInitializer {
    public static final String MOD_ID = "fasterladderclimbing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
 
    public static final GameRuleCategory CATEGORY = DynamicGameRuleManager.registerCategory(
            Identifier.fromNamespaceAndPath(MOD_ID, "climbing")
    );
 
    public static GameRule<Integer> CLIMBING_SPEED_MULTIPLIER;
 
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Faster Ladder Climbing - Protocol 2.1 Standard");
 
        // Dependency Enforcement Law (Constitution 5.7)
        if (!FabricLoader.getInstance().isModLoaded("dasik-library")) {
            throw new RuntimeException("Faster Ladder Climbing requires DasikLibrary to function. Please install it.");
        }
 
        // Register GameRule via DasikLibrary
        CLIMBING_SPEED_MULTIPLIER = DynamicGameRuleManager.integerRule(MOD_ID + ":climbing_speed_multiplier", CATEGORY, 100)
                .range(0, 500)
                .name("Climbing Speed Multiplier")
                .description("Scales the movement speed of entities on ladders and vines. 100 is Vanilla, 500 is 5x speed.")
                .register();
    }
}
