package org.hexagrim.grimitems.items;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.hexagrim.grimitems.effect.ModEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MeltedSoul extends Item {
    public static final FoodComponent MELTEDSOUL = new FoodComponent.Builder().nutrition(10).saturationModifier(10f).statusEffect(new StatusEffectInstance(ModEffects.SOULOVERDOSE,50,0),1f).build();

    public MeltedSoul(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            player.sendMessage(Text.literal("You fated gas").formatted(Formatting.RED), false);
            Vec3d look = user.getRotationVec(1).normalize();
            player.setVelocity(new Vec3d(look.x, look.y * 0.3, look.z).multiply(3f));
            player.velocityModified = true;
            world.playSound(
                    null,
                    BlockPos.ofFloored(player.getPos()),
                    SoundEvents.ENTITY_SLIME_SQUISH,
                    SoundCategory.HOSTILE,
                    0.5f,
                    1f
            );
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Gassy Melted Soul, eat to release a fart"));
        super.appendTooltip(stack, context, tooltip, type);
    }
}

