package org.hexagrim.grimitems.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;


public class ModEffects{
    public static final RegistryEntry<StatusEffect> REAPERSCURSE = registerStatusEffect("reaperscurse",
            new ReapersCurse(StatusEffectCategory.HARMFUL,0x210000)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,Identifier.of("grimitems","reaperscurse"),-0.3f
                , EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final RegistryEntry<StatusEffect> SOULOVERDOSE = registerStatusEffect("souloverdose",
            new SoulOverdose(StatusEffectCategory.BENEFICIAL,0xff6c48)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,Identifier.of("grimitems","souloverdose"),0.3f
                            , EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect){
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("grimitems",name),statusEffect);
    }
    public  static  void RegisterEffects(){

    }
}
