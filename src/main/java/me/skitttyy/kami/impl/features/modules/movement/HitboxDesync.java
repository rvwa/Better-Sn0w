package me.skitttyy.kami.impl.features.modules.movement;

import me.skitttyy.kami.api.event.eventbus.SubscribeEvent;
import me.skitttyy.kami.api.event.events.TickEvent;
import me.skitttyy.kami.api.feature.module.Module;
import me.skitttyy.kami.api.utils.NullUtils;
import me.skitttyy.kami.api.utils.Timer;
import me.skitttyy.kami.api.value.Value;
import me.skitttyy.kami.api.value.builder.ValueBuilder;
import net.minecraft.util.math.Vec3d;

public class HitboxDesync extends Module {

    public static HitboxDesync INSTANCE;

    Value<Boolean> alternating = new ValueBuilder<Boolean>()
            .withDescriptor("Alternating")
            .withValue(false)
            .register(this);

    Value<Boolean> minimal = new ValueBuilder<Boolean>()
            .withDescriptor("Minimal")
            .withValue(false)
            .register(this);

    Value<Boolean> specific = new ValueBuilder<Boolean>()
            .withDescriptor("Specific")
            .withValue(false)
            .register(this);

    Value<Boolean> selfDisable = new ValueBuilder<Boolean>()
            .withDescriptor("Self Disable")
            .withValue(true)
            .register(this);

    Value<Boolean> jumpDisable = new ValueBuilder<Boolean>()
            .withDescriptor("Jump Disable")
            .withValue(true)
            .register(this);

    private final Timer timer = new Timer();
    private double prevY;

    public HitboxDesync() {
        super("HitboxDesync", Category.Movement);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent.Pre event) {
        if (NullUtils.nullCheck()) {
            this.toggle();
            return;
        }

        if (jumpDisable.getValue() && mc.player.getY() != prevY) {
            this.toggle();
            return;
        }

        Vec3d vec3d = mc.player.getBlockPos().toCenterPos();

        double offset = minimal.getValue() ? 0.001 : 0.002;
        double timeout = specific.getValue() ? 500 : 1500;

        boolean flag = timer.isPassed() && alternating.getValue() && !mc.player.isSneaking();
        boolean flagX = (vec3d.x - mc.player.getX()) > 0;
        boolean flagZ = (vec3d.z - mc.player.getZ()) > 0;

        double x = vec3d.x + ((flag ? offset : 0) * (flagX ? 1 : -1)) + 0.20000000009497754 * (flagX ? -1 : 1);
        double z = vec3d.z + ((flag ? offset : 0) * (flagZ ? 1 : -1)) + 0.2000000000949811 * (flagZ ? -1 : 1);

        mc.player.setPosition(x, mc.player.getY(), z);

        if (timer.isPassed()) {
            timer.resetDelay();
        }

        if (selfDisable.getValue() && !alternating.getValue()) {
            this.toggle();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            this.toggle();
            return;
        }
        prevY = mc.player.getY();
        timer.setDelay(1500);
        timer.resetDelay();
    }

    @Override
    public String getDescription() {
        return "Precisely offsets your position to glitch out Minecraft hitbox calculations.";
    }
}
