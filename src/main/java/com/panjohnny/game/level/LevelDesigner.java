package com.panjohnny.game.level;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.event.EventListener;
import com.panjohnny.game.event.EventTarget;
import com.panjohnny.game.io.Mouse;
import com.panjohnny.game.io.MouseClickEvent;
import com.panjohnny.game.light.Bulb;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.render.Drawable;
import com.panjohnny.game.render.Renderer;
import com.panjohnny.game.scenes.Scene;
import com.panjohnny.game.tile.Tile;
import com.panjohnny.game.util.ImageUtil;
import com.panjohnny.game.widgets.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.function.Consumer;

public class LevelDesigner extends Scene implements EventListener {
    private LinkedList<GameObject> current;

    @Getter
    @Setter
    private boolean inEditMode = true;
    private Cursor deleteCursor;

    private Mode mode;

    private TextWidget debugText;

    private BufferedImage cursor;
    private int cursorX, cursorY;
    private GameObject cursorType;

    private Drawable cursorDrawable;

    @Override
    public Scene init() {
        // TODO fix this and work on the scaling natively that should fucking help me! Also do something about the screen size!
        reset();
        GloomGame.registerEventListener(this);
        deleteCursor = Toolkit.getDefaultToolkit().createCustomCursor(GloomGame.getInstance().getImageFetcher().get("/assets/designer/trashcan.png"), new Point(16, 16), "trashcan cursor");

        // LevelManager manager = new LevelManager(LevelParser.parseInternal("test.plf"));
        // ButtonWidget test = new ButtonWidget(700, 10, (b) -> manager.load(0), buttonWidgetGraphicsPair -> ButtonWidget.overlay(buttonWidgetGraphicsPair, Colors.RED), "Test");


        ClickableImageWidget trashcan = createBasicImageButton("/assets/designer/trashcan.png", 10, 10, (b) -> {
            if (mode == Mode.DELETE) {
                mode = Mode.DRAG;
                GloomGame.getInstance().getWindow().showCursor(true);
            } else {
                mode = Mode.DELETE;
                GloomGame.getInstance().getWindow().setCustomCursor(deleteCursor);
            }
        });

        debugText = new TextWidget(700, 20, "", Colors.YELLOW, 15);
        cursor = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        cursorDrawable = (g) -> g.drawImage(cursor, cursorX, cursorY, null);
        //add(test);

        add(debugText);
        add(trashcan);
        add(createTypePlacer("/assets/tiles/tile.png", 50, 10, new Tile()));
        add(createTypePlacer("/assets/tiles/bulb.png", 90, 11, new Bulb(0,0)));


        current = new LinkedList<>();
        return this;
    }

    @Override
    public void update() {
        super.update();

        if (mode == Mode.PLACE || mode == Mode.DRAG) {
            cursorX = Mouse.getInstance().getFixedX();
            cursorY = Mouse.getInstance().getFixedY();
        }
    }

    @EventTarget(MouseClickEvent.class)
    public void click(MouseClickEvent event) {
        if (event.isLeftClick()) {
            if (mode != Mode.NONE && mode != null) {
                switch (mode) {
                    case PLACE -> {
                        GloomGame.getInstance().getWindow().showCursor(true);
                        current.add(cursorType.clone().apply((g) -> {
                            g.setX(cursorX);
                            g.setY(cursorY);
                            g.setWidth(32);
                            g.setHeight(32);

                            debugText.setText(g.getClass().getSimpleName() + "(X: " + cursorX + " Y: " + cursorY + " W: " + g.getWidth() + " H: " + g.getHeight() + ")");
                        }));

                        mode = Mode.NONE;
                    }
                    case DELETE -> {
                        for (GameObject o : current) {
                            if (o.getBound().intersects(new Rectangle(cursorX, cursorY, 10, 10))) {
                                current.remove(o);
                                break;
                            }
                        }
                    }
                    default -> {
                    }
                }
            }

            for (Drawable d : getOfType(ClickableImageWidget.class)) {
                if (WidgetUtil.isMouseOver((Widget) d)) {
                    ClickableImageWidget ciw = (ClickableImageWidget) d;
                    ciw.onInteract(ciw);
                }
            }
        }
    }

    @Override
    public void render() {
        if (inEditMode) {
            // create copy of current
            LinkedList<Drawable> copy = new LinkedList<>(current);
            if (mode == Mode.DRAG || mode == Mode.PLACE) {
                copy.addFirst(cursorDrawable);
            }
            copy.addAll(getDrawables());
            Renderer.getInstance().render(copy);
        } else {
            super.render();
        }
    }

    private ClickableImageWidget createBasicImageButton(String image, int x, int y, Consumer<ClickableImageWidget> onClick) {
        return new ClickableImageWidget(ImageUtil.combine("/assets/designer/background.png", image), 32, 32, x, y).setOnClick(onClick);
    }

    private ClickableImageWidget createTypePlacer(String image, int x, int y, GameObject type) {
        return createBasicImageButton(image, x, y, (b) -> {
            mode = Mode.PLACE;
            GloomGame.getInstance().getWindow().showCursor(false);
            cursor = ImageUtil.combine(image, "/assets/designer/placing.png");
            cursorType = type;

            cursorX = x;
            cursorY = y;

            debugText.setText(type.getClass().getSimpleName());
        });
    }

    enum Mode {
        DELETE,
        DRAG,
        PLACE,
        NONE
    }
}
