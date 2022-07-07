package com.panjohnny.game;

import com.google.gson.JsonObject;
import com.panjohnny.game.data.DataSet;
import com.panjohnny.game.data.FlagChecker;
import com.panjohnny.game.data.GameDataManager;
import com.panjohnny.game.data.Translator;
import com.panjohnny.game.event.Event;
import com.panjohnny.game.event.EventHandler;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.io.KeyboardEvent;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.io.SoundPlayer;
import com.panjohnny.game.mem.DataFetcher;
import com.panjohnny.game.mem.ImageFetcher;
import com.panjohnny.game.render.FontRenderer;
import com.panjohnny.game.render.Renderer;
import com.panjohnny.game.render.Window;
import com.panjohnny.game.scenes.MainMenu;
import com.panjohnny.game.scenes.OptionScene;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.scenes.TestScene;
import com.panjohnny.game.scenes.designer.LevelDesigner;
import lombok.Getter;

import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class GloomGame {
    private static GloomGame instance;

    private final Thread gameThread;
    private boolean running;

    @Getter
    private Window window;

    @Getter
    private final Renderer renderer;

    @Getter
    private int sceneIndex;

    @Getter
    private final GameDataManager dataManager;

    private final ArrayList<Scene> scenes;
    @Getter
    private Scene currentScene;
    private boolean currentSceneLoaded = true;

    @Getter
    private final ImageFetcher imageFetcher;
    @Getter
    private final DataFetcher dataFetcher;

    @Getter
    private final EventHandler eventHandler;

    @Getter
    private Player player;

    public GloomGame() {
        this.dataManager = new GameDataManager();
        imageFetcher = new ImageFetcher();
        dataFetcher = new DataFetcher();
        eventHandler = new EventHandler();
        scenes = new ArrayList<>();
        gameThread = new Thread(() -> {
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                e.printStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append("---------- UNCAUGHT EXCEPTION ----------\n");
                sb.append("// This information is important for developers.\n");
                sb.append("Version: ").append(getClass().getPackage().getImplementationVersion()).append("\n");
                sb.append("Thread: ").append(t.getName()).append("\n");
                sb.append("Message: ").append(e.getMessage()).append("\n");
                sb.append("Cause: ").append(e.getCause()).append("\n");
                sb.append("Stacktrace:\n");
                for (StackTraceElement ste : e.getStackTrace()) {
                    sb.append(ste.toString()).append("\n");
                }
                sb.append("----\n\n");

                // print system properties
                sb.append("---------- SYSTEM PROPERTIES ----------\n");
                Properties props = System.getProperties();
                // print os, java version, and os version
                sb.append("OS: ").append(System.getProperty("os.name")).append("\n");
                sb.append("Java version: ").append(System.getProperty("java.version")).append("\n");
                sb.append("OS version: ").append(System.getProperty("os.version")).append("\n");
                sb.append("----\n\n");
                // print flags that program was started with
                sb.append("---------- ARGUMENTS ----------\n");
                for (String arg : FlagChecker.flags) {
                    sb.append(arg).append("\n");
                }
                sb.append("----\n\n");
                sb.append("---------- RAW ----------\n");
                sb.append(e).append("\n");
                for (StackTraceElement ste : e.getStackTrace()) {
                    sb.append(ste.toString()).append("\n");
                }
                // append something wholesome to the end of the file
                sb.append("Error happens :sadge:");

                dataManager.writeFile("error.log", sb.toString());
                System.err.println("Uncaught exception in thread " + t.getName());
                System.err.println("Error log written to /data/error.log");
                e.printStackTrace();
                System.exit(1);
            });
            running = true;
            long lastTime = System.nanoTime();
            double nsPerFrame = 1000000000D / Options.MAX_FPS;
            int frames = 0;
            int updates = 0;
            long lastTimer = System.currentTimeMillis();
            double delta = 0;
            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / nsPerFrame;
                lastTime = now;
                boolean shouldRender = false;
                while (delta >= 1) {
                    update();
                    updates++;
                    delta--;
                    shouldRender = true;
                }
                if (shouldRender) {
                    frames++;
                    render();
                }
                if (System.currentTimeMillis() - lastTimer >= 1000) {
                    lastTimer += 1000;
                    if (Options.DEVELOPER_MODE) {
                        System.out.printf("FPS: %d, UPS: %d%n", frames, updates);
                        if (window != null) {
                            window.debug(String.format("FPS: %d, UPS: %d", frames, updates));
                        }
                    }
                    frames = 0;
                    updates = 0;
                }
            }
        }, "Main Thread");

        if(!Options.UNIT_TESTING_MODE)
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        renderer = Renderer.getInstance();
        window = new Window();

        Mouse.init(this);

        renderer.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                getEventHandler().fire(new KeyboardEvent(getClass(), e));
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                getEventHandler().fire(new KeyboardEvent(getClass(), e));
            }
        });

        if (!Options.LEVEL_DESIGNER) {
            scenes.add(new MainMenu());
            scenes.add(new OptionScene());
            scenes.add(new TestScene());
        } else {
            scenes.add(new LevelDesigner());
        }

        setScene(0);

        // prevent me from dying on the spot when testing
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SoundPlayer.playSound("/assets/music/theme_song.wav", 1);
        });

    }

    public static void main(String[] args) {
        FlagChecker.check(args);
        instance = new GloomGame();
        if(Options.UNIT_TESTING_MODE)
            return;
        instance.load();
        instance.start();
    }

    public synchronized void start() {
        running = true;
        player = new Player(100, 100);
        getEventHandler().register(player);
        window.show();
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!currentSceneLoaded) {
            currentScene = scenes.get(sceneIndex).init();
            currentSceneLoaded = true;
        }
        currentScene.update();
    }

    public void render() {
        currentScene.render();
    }

    public void setScene(int scene) {
        // check if it is range or close the game
        if (scene >= scenes.size() || scene < 0) {
            System.err.println("Scene out of range");
            stop();
        }
        sceneIndex = scene;
        currentSceneLoaded = false;

        System.gc();
    }

    public void save() {
        JsonObject json = new JsonObject();
        json.add("window", window.toJson());
        json.add("language", Translator.toJson());
        json.add("settings", Options.toJson());
        dataManager.saveFile(new DataSet(json), "/latest_session.json");
    }

    public void load() {
        FontRenderer.load();
        DataSet data = dataManager.loadFile("/latest_session.json");
        if (!data.isEmpty()) {
            window.pushJson(data.getObject("window"));
            Translator.load(data.getString("language"));
            if(data.containsKey("settings")) {
                Options.load(data.getObject("settings"));
            }
        }
    }

    public static GloomGame getInstance() {
        return instance;
    }

    public static void fireEvent(Event<?> event) {
        getInstance().getEventHandler().fire(event);
    }

    public static void registerEventListener(EventListener listener) {
        getInstance().getEventHandler().register(listener);
    }

    private void shutdown() {
        System.out.println("Saving...");
        save();

        if (Options.DEVELOPER_MODE) {
            // delete all files in the folder ending _DUMP
            File folder = new File(".");
            File dumpFolder = new File(folder.getAbsolutePath() + "/_DUMP");
            if (dumpFolder.exists()) {
                if (dumpFolder.isDirectory()) {
                    File[] files = dumpFolder.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            System.out.printf("Deleted %s: %s%n", file.getName(), file.delete());
                        }
                    }
                }
            } else {
                if (!dumpFolder.mkdirs()) {
                    System.err.println("Failed to create dump folder");
                }
            }
            File dump = new File(dumpFolder, "LATEST_DUMP.txt");
            try {
                if (dump.createNewFile())
                    System.out.println("Main dump file created");
            } catch (IOException ignored) {
                System.err.println("Failed to create dump file");
                return;
            }

            try (FileWriter writer = new FileWriter(dump)) {

                writer.append("----- CACHED DATA -----\n");
                dataFetcher.getCache().asMap().forEach((k, v) -> {
                    try {
                        writer.append(k).append(": ").append(String.valueOf(v)).append("\n");
                    } catch (IOException ignored) {
                    }
                });

                writer.append("\n\n----- CACHED IMAGES -----\n");
                imageFetcher.getCache().asMap().forEach((k, v) -> {
                    try {
                        writer.append(k).append(": ").append("\n");
                    } catch (IOException ignored) {
                    }
                });

                writer.append("\n\n----- SCENES -----\n");
                scenes.forEach(scene -> {
                    try {
                        writer.append(scene.getClass().getSimpleName()).append(": ").append(String.valueOf(scene)).append("\n");
                    } catch (IOException ignored) {

                    }
                });

                writer.append("\nLast Scene: ").append(currentScene.getClass().getSimpleName()).append("\n");

                writer.append("\n\n----- EVENT LISTENERS -----\n");
                eventHandler.getListeners().forEach(listener -> {
                    try {
                        writer.append(listener.getClass().getSimpleName()).append(": ").append(listener.createCacheDump()).append("\n");
                    } catch (IOException ignored) {
                    }
                });

                writer.append("\n\n----- END -----");
                String[] randomSentences = new String[]{
                        "I hope you enjoyed this dump",
                        "Ducks are awesome",
                        "Never gonna give you up",
                        "Good luck",
                        "I'm a dump",
                        "Beep boop",
                        "Beep beep I'm a dump",
                        "Hello awesome!",
                        "Nothin' to see here",
                        "Your mom is a dump",
                        "42 is the answer to life, the universe and everything",
                        "(╯°□°）╯︵ ┻━┻",
                        "/╲/\\╭(°□°)╮/\\╱\\",
                        "┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻",
                        "┻━┻ ︵ ╯︵╰(°□°)╯︵ ┻━┻",
                        "(O_o)",
                        "┬─┬ノ( º _ ºノ)",
                        "Feelin' good",
                        "Shit happens",
                        "You should probably stop and take a dump 💩",
                        "Try rubber ducking",
                        "Imagination is the best",
                        "I am writing these instead of doing something productive",
                        "I am dumb",
                        "gloom_original.ttf is a great font",
                        "╚(•⌂•)╝",
                        "╚(⌂⌂)╝",
                        "╚(⌂⌂⌂⌂⌂)╝",
                        "╚(⌂⌂⌂⌂⌂⌂⌂)╝",
                        "Github copilot is a great guy",
                        "Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah",
                        "Help me",
                        "This code is literally trash",
                        "Are you gloomy?",
                        "Piss off",
                        "\uD83D\uDE2D"
                };
                writer.append("\n\n").append(randomSentences[new Random().nextInt(randomSentences.length)]);
                writer.flush();

                System.out.println("Dump finished");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Exiting...");
    }
}
