package org.hexagrim.grimitems.abilities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.hexagrim.grimitems.effect.ModEffects;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class Recall extends Item {


    public Recall(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks)
    {
        if (!world.isClient() && user instanceof ServerPlayerEntity player)
        {
            int used = getMaxUseTime(stack, user) - remainingUseTicks;
            DustParticleEffect redDust = new DustParticleEffect(new Vector3f(1f, 0f, 0f), 1f);
            ((ServerWorld) world).spawnParticles(
                    redDust,
                    player.getX(),
                    player.getY() + 1,
                    player.getZ(),
                    20,
                    0.5,
                    1,
                    0.5,
                    0.01
            );
            if (used >= 60)
            {
                world.playSound(
                        null,
                        BlockPos.ofFloored(player.getPos()),
                        SoundEvents.BLOCK_END_GATEWAY_SPAWN,
                        SoundCategory.HOSTILE,
                        1f,
                        1f
                );
                player.getItemCooldownManager().set(this, 100);
                BlockPos pos = player.getSpawnPointPosition();
                RegistryKey<World> dim = player.getSpawnPointDimension();
                ServerWorld spawnWorld;
                if (pos != null && dim != null) {
                    spawnWorld = player.getServer().getWorld(dim);

                    if (!spawnWorld.getBlockState(pos).isIn(BlockTags.BEDS) && !spawnWorld.getBlockState(pos).isOf(Blocks.RESPAWN_ANCHOR)) {
                        pos = spawnWorld.getSpawnPos();
                        spawnWorld = player.getServer().getOverworld();
                    }
                } else {
                    spawnWorld = player.getServer().getOverworld();
                    pos = spawnWorld.getSpawnPos();
                }

                player.teleport(
                        spawnWorld,
                        pos.getX() + 0.5,
                        pos.getY(),
                        pos.getZ() + 0.5,
                        player.getSpawnAngle(),
                        player.getPitch()
                );




                player.stopUsingItem();
            }
        }
    }
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Hold rightclick for a few seconds to return to your respawn point"));
        super.appendTooltip(stack, context, tooltip, type);
    }
}