package org.hexagrim.grimitems;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.hexagrim.grimitems.items.ModItems;

public class ModItemGroup {
    public static final ItemGroup GRIM_ITEMS = Registry.register(Registries.ITEM_GROUP, Identifier.of("grimitems","grim_items"), FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.SCYTHE)).displayName(Text.literal("Grim's Arsenal and Items"))
            .entries((displayContext, entries) ->
            {
                entries.add(ModItems.MELTEDSOUL);
                entries.add(ModItems.SOULSHARD);
                entries.add(ModItems.SCYTHE);
                entries.add(ModItems.ADVANCEDSCYTHE);
                entries.add(ModItems.RECALL);
                entries.add(ModItems.SOULDRIVE);



            }).build());

    public static void registerGroup(){

    }
}

