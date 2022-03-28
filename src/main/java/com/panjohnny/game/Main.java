package com.panjohnny.game;

import com.google.gson.JsonObject;
import com.panjohnny.game.data.DataSet;
import com.panjohnny.game.data.FlagChecker;
import com.panjohnny.game.data.GameDataManager;
import com.panjohnny.game.event.Event;
import com.panjohnny.game.event.EventHandler;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.mem.DataFetcher;
import com.panjohnny.game.mem.ImageFetcher;
import com.panjohnny.game.render.FontRenderer;
import com.panjohnny.game.render.Renderer;
import com.panjohnny.game.render.Window;
import com.panjohnny.game.scenes.MainMenu;
import com.panjohnny.game.scenes.OptionScene;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.scenes.TestScene;
import lombok.Getter;

import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static Main instance;

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
    private Scene currentScene;
    private boolean currentSceneLoaded = true;

    @Getter
    private final ImageFetcher imageFetcher;
    @Getter
    private final DataFetcher dataFetcher;

    @Getter
    private final EventHandler eventHandler;

    public Main() {
        imageFetcher = new ImageFetcher();
        dataFetcher = new DataFetcher();
        eventHandler = new EventHandler();
        scenes = new ArrayList<>();
        gameThread = new Thread(() -> {
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
                    if(Options.DEVELOPER_MODE) {
                        System.out.printf("FPS: %d, UPS: %d%n", frames, updates);
                        if(window!=null) {
                            window.debug(String.format("FPS: %d, UPS: %d", frames, updates));
                        }
                    }
                    frames = 0;
                    updates = 0;
                }
            }
        }, "Main Thread");

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
                pressKey(e.getKeyCode());
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                releaseKey(e.getKeyCode());
            }
        });

        this.dataManager = new GameDataManager();

        load();

        scenes.add(new MainMenu());
        scenes.add(new OptionScene());
        scenes.add(new TestScene());

        setScene(0);
    }

    public static void main(String[] args) {
        FlagChecker.check(args);
        instance = new Main();
        instance.start();
    }

    public synchronized void start() {
        running = true;
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
        if(!currentSceneLoaded) {
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
    }

    public void save() {
        JsonObject json = new JsonObject();
        json.add("window", window.toJson());
        dataManager.saveFile(new DataSet(json), "/latest_session.json");
    }

    public void load() {
        FontRenderer.load();
        DataSet data = dataManager.loadFile("/latest_session.json");
        if(!data.isEmpty()) {
            window.pushJson(data.getObject("window"));
        }
    }

    public void pressKey(int key) {
        currentScene.onKeyPress(key);
    }

    public void releaseKey(int key) {
        currentScene.onKeyRelease(key);
    }

    public static Main getInstance() {
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
            File dump = new File("LATEST_DUMP.txt");
            try {
                if(dump.createNewFile())
                    System.out.println("Dump file created");
                System.out.println("Dumping to " + dump.getAbsolutePath());
            } catch (IOException ignored) {
            }

            try {
                FileWriter writer = new FileWriter(dump);

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
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Exiting...");
    }
}
