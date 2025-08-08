package com.ysm.preloader;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.*;
import java.nio.file.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * NeoForge/Forge Client preloader:
 * - On client setup, search mods/ for META-INF/native/ysm-core.dll or libysm-core.so
 * - Extract to <gameDir>/natives/ and System.load(...) it directly.
 */
@Mod("ysm_native_preloader")
public class NativePreloader {

    public NativePreloader() {
        // empty
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        try {
            Path gameDir = Paths.get(System.getProperty("user.dir")); // Forge usually runs with user.dir = game dir
            Path nativesDir = gameDir.resolve("natives");
            Files.createDirectories(nativesDir);

            Path modsDir = gameDir.resolve("mods");
            if (!Files.exists(modsDir) || !Files.isDirectory(modsDir)) {
                // fallback: try "mods" anyway
            }

            if (Files.exists(modsDir) && Files.isDirectory(modsDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir, "*.jar")) {
                    for (Path jarPath : stream) {
                        try (JarFile jf = new JarFile(jarPath.toFile())) {
                            JarEntry dll = jf.getJarEntry("META-INF/native/ysm-core.dll");
                            JarEntry so  = jf.getJarEntry("META-INF/native/libysm-core.so");
                            if (dll != null) {
                                extractAndLoad(jf, dll, nativesDir.resolve("ysm-core.dll"));
                                return;
                            } else if (so != null) {
                                extractAndLoad(jf, so, nativesDir.resolve("libysm-core.so"));
                                return;
                            }
                        } catch (IOException ioe) {
                            // ignore this jar and continue
                        }
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void extractAndLoad(JarFile jf, JarEntry entry, Path target) {
        try (InputStream is = jf.getInputStream(entry)) {
            Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
            System.load(target.toAbsolutePath().toString());
            System.out.println("[ysm-preloader] Loaded native: " + target.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
