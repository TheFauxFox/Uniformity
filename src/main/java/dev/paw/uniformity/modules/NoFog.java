package dev.paw.uniformity.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.RenderFogEvent;
import net.minecraft.client.render.FogShape;
import org.dizitart.jbus.Subscribe;

public class NoFog extends KeyboundModule {
    public NoFog() {
        super("NoFog", -1);
    }

    @Subscribe
    public void onGetFog(RenderFogEvent evt) {
        if (isEnabled()) {
            RenderSystem.setShaderFogStart(-8.0F);
            RenderSystem.setShaderFogEnd(1000000.0F);
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
        }
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.noFogToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.noFogToggle = value;
    }
}
