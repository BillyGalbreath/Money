package net.pl3x.bukkit.pl3xmoney;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public enum Mob {
    BAT,
    BLAZE,
    CAVE_SPIDER,
    CHARGED_CREEPER,
    CHICKEN,
    COW,
    CREEPER,
    DONKEY,
    ELDER_GUARDIAN,
    ENDER_CRYSTAL,
    ENDER_DRAGON,
    ENDERMAN,
    ENDERMITE,
    EVOKER,
    GHAST,
    GIANT,
    GUARDIAN,
    HORSE,
    HUSK,
    IRON_GOLEM,
    LLAMA_SPIT,
    LLAMA,
    MAGMA_CUBE,
    MULE,
    MUSHROOM_COW,
    OCELOT,
    PIG_ZOMBIE,
    PIG,
    POLAR_BEAR,
    RABBIT,
    SHEEP,
    SHULKER,
    SILVERFISH,
    SKELETON_HORSE,
    SKELETON,
    SLIME,
    SNOWMAN,
    SPIDER,
    SQUID,
    STRAY,
    VEX,
    VILLAGER,
    VINDICATOR,
    WITCH,
    WITHER_SKELETON,
    WITHER,
    WOLF,
    ZOMBIE_HORSE,
    ZOMBIE_VILLAGER,
    ZOMBIE;

    public static Mob getMob(LivingEntity entity) {
        EntityType entityType = entity.getType();
        if (entityType == EntityType.CREEPER) {
            return ((Creeper) entity).isPowered() ? CHARGED_CREEPER : CREEPER;
        }

        return getMob(entityType.name());
    }

    public static Mob getMob(String name) {
        try {
            return Mob.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
