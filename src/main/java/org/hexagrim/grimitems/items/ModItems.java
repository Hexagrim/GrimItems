package org.hexagrim.grimitems.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.hexagrim.grimitems.ModItemGroup;
import org.hexagrim.grimitems.abilities.AdvancedScythe;
import org.hexagrim.grimitems.abilities.Recall;
import org.hexagrim.grimitems.abilities.Scythe;
import org.hexagrim.grimitems.abilities.SoulDrive;
import org.joml.Vector3f;

public class ModItems {
    public static final DustParticleEffect redDust = new DustParticleEffect(new Vector3f(1f, 0f, 0f), 1f);
    public static final Item SCYTHE = registerItem("scythe",new Scythe(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE,6,-2.4f)).maxCount(1)));
    public static final Item SOULSHARD = registerItem("soulshard",new Item(new Item.Settings()));
    public static final Item ADVANCEDSCYTHE = registerItem("advancedscythe",new AdvancedScythe(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE,9,-2.4f)).maxCount(1)));
    public static final Item MELTEDSOUL = registerItem("meltedsoul",new MeltedSoul(new Item.Settings().food(MeltedSoul.MELTEDSOUL)));
    public static final Item RECALL = registerItem("recall",new Recall(new Item.Settings().maxCount(1)));
    public static final Item SOULDRIVE = registerItem("souldrive",new SoulDrive(new Item.Settings().maxCount(1)));

    public static Item registerItem(String name,Item item){
        return Registry.register(Registries.ITEM, Identifier.of("grimitems",name),item);
    }
    public static void RegisterItem(){


    }
}
