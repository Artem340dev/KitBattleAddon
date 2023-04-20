package me.peopleXrCb.kitbattleaddon.abilities;

import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BerserkAbility extends Ability {
    private PotionEffect increaseEffect;
    private Material activationMaterial;
    private int cooldown;

    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.cooldown = fileConfiguration.getInt("Abilities.Berserk.Cooldown");
        this.activationMaterial = XMaterial.valueOf(fileConfiguration.getString("Abilities.Berserk.ActivationMaterial")).parseMaterial();

        this.increaseEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1);
    }

    @Override
    public Material getActivationMaterial() {
        return activationMaterial;
    }

    @Override
    public EntityType getActivationProjectile() {
        return null;
    }

    @Override
    public boolean isAttackActivated() {
        return false;
    }

    @Override
    public boolean isAttackReceiveActivated() {
        return false;
    }

    @Override
    public boolean isDamageActivated() {
        return false;
    }

    @Override
    public boolean isEntityInteractionActivated() {
        return false;
    }

    @Override
    public boolean execute(Player player, PlayerData playerData, Event event) {
        if (playerData.hasCooldown(player, "Berserk")) {
            return false;
        } else {
            playerData.setCooldown(player, "Berserk", cooldown, true);
            Kitbattle.getInstance().sendUseAbility(player, playerData);

            player.addPotionEffect(increaseEffect);
            player.setMaxHealth(7f);
            player.setHealthScale(7f);

            (new BukkitRunnable() {
                @Override
                public void run() {
                    player.resetMaxHealth();
                    player.setHealthScale(player.getMaxHealth());
                    if (player.hasPotionEffect(increaseEffect.getType())) player.removePotionEffect(increaseEffect.getType());
                }
            }).runTaskLater(Kitbattle.getInstance(), 100L);

            return true;
        }
    }
}