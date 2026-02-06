package org.hexagrim.grimitems.abilities;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.hexagrim.grimitems.effect.ModEffects;
import org.hexagrim.grimitems.items.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Scythe extends Item {


    public Scythe(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {


        if (!world.isClient() && user instanceof ServerPlayerEntity) {


            ((ServerPlayerEntity) user).getItemCooldownManager().set(this, 60);

            world.playSound(
                    null,
                    BlockPos.ofFloored(user.getPos()),
                    SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                    SoundCategory.HOSTILE,
                    1f,
                    1f
            );

            Vec3d origin = user.getPos().add(0, user.getEyeY() - user.getY(), 0);


            Vec3d lookVec = user.getRotationVec(1).normalize();


            Vec3d spawnPos = origin.add(lookVec.multiply(4));


            AreaEffectCloudEntity cloud = getAreaEffectCloudEntity(world, user, spawnPos);
            cloud.addEffect(new StatusEffectInstance(
                    ModEffects.REAPERSCURSE, 80, 1, false, false, true
            ));
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT,world);
            lightning.setPos(spawnPos.x,spawnPos.y,spawnPos.z);
            world.spawnEntity(lightning);

            world.spawnEntity(cloud);

        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private static @NotNull AreaEffectCloudEntity getAreaEffectCloudEntity(World world, PlayerEntity user, Vec3d spawnPos) {
        AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(
                world,
                spawnPos.x,
                spawnPos.y,
                spawnPos.z
        );

        cloud.setOwner(user);
        cloud.setRadius(3f);
        cloud.setDuration(10);
        cloud.setWaitTime(0);
        cloud.setParticleType(ModItems.redDust);
        return cloud;
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (target != null && attacker != null) {
            target.addStatusEffect(new StatusEffectInstance(ModEffects.REAPERSCURSE, 100, 0));
            ServerWorld world = (ServerWorld) target.getWorld();
            world.playSound(
                    null,
                    BlockPos.ofFloored(attacker.getPos()),
                    SoundEvents.ITEM_MACE_SMASH_GROUND,
                    SoundCategory.HOSTILE,
                    0.5f,
                    1f
            );
            world.spawnParticles(
                    ModItems.redDust,
                    target.getX(),
                    target.getY() + 1.0,
                    target.getZ(),
                    20,
                    0.2, 0.4, 0.2,
                    0.02
            );
            attacker.addStatusEffect(new StatusEffectInstance(ModEffects.SOULOVERDOSE, 100, 0));
        }

        return super.postHit(stack, target, attacker);
    }
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Scythe of the Reaper"));
        tooltip.add(Text.literal("Rightclick to curse those ahead and steal their soul"));
        tooltip.add(Text.literal("Hit with it to steal the enemy's soul and get high"));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
