package me.skitttyy.kami.impl.features.modules.combat;

import me.skitttyy.kami.api.event.eventbus.SubscribeEvent;
import me.skitttyy.kami.api.event.events.TickEvent;
import me.skitttyy.kami.api.event.events.network.PacketEvent;
import me.skitttyy.kami.api.feature.module.Module;
import me.skitttyy.kami.api.utils.NullUtils;
import me.skitttyy.kami.api.value.Value;
import me.skitttyy.kami.api.value.builder.ValueBuilder;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;

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
    public void onTick(TickEvent.PlayerTickEvent.Pre event) {
        if (NullUtils.nullCheck()) return;
        if (!deathDisable.getValue()) return;

        if (mc.player.getHealth() <= 0) {
            this.toggle();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (!loginDisable.getValue()) return;

        if (event.getPacket() instanceof GameJoinS2CPacket
                || event.getPacket() instanceof PlayerRespawnS2CPacket) {
            this.toggle();
        }
    }

    @Override
    public String getDescription() {
        return "Makes combat modules target you so you die faster.";
    }
}
