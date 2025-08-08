ysm_native_preloader - Build package
===================================

This archive contains the source for a NeoForge/Forge preloader mod that extracts
the native library (ysm-core.dll / libysm-core.so) from the yes_steve_model mod jar
and loads it at client initialization, so the mod works across different launchers
(PCL, HMCL, Prism, MultiMC, etc.).

WHAT'S IN THIS PACKAGE
----------------------
src/main/java/com/ysm/preloader/NativePreloader.java  - Java source
src/main/resources/META-INF/mods.toml                 - mods metadata
README_BUILD.md                                       - this file

BUILD REQUIREMENTS (one-time)
-----------------------------
- A machine with internet access
- JDK 17 or JDK 21 installed (java & javac on PATH)
- NeoForge/Forge MDK for Minecraft 1.21 (version matching NeoForge 21.1.197)
  You can obtain NeoForge MDK from the source you normally use for NeoForge.
  (If you use a CI or local machine, download the MDK and use its gradle wrapper)

BUILD STEPS (quick)
-------------------
1) Download & unzip NeoForge/Forge MDK (target: 21.1.197) to a directory, e.g.:
   /home/user/neoforge-mdk/

2) Copy the 'src' directory in this package into the MDK project directory,
   merging with the MDK's existing src (i.e. MDK_ROOT/src/main/java/... etc.).
   After copy, the project should have:
     MDK_ROOT/src/main/java/com/ysm/preloader/NativePreloader.java
     MDK_ROOT/src/main/resources/META-INF/mods.toml

3) Open a terminal in the MDK root and run the Gradle build (use the MDK's wrapper):
   On Linux/macOS:
     ./gradlew build
   On Windows (cmd/powershell):
     gradlew.bat build

   If Gradle downloads dependencies, wait for the process to finish. If build is successful,
   the mod jar will be in:
     MDK_ROOT/build/libs/ysm_native_preloader-1.0.0.jar

4) Copy the generated jar to your mods/ folder alongside your yes_steve_model jar:
     <instance>/mods/yes_steve_model.jar
     <instance>/mods/ysm_native_preloader-1.0.0.jar

5) Launch NeoForge 21.1.197 from any launcher (PCL/HMCL/Prism/etc). The preloader will
   attempt to extract and load the native. Check the game log for:
     [ysm-preloader] Loaded native: <absolute path>

TROUBLESHOOTING
---------------
- If the build fails due to missing Forge/Gradle configuration, ensure you used the
  correct MDK and followed its README for initial setup (some MDKs require running
  'gradlew' tasks to generate required files once).
- If System.load fails at runtime, ensure the target platform (Windows vs Linux) has the
  proper native (ysm-core.dll for Windows, libysm-core.so for Linux) inside the yes_steve_model jar.
- If you want to embed the preloader into the main mod jar (single file distribution),
  I can provide instructions to merge classes and resources into the existing mod jar.

SECURITY & LICENSE
------------------
- This preloader only extracts and loads local native binaries from mod jars and does not
  contact external servers.
- Ensure you have the mod author's permission to redistribute if you plan to include the
  native binary directly in your distributed package.

NEXT STEPS
----------
- If you want, I can also prepare a ready-to-run zip that includes a step-by-step shell
  script to download the correct NeoForge MDK automatically (or CI config for GitHub Actions).
- If you prefer, upload the compiled jar back here after building and I will verify it
  against your yes_steve_model jar and run a final check (log parsing guidance).

