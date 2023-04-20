package me.peopleXrCb.kitbattleaddon;

import me.peopleXrCb.kitbattleaddon.abilities.*;
import me.wazup.kitbattle.Kit;
import me.wazup.kitbattle.KitbattleAPI;
import me.wazup.kitbattle.abilities.AbilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KitBattleAddonPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        AbilityManager.getInstance().registerAbility(new SnailAbility());
        AbilityManager.getInstance().registerAbility(new ScorpionAbility());
        AbilityManager.getInstance().registerAbility(new GlitcherAbility());
        AbilityManager.getInstance().registerAbility(new WrathAbility());
        AbilityManager.getInstance().registerAbility(new AvatarAbility());
        AbilityManager.getInstance().registerAbility(new BerserkAbility());
        AbilityManager.getInstance().registerAbility(new SilvermonnerAbility());
        AbilityManager.getInstance().registerAbility(new MonkkAbility());
        AbilityManager.getInstance().registerAbility(new VampireBAbility());

        AbilityManager.getInstance().loadAbilitiesConfig();
        AbilityManager.getInstance().updateKitAbilities();
    }
}