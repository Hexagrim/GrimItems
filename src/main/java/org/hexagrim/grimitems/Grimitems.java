package org.hexagrim.grimitems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import org.hexagrim.grimitems.blocks.ModBlocks;
import org.hexagrim.grimitems.effect.ModEffects;
import org.hexagrim.grimitems.items.ModItems;
import org.hexagrim.grimitems.potion.ModPotions;
import org.hexagrim.grimitems.world.ModPlacedFeatures;
import org.hexagrim.grimitems.world.gen.ModWorldGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.server.command.CommandManager.literal;

public class Grimitems implements ModInitializer {

    @Override
    public void onInitialize() {




        ModBlocks.registerModBlocks();
        ModItems.RegisterItem();
        ModEffects.RegisterEffects();
        ModPotions.RegisterPotions();
        ModItemGroup.registerGroup();
        ModWorldGeneration.generateModWorldGen();

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                builder.registerPotionRecipe(Potions.AWKWARD, Items.SOUL_SAND,ModPotions.SOUL_POTION);
        });
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(ModPotions.SOUL_POTION, Items.SPIDER_EYE,ModPotions.REAPER_POTION);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("unlock_grim_recipies")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("CUSTOM RECIPES UNLOCKED"), false);
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    assert player != null;
                    player.unlockRecipes(List.of(
                            Identifier.of("grimitems", "scythe"),
                            Identifier.of("grimitems", "advancedscythe"),
                            Identifier.of("grimitems", "meltedsoul"),
                            Identifier.of("grimitems", "soulblock"),
                            Identifier.of("grimitems", "souldrive"),
                            Identifier.of("grimitems", "souldhard"),
                            Identifier.of("grimitems", "recall")
                    ));
                    return 1;
                })));


    }
    public static void RegisterItemGroups(){

    }

}
