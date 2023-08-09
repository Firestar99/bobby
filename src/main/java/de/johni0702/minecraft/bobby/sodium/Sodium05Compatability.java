package de.johni0702.minecraft.bobby.sodium;

import de.johni0702.minecraft.bobby.mixin.sodium_05.SodiumWorldRendererAccessor;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkStatus;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;

public class Sodium05Compatability {

    public static void onChunkAdded(SodiumWorldRenderer sodiumRenderer, int x, int z) {
        if (sodiumRenderer instanceof SodiumWorldRendererAccessor accessor) {
            accessor.getRenderSectionManager().onChunkAdded(x, z);
        }
    }

    public static void onChunkLightAdded(SodiumWorldRenderer sodiumRenderer, int x, int z) {
        if (sodiumRenderer instanceof SodiumWorldRendererAccessor accessor) {
            ChunkTrackerHolder.get(accessor.getWorld()).onChunkStatusAdded(x, z, ChunkStatus.FLAG_ALL);
        }
    }

    public static void onChunkRemoved(SodiumWorldRenderer sodiumRenderer, int x, int z) {
        if (sodiumRenderer instanceof SodiumWorldRendererAccessor accessor) {
            accessor.getRenderSectionManager().onChunkRemoved(x, z);
        }
    }
}
