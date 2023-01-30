package me.peopleXrCb.kitbattleaddon.abilities;

import me.wazup.kitbattle.Kitbattle;
import me.wazup.kitbattle.PlayerData;
import me.wazup.kitbattle.abilities.Ability;
import me.wazup.kitbattle.utils.Utils;
import me.wazup.kitbattle.utils.XMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SnailAbility extends Ability {
    private int slowChance;
    private PotionEffect slowEffect;


    @Override
    public String getName() {
        return "Snail";
    }

    @Override
    public void load(FileConfiguration fileConfiguration) {
        this.slowChance = Integer.valueOf(fileConfiguration.getString("Abilities.Snail.Slow-Chance").replace("%", ""));
        this.slowEffect = new PotionEffect(PotionEffectType.SLOW, fileConfiguration.getInt("Abilities.Snail.Slow-Lasts-For") * 20, fileConfiguration.getInt("Abilities.Snail.Slow-Level") - 1);
    }

    @Override
    public Material getActivationMaterial() {
        return null;
    }

    @Override
    public EntityType getActivationProjectile() {
        return null;
    }

    @Override
    public boolean isAttackActivated() {
        return true;
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
        int random = Utils.random.nextInt(100) + 1;

        if (random <= this.slowChance) {
            Player damagedPlayer = (Player)((EntityDamageByEntityEvent) event).getEntity();
            damagedPlayer.removePotionEffect(PotionEffectType.SLOW);
            damagedPlayer.addPotionEffect(this.slowEffect);
            Kitbattle.getInstance().sendUseAbility(player, playerData);
            return true;
        } else {
            return false;
        }
    }
}