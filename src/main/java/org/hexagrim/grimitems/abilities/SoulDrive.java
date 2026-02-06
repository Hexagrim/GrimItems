package org.hexagrim.grimitems.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.List;

public class SoulDrive extends Item {
    public SoulDrive(Settings settings) {
        super(settings);
    }
    private static final int MAX_HOLD = 100;
    private static final int COOLDOWN= 300; // plich make this 600m ,, MEH i did 300 600 is tuu much
    private int holdTicks = 0;


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user){
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack){
        return UseAction.NONE;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks){
        if(world.isClient()) return;
        if(!(user instanceof ServerPlayerEntity player)) return;

        if(player.getItemCooldownManager().isCoolingDown(this)) return;

        holdTicks++;
        Vec3d look = player.getRotationVec(1).normalize();
        Vec3d pos = player.getPos().add(look.multiply(6.5));

        DustParticleEffect redDust = new DustParticleEffect(new Vector3f(1f,0f,0f), 2f);
        player.getServerWorld().spawnParticles(redDust, pos.x, pos.y + 1, pos.z, 5, 0.5, 0.5, 0.5, 1);

        DustParticleEffect blackDust = new DustParticleEffect(new Vector3f(0f,0f,0f), 2f);
        player.getServerWorld().spawnParticles(blackDust, pos.x, pos.y + 1, pos.z, 10, 0.2, 0.2, 0.2, 0);
        List<Entity> entities = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(10), e -> e != player);
        for(Entity e : entities)
        {
            e.setPosition(pos.x, pos.y, pos.z);
            e.damage(e.getWorld().getDamageSources().magic(), 0.5f);

            if(holdTicks >= MAX_HOLD)
            {
                player.getItemCooldownManager().set(this, COOLDOWN);
                player.stopUsingItem();
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks){
        if(world.isClient()) return;
        if(!(user instanceof ServerPlayerEntity player)) return;
        if(holdTicks > 0 && holdTicks < MAX_HOLD){
            player.getItemCooldownManager().set(this, COOLDOWN);
        }
        holdTicks = 0;
    }
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Drive souls towards a point, Hold Rightclick to use"));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
