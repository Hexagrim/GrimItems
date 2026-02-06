package org.hexagrim.grimitems.blocks;


import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SOUL_ORE = RegisterBlock("soulore",new Block(AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.CALCITE)));
    public static final Block SOUL_BLOCK = RegisterBlock("soulblock",new Block(AbstractBlock.Settings.create().strength(5f).requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)));




    public static void registerModBlocks(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries ->{
            entries.add(SOUL_ORE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->{
            entries.add(SOUL_BLOCK);
        });
    }
    public static void RegisterBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of("grimitems",name),new BlockItem(block,new Item.Settings()));
    }
    private static Block RegisterBlock(String name, Block block){
        RegisterBlockItem(name,block);
        return Registry.register(Registries.BLOCK,Identifier.of("grimitems",name),block);
    }
}
