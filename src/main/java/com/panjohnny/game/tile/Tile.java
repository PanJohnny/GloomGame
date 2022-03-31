package com.panjohnny.game.tile;

import com.panjohnny.game.GameObject;
import com.panjohnny.game.GloomGame;
import com.panjohnny.game.Player;
import com.panjohnny.game.scenes.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile extends GameObject {

    private final BufferedImage tile;
    public Tile(int x, int y, int width, int height) {
        super(x, y, width, height);

        tile = GloomGame.getInstance().getImageFetcher().get("/tiles/tile.png");
    }

    @Override
    public void tick() {
        // detect any game object colliding if it is player then stop it from moving into the tile
        Scene scene = GloomGame.getInstance().getCurrentScene();
        for (GameObject object : scene.getObjects()) {
            if (object.getActualBound().intersects(this.getActualBound()) && object instanceof Player player) {
                // find from which side the player is colliding and stop it from moving into the tile
                if (player.getX() < this.getX()) {
                    player.setX(this.getX() - player.getWidth());
                } else if (player.getX() + player.getWidth() > this.getX() + this.getWidth()) {
                    player.setX(this.getX() + this.getWidth());
                } else if (player.getY() < this.getY()) {
                    player.setY(this.getY() - player.getHeight());
                    player.setOnGround(true);
                } else if (player.getY() + player.getHeight() > this.getY() + this.getHeight()) {
                    player.setY(this.getY() + this.getHeight());
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(tile, getActualPosition().x, getActualPosition().y, getActualSize().width, getActualSize().height, null);
    }
}
