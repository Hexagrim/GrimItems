package org.hexagrim.grimitems.abilities;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.hexagrim.grimitems.effect.ModEffects;
import org.hexagrim.grimitems.items.ModItems;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

public class AdvancedScythe extends Item {


    public AdvancedScythe(Settings settings) {
        super(settings);
    }
    private static boolean fly = false;
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected){

        if(!world.isClient()){
        if(!(entity instanceof ServerPlayerEntity player)) return;



        if(fly) {
            player.getServerWorld().spawnParticles(ModItems.redDust, player.getX(), player.getY() + 0.1, player.getZ(), 10, 0.3, 0.1, 0.3, 0.01);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,100,1));
        }
        }
    }




    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {


        if (!world.isClient() && user instanceof ServerPlayerEntity ) {
            if(!user.isSneaking()) {
                Attack(user, world);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 10, false, false, false));
                user.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 10f, 1f);
            }
            else{
                user.sendMessage(Text.literal("You can fly, wow").formatted(Formatting.RED), false);
                world.playSound(
                        null,
                        BlockPos.ofFloored(user.getPos()),
                        SoundEvents.BLOCK_BEACON_ACTIVATE,
                        SoundCategory.HOSTILE,
                        1f,
                        1f
                );
                flightBoost(user,world);
            }
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
                    30,
                    0.2, 0.4, 0.2,
                    0.02
            );
            attacker.addStatusEffect(new StatusEffectInstance(ModEffects.SOULOVERDOSE, 100, 0));
        }

        return super.postHit(stack, target, attacker);
    }
    private void Attack(PlayerEntity user,World world){


        ((ServerPlayerEntity) user).getItemCooldownManager().set(this, 45);

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
        Vec3d spawnPos = origin.add(lookVec.multiply(8));


        AreaEffectCloudEntity cloud = getAreaEffectCloudEntity(world, user, spawnPos);
        cloud.addEffect(new StatusEffectInstance(
                ModEffects.REAPERSCURSE, 80, 2, false, false, true
        ));
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT,world);
        world.createExplosion(
                null,
                spawnPos.x,spawnPos.y,spawnPos.z,
                3.0F,
                World.ExplosionSourceType.MOB
        );
        lightning.setPos(spawnPos.x,spawnPos.y,spawnPos.z);
        world.spawnEntity(lightning);

        world.spawnEntity(cloud);

    }

    public void flightBoost(PlayerEntity player,World world){
        if(!(player instanceof ServerPlayerEntity p)) return;
        p.getAbilities().allowFlying=true;
        p.getAbilities().flying=true;
        fly = true;
        ((ServerPlayerEntity) player).getItemCooldownManager().set(this, 100000);
        p.sendAbilitiesUpdate();
        DustParticleEffect d=new DustParticleEffect(new Vector3f(1f,0f,0f),1f);
        p.getServerWorld().spawnParticles(d,p.getX(),p.getY(),p.getZ(),30,0.5,0.1,0.5,0.01);
        new Thread(()->{
            try{Thread.sleep(5000);}catch(Exception e){}
            if(((ServerPlayerEntity) player).interactionManager.getGameMode() == GameMode.SURVIVAL) {
                p.getAbilities().allowFlying = false;
                p.getAbilities().flying = false;
            }
            ((ServerPlayerEntity) player).getItemCooldownManager().set(this, 100);
            world.playSound(
                    null,
                    BlockPos.ofFloored(player.getPos()),
                    SoundEvents.BLOCK_BEACON_DEACTIVATE,
                    SoundCategory.HOSTILE,
                    1f,
                    1f
            );

            fly = false;
            p.sendAbilitiesUpdate();
        }).start();
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("The Most Powerful Scythe of the Reaper"));
        tooltip.add(Text.literal("Rightclick to curse those ahead and steal their soul with an explosion"));
        tooltip.add(Text.literal("Shift Rightclick to fly, cause you can"));
        tooltip.add(Text.literal("Hit with it to steal the enemy's soul and get high"));
        super.appendTooltip(stack, context, tooltip, type);
    }

}
