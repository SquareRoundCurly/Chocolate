# Chocolate

A vanilla+ style Minecraft mod.
It's intended to add "what should have been" in Minecraft.

## Development setup

This is a Java mod for **Minecraft Java Edition 26.3**, using **Fabric**.
The mod currently adds the **Actor**, a directional block with dispenser textures.
Its tool-use behavior will be added later.

## Actor

Find **Actor** in the **Redstone Blocks** creative tab, search for it in the Creative
inventory, or use `/give @s chocolate:actor` with commands enabled.

- Places facing toward the player, including upward and downward, like a dispenser.
- Uses vanilla dispenser models and textures for both the block and its item.
- Has dispenser-like hardness and drops itself when mined with a pickaxe.
- Currently has no inventory, interface, redstone behavior, or crafting recipe.

The planned functionality is to use stored tools to mine blocks, chop wood, and
plant seeds. This first version establishes the block and its facing direction.

To test it, restart the development client, give yourself an Actor, and place it
on walls, floors, and ceilings to check its orientation and textures. Switch to
Survival and break it with a pickaxe to check its item drop. Right-clicking or
powering it should not perform an action yet.

### What the tools do

- **JDK 25** compiles and runs the Java code. Install a JDK, not just a JRE.
- **Fabric Loader** loads the mod into Minecraft.
- **Fabric API** provides hooks for adding game features.
- **Gradle** builds the project and downloads dependencies. The included wrapper
  (`gradlew` / `gradlew.bat`) downloads the correct Gradle version automatically.
- **Fabric Loom** connects Gradle to Minecraft and provides development runs.

You do not need to install Gradle or Fabric separately to run the development game.

### 1. Set up Java

