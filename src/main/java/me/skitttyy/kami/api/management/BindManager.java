package me.skitttyy.kami.api.management;

import me.skitttyy.kami.api.binds.IBindable;
import me.skitttyy.kami.api.event.eventbus.SubscribeEvent;
import me.skitttyy.kami.api.event.events.key.KeyboardEvent;
import me.skitttyy.kami.api.event.events.key.MouseEvent;
import me.skitttyy.kami.api.wrapper.IMinecraft;
import me.skitttyy.kami.impl.KamiMod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BindManager implements IMinecraft {
    public static BindManager INSTANCE;
    List<IBindable> bindables;

    public BindManager() {
        bindables = new ArrayList<>();
        KamiMod.EVENT_BUS.register(this);
    }

    public List<IBindable> getBindables() { return bindables; }
    public void setBindables(List<IBindable> bindables) { this.bindables = bindables; }

    @SubscribeEvent
    public void onKeyboard(KeyboardEvent event) {
        if (mc.currentScreen != null) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;

        for (IBindable bindable : getBindables()) {
            if (!bindable.isMouse() && bindable.getKey() == event.getKey()) {
                bindable.onKey();
            }
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (!event.getType().equals(MouseEvent.Type.CLICK)) return;
        if (mc.currentScreen != null) return;

        for (IBindable bindable : getBindables()) {
            if (bindable.isMouse() && bindable.getKey() == event.getButton()) {
                bindable.onKey();
            }
        }
    }
}
