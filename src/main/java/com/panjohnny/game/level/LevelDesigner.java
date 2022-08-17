package com.panjohnny.game.level;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.data.plf.PLFTools;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.panjohnny.game.render.Window;

import javax.swing.*;

/**
 * Used to design levels.
 */
public class LevelDesigner extends Scene implements EventListener {
    private ArrayList<GameObject> current;

    @Getter
    @Setter
    private boolean inEditMode = true;
    private Cursor deleteCursor;

    @Getter
    private Mode mode = Mode.NONE;

    private TextWidget debugText;

    private BufferedImage cursor;
    private int cursorX, cursorY;
    private GameObject cursorType;

    private Drawable cursorDrawable;

    private ObjectOptionWidget objectOptions;

    private boolean exporting = false;

    @Override
    public Scene init() {
        // TODO fix this and work on the scaling natively that should fucking help me! Also do something about the screen size!
        reset();
        GloomGame.registerEventListener(this);
        deleteCursor = Toolkit.getDefaultToolkit().createCustomCursor(GloomGame.getInstance().getImageFetcher().get("/assets/designer/trashcan.png"), new Point(16, 16), "trashcan cursor");

        LevelManager manager = new LevelManager(LevelParser.parseInternal("test.plf"));
        ButtonWidget test = new ButtonWidget(100, 300, (b) -> manager.load(0), buttonWidgetGraphicsPair -> ButtonWidget.overlay(buttonWidgetGraphicsPair, Colors.RED), "Test");


        ClickableImageWidget trashcan = createBasicImageButton("/assets/designer/trashcan.png", 10, 10, (b) -> {
            if (mode == Mode.DELETE) {
                mode = Mode.NONE;
                GloomGame.getInstance().getWindow().showCursor(true);
            } else {
                mode = Mode.DELETE;
                GloomGame.getInstance().getWindow().setCustomCursor(deleteCursor);
            }
        });

        ClickableImageWidget export = createBasicImageButton("/assets/designer/export.png", 10, 50, (b) -> {
            exporting = true;
            List<ObjectOptionWidget.Option<?>> options = new ArrayList<>();
            options.add(ObjectOptionWidget.Option.string("name", "Level -1", ObjectOptionWidget.Option.emptyConsumer()));
            options.add(ObjectOptionWidget.Option.string("author", "Anonymous", ObjectOptionWidget.Option.emptyConsumer()));
            ObjectOptionWidget widget = new ObjectOptionWidget(options, Window.WIDTH / 2 - 50, 10, 30, this);

            List<GameObject> clone = new ArrayList<>(current);
            mode=Mode.NONE;
            ButtonWidget button = new ButtonWidget(Window.WIDTH - 100, 10, (b2) -> {
                StringBuilder sb = new StringBuilder();
                sb.append(LevelParser.CURRENT_VERSION);
                sb.append("\n");
                sb.append(widget.getValue("name"));
                sb.append("\n");
                sb.append(widget.getValue("author"));
                sb.append("\n");
                // TODO add prefab support
                sb.append(PLFTools.stringifyArraySmart(clone));
                sb.append("\n");
                sb.append("[").append(widget.getValue("prefabs")).append("]");

                exporting = false;
                JOptionPane.showMessageDialog(null, sb, "Output", JOptionPane.PLAIN_MESSAGE);

                inEditMode = false;
                current.clear();
                inEditMode = true;
            }, buttonWidgetGraphicsPair -> ButtonWidget.overlay(buttonWidgetGraphicsPair, Colors.RED), "Export");
            current.add(button);
            current.add(widget);
        });

        debugText = new TextWidget(Window.WIDTH - 100, Window.HEIGHT - 20, "", Colors.YELLOW, 15);
        cursor = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        cursorDrawable = (g) -> g.drawImage(cursor, cursorX, cursorY, null);
        add(test);

        add(debugText);
        add(trashcan);
        add(createTypePlacer("/assets/tiles/tile.png", 50, 10, new Tile()));
        add(createTypePlacer("/assets/tiles/bulb.png", 90, 11, new Bulb(0,0)));
        add(export);

        objectOptions = new ObjectOptionWidget(new LinkedList<>(), Window.WIDTH - 100, 10, 30, this);
        add(objectOptions);

        current = new ArrayList<>();
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
        if(!inEditMode)
            return;
        if (event.isLeftClick()) {
            if (mode != Mode.NONE && mode != null && !exporting) {
                switch (mode) {
                    case PLACE -> {
                        GloomGame.getInstance().getWindow().showCursor(true);
                        GameObject nObject = cursorType.clone().apply((g) -> {
                                    g.setX(cursorX);
                                    g.setY(cursorY);
                                    g.setWidth(32);
                                    g.setHeight(32);

                                    debugText.setText(g.getClass().getSimpleName() + "(X: " + cursorX + " Y: " + cursorY + " W: " + g.getWidth() + " H: " + g.getHeight() + ")");
                                });
                        current.add(nObject);

                        objectOptions.setOptions(genBasicObjectOptions(nObject));

                        mode = Mode.NONE;
                    }
                    case DELETE -> {
                        for (GameObject o : current) {
                            if (o.getBound().contains(cursorX, cursorY)) {
                                System.out.println("Found object to delete");
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

        if(getSelected() != null) {
            for (Drawable d : getOfType(ObjectOptionWidget.class)) {
                ObjectOptionWidget oow = (ObjectOptionWidget) d;
                if (oow.isOver(event.getX(), event.getY()))
                    oow.onInteract(event);
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
            if(!exporting)
                copy.addAll(getDrawables());
            Renderer.getInstance().render(copy);
        } else {
            super.render();
        }
    }

    public List<ObjectOptionWidget.Option<?>> genBasicObjectOptions(GameObject object) {
        List<ObjectOptionWidget.Option<?>> options = new LinkedList<>();
        options.add(ObjectOptionWidget.Option.integer("x", object.getX(), (a) -> object.setX(a.getValue())));
        options.add(ObjectOptionWidget.Option.integer("y", object.getY(), (a) -> object.setY(a.getValue())));
        options.add(ObjectOptionWidget.Option.integer("width", object.getWidth(), (a) -> object.setWidth(a.getValue())));
        options.add(ObjectOptionWidget.Option.integer("height", object.getHeight(), (a) -> object.setHeight(a.getValue())));
        return options;
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
    public GameObject getSelected() {
        if (current.isEmpty())
            return null;
        if (exporting)
            return new Tile();
        return current.get(current.size()-1);
    }

    enum Mode {
        DELETE,
        DRAG,
        PLACE,
        NONE
    }
}
