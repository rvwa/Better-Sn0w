package me.skitttyy.kami.impl.features.modules.combat;

import me.skitttyy.kami.api.event.eventbus.SubscribeEvent;
import me.skitttyy.kami.api.event.events.network.PacketEvent;
import me.skitttyy.kami.api.event.events.world.EntityEvent;
import me.skitttyy.kami.api.feature.module.Module;
import me.skitttyy.kami.api.utils.NullUtils;
import me.skitttyy.kami.api.value.Value;
import me.skitttyy.kami.api.value.builder.ValueBuilder;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

public class AutoSuicide extends Module {

    public static AutoSuicide INSTANCE;

    Value<Boolean> deathDisable = new ValueBuilder<Boolean>()
            .withDescriptor("Death Disable")
            .withValue(true)
            .register(this);

    Value<Boolean> loginDisable = new ValueBuilder<Boolean>()
            .withDescriptor("Login Disable")
            .withValue(true)
            .register(this);

    public AutoSuicide() {
        super("AutoSuicide", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        // Disable on death
        if (event.getPacket() instanceof DeathMessageS2CPacket packet) {
            if (!deathDisable.getValue()) return;
            if (mc.player == null) return;
            if (packet.getPlayerId() == mc.player.getId()) {
                this.toggle();
            }
        }

        // Disable on login/respawn
        if (event.getPacket() instanceof GameJoinS2CPacket) {
            if (loginDisable.getValue()) {
                this.toggle();
            }
        }
    }

    @Override
    public String getDescription() {
        return "Makes combat modules target you so you die faster.";
    }
}
