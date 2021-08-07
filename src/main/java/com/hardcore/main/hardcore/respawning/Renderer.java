package com.hardcore.main.hardcore.respawning;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class Renderer extends MapRenderer {
    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        while (canvas.getCursors().size() > 0) {
            canvas.getCursors().removeCursor(canvas.getCursors().getCursor(0));
        }

        canvas.getCursors().addCursor(0, 0, (byte) 0).setType(MapCursor.Type.RED_X);
    }
}
