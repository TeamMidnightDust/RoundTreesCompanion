package eu.midnightdust.roundtrees;

import eu.midnightdust.roundtrees.config.RoundTreesConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class RoundTreesClient implements ClientModInitializer {
    public static RoundTreesConfig RT_CONFIG;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(RoundTreesConfig.class, JanksonConfigSerializer::new);
        RT_CONFIG = AutoConfig.getConfigHolder(RoundTreesConfig.class).getConfig();
    }
}