Install [Eclipse Temurin JDK 25](https://adoptium.net/temurin/releases/?version=25),
set `JAVA_HOME` to its installation directory, and add its `bin` directory to `PATH`.
Check your terminal with:

```sh
java -version
javac -version
```

Both should report version 25.

For the Linux machine used to initialize this repository, a JDK is already installed
at `$HOME/.local/share/chocolate-dev/jdk-25`. Enable it in a terminal with:

```sh
export JAVA_HOME="$HOME/.local/share/chocolate-dev/jdk-25"
export PATH="$JAVA_HOME/bin:$PATH"
```

These exports apply to the current terminal. Add them to your shell configuration
(for example `~/.bashrc`) if you want them in future terminals too.
Other contributors should use their own JDK installation path.

### 2. Open the repository in your editor

Open the whole `Chocolate` folder, not just an individual Java file.

- **VS Code:** install the recommended Java Extension Pack and Gradle for Java
  extensions, and allow the Gradle project to import. Use **Java: Configure Java
  Runtime** to select JDK 25 if it is not detected. The Gradle JVM must also use 25.
- **IntelliJ IDEA:** open the folder as a Gradle project, select JDK 25 for both the
  Project SDK and Gradle JVM, and let Gradle finish syncing.

The first import/build downloads Minecraft and dependencies, so it needs internet
access and can take several minutes.

### 3. Build and run

Run these commands from the repository root:

```sh
./gradlew build
./gradlew runClient
```

On Windows, use `.\gradlew.bat build` and `.\gradlew.bat runClient` instead.

`runClient` starts a separate development Minecraft instance with Chocolate and
Fabric API loaded. Worlds, settings, and logs live in the ignored `run/` folder.
Look for `Chocolate initialized` in the console or `run/logs/latest.log` to confirm
that the mod loaded. Search the Creative inventory for Actor to try the first block.

Close Minecraft before rebuilding and relaunching to see code changes.

### 4. Launch with the VS Code debugger

Fabric Loom generates VS Code launch configurations for the client and dedicated
server. This workspace already has them generated locally.

1. Install the extensions recommended by this workspace (Extensions view, search
   `@recommended`). The Java Extension Pack includes the Java debugger.
2. Wait for the Java/Gradle project import to finish. If you just installed the
   extensions, run **Developer: Reload Window** from the Command Palette.
3. Open `src/main/java/dev/chocolate/Chocolate.java` and click the gutter beside
   `LOGGER.info("Chocolate initialized");` to set a breakpoint.
4. Open **Run and Debug** (`Ctrl+Shift+D`), select **Minecraft Client**, and press
   **F5**. Minecraft launches with Chocolate and Fabric API loaded, with the
   debugger connected from the start. No copying JARs into a mods folder is needed.
5. When the breakpoint pauses startup, inspect variables or step through the code.
   Press **F5** again to continue into the game. Create a Creative world for testing
   gameplay as we add features.

**Ctrl+F5** launches the selected configuration without debugging. Singleplayer
also runs an integrated server in the same Java process, so ordinary gameplay and
client breakpoints can both be tested with the client launch. Use **Minecraft
Server** separately when checking dedicated-server compatibility; its first run
requires the EULA step described below.

To skip the title screen and open an existing world, select **Minecraft Client
(Quick Play)** and press **F5**. Enter the world's **folder name under `run/saves/`**
when prompted (the default is `New World`). This uses Minecraft's
`--quickPlaySingleplayer` argument; the debugger still connects during startup.
Create the world normally first: Quick Play opens an existing save rather than
creating one. VS Code remembers the selected launch configuration for future runs.
Choose **Minecraft Client** to return to the normal title-screen launch.

The local `.vscode/settings.json` points VS Code to the installed JDK 25. On another
machine, configure `java.jdt.ls.java.home`, `java.import.gradle.java.home`, and the
`JavaSE-25` entry in `java.configuration.runtimes` with that machine's JDK path.
These local settings are ignored by Git.

For a fresh checkout, after moving this folder, or after changing dependency
versions, regenerate the launch configurations using **Terminal > Run Task >
Minecraft: prepare VS Code**, or run this in a terminal with Java configured:

```sh
./gradlew classes clientClasses vscode
```

The task prepares classes, launch files, and game assets. `.vscode/launch.json` is
generated and ignored because Loom includes absolute local paths. Regenerate it
instead of editing it manually. The shared tasks use the JDK path configured in
`java.import.gradle.java.home`. **Ctrl+Shift+B** runs the shared build task.

VS Code normally builds Java changes before debugging. Restart Minecraft for
reliable testing of registration, resource, or structural changes. Java hot code
replacement can handle some method-body edits but does not rerun initialization.
To browse Minecraft's own source code, run `./gradlew genSources` and refresh the
Gradle project.

The build removes Loom's optional `projectName` restriction from generated launch
configurations. VS Code's Gradle build server may import the folder as `Chocolate`
while Gradle calls it `chocolate`; a fixed name would cause a "not a valid Java
project" error. If you have an older launch file, rerun the prepare task above.
If the debugger still cannot resolve the main class, finish the Gradle import
first. If import remains stuck, check the JDK settings and run **Java: Clean Java
Language Server Workspace**, then let the project reload.

See the official [Fabric VS Code launch guide](https://docs.fabricmc.net/develop/getting-started/vscode/launching-the-game)
and [VS Code Java debugging guide](https://code.visualstudio.com/docs/java/java-debugging).

| Command | Purpose |
| --- | --- |
| `./gradlew build` | Compile, run available checks, and package the mod |
| `./gradlew runClient` | Launch Minecraft with the mod |
| `./gradlew runServer` | Launch a development dedicated server |
| `./gradlew genSources` | Prepare Minecraft sources for browsing in the IDE |
| `./gradlew clean` | Remove build output, keeping development worlds |

A dedicated server requires accepting Minecraft's EULA in the `eula.txt` file it
creates under its run directory. Read the linked EULA before changing that file.

The distributable mod is `build/libs/chocolate-0.1.0-SNAPSHOT.jar`.
The `-sources.jar` contains source code and is not the playable mod.
To use the mod outside development, install Fabric Loader for **26.3** in your
Minecraft launcher, then put the mod JAR and the matching Fabric API JAR into that
installation's `mods/` folder.

## Where things belong

```text
src/main/java/dev/chocolate/Chocolate.java         Common initialization
src/client/java/dev/chocolate/ChocolateClient.java Client-only initialization
src/main/resources/fabric.mod.json                Mod identity and dependencies
build.gradle                                     Build configuration
gradle.properties                                Dependency and mod versions
```

Put shared gameplay code in `src/main/java`. Keep rendering and other client-only
code in `src/client/java` so a dedicated server can load the mod safely.
Future textures, models, and translations belong under
`src/main/resources/assets/chocolate/`; recipes, loot tables, and other game data
belong under `src/main/resources/data/chocolate/`.

The mod ID is `chocolate` and the Java package is `dev.chocolate`.
Dependency versions are pinned in `gradle.properties`; upgrading Minecraft also
requires checking Fabric compatibility and any changed game APIs.
Minecraft 26.1 and later use unobfuscated names, so this project needs no Yarn
mapping dependency. Older tutorials may show different Gradle configuration.

GitHub Actions builds the mod on pushes and pull requests and saves the resulting
JARs as a workflow artifact. Generated files, caches, and development worlds are
excluded from Git. No project license has been chosen yet.

## Useful references

- [Fabric development documentation](https://docs.fabricmc.net/develop/)
- [Fabric development environment setup](https://docs.fabricmc.net/develop/getting-started/setting-up)
- [Fabric dependency versions](https://fabricmc.net/develop/)
- [Official Fabric example mod](https://github.com/FabricMC/fabric-example-mod)

Choose documentation for the Minecraft version you are targeting where available;
guides for earlier versions may need adjustments.
