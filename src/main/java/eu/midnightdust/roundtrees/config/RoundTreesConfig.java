package eu.midnightdust.roundtrees.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "roundtrees")
public class RoundTreesConfig implements ConfigData {
    @Comment(value = "Enable round log bounding box")
    public boolean round_box = true;
    @Comment(value = "Expand signs placed on log blocks for better visibility")
    public boolean sign_fix = true;
}
