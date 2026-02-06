package org.hexagrim.grimitems.potion;

import com.ibm.icu.text.Normalizer2;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemGroups;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.hexagrim.grimitems.effect.ModEffects;

public class ModPotions {
    public  static  void RegisterPotions(){


    }
    public static final RegistryEntry<Potion> SOUL_POTION = registerPotion("soulpotion", new Potion(new StatusEffectInstance(ModEffects.SOULOVERDOSE,1200,0)));
    public static final RegistryEntry<Potion> REAPER_POTION = registerPotion("reaperpotion", new Potion(new StatusEffectInstance(ModEffects.REAPERSCURSE,1200,0)));

    private static RegistryEntry<Potion> registerPotion(String name,Potion potion){
        return Registry.registerReference(Registries.POTION, Identifier.of("grimitems",name),potion);
    }
}
