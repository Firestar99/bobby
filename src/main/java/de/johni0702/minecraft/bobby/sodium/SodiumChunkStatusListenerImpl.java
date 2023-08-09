package de.johni0702.minecraft.bobby.sodium;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import static java.lang.invoke.MethodType.methodType;

public class SodiumChunkStatusListenerImpl implements ChunkStatusListener {

    private static final MethodHandle ON_CHUNK_ADDED;
    private static final MethodHandle ON_CHUNK_LIGHT_ADDED;
    private static final MethodHandle ON_CHUNK_REMOVED;

    static {
        MethodHandle ON_CHUNK_ADDED_TMP;
        MethodHandle ON_CHUNK_LIGHT_ADDED_TMP;
        MethodHandle ON_CHUNK_REMOVED_TMP;
        Lookup lookup = MethodHandles.lookup();

        try {
            // sodium 0.4
            //noinspection JavaLangInvokeHandleSignature
            ON_CHUNK_ADDED_TMP = lookup.findVirtual(SodiumWorldRenderer.class, "onChunkAdded", methodType(void.class, int.class, int.class));
            //noinspection JavaLangInvokeHandleSignature
            ON_CHUNK_LIGHT_ADDED_TMP = lookup.findVirtual(SodiumWorldRenderer.class, "onChunkLightAdded", methodType(void.class, int.class, int.class));
            //noinspection JavaLangInvokeHandleSignature
            ON_CHUNK_REMOVED_TMP = lookup.findVirtual(SodiumWorldRenderer.class, "onChunkRemoved", methodType(void.class, int.class, int.class));
        } catch (NoSuchMethodException | IllegalAccessException e04) {
            try {
                // sodium 0.5
                // via Sodium05Compatability

                // note that these 0.5 checks may always succeed, as we provide Sodium05Compatability, not Sodium
                // it will only error when it notices that Sodium methods are missing, which happens either
                // * when the Sodium05Compatability class is loaded or
                // * when it attempts to call into Sodium methods that may not exist
                ON_CHUNK_ADDED_TMP = lookup.findStatic(Sodium05Compatability.class, "onChunkAdded", methodType(void.class, SodiumWorldRenderer.class, int.class, int.class));
                ON_CHUNK_LIGHT_ADDED_TMP = lookup.findStatic(Sodium05Compatability.class, "onChunkLightAdded", methodType(void.class, SodiumWorldRenderer.class, int.class, int.class));
                ON_CHUNK_REMOVED_TMP = lookup.findStatic(Sodium05Compatability.class, "onChunkRemoved", methodType(void.class, SodiumWorldRenderer.class, int.class, int.class));
            } catch (NoSuchMethodException | IllegalAccessException e05) {
                RuntimeException r = new RuntimeException("Sodium integration is present, but neither Sodium 0.4 or 0.5 integration methods could be found!");
                r.addSuppressed(new RuntimeException("Sodium 0.4 Exception", e04));
                r.addSuppressed(new RuntimeException("Sodium 0.5 Exception", e05));
                throw r;
            }
        }

        ON_CHUNK_ADDED = ON_CHUNK_ADDED_TMP;
        ON_CHUNK_LIGHT_ADDED = ON_CHUNK_LIGHT_ADDED_TMP;
        ON_CHUNK_REMOVED = ON_CHUNK_REMOVED_TMP;
    }

    @Override
    public void onChunkAdded(int x, int z) {
        SodiumWorldRenderer sodiumRenderer = SodiumWorldRenderer.instanceNullable();
        if (sodiumRenderer != null) {
            try {
                ON_CHUNK_ADDED.invoke(sodiumRenderer, x, z);
                ON_CHUNK_LIGHT_ADDED.invoke(sodiumRenderer, x, z);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onChunkRemoved(int x, int z) {
        SodiumWorldRenderer sodiumRenderer = SodiumWorldRenderer.instanceNullable();
        if (sodiumRenderer != null) {
            try {
                ON_CHUNK_REMOVED.invoke(sodiumRenderer, x, z);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
