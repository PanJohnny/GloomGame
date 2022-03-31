package com.panjohnny.game.mem;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.panjohnny.game.render.AnimatedTexture;
import lombok.NonNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

public class ImageFetcher {
    private final LoadingCache<String, BufferedImage> cache;

    public ImageFetcher() {
        CacheLoader<String, BufferedImage> loader = new CacheLoader<>() {
            @NonNull
            public BufferedImage load(@NonNull String key) throws Exception {
                InputStream stream = ImageFetcher.class.getResourceAsStream(key);
                if(Objects.isNull(stream)) {
                    throw new AssetNotFoundException(key);
                }
                return ImageIO.read(Objects.requireNonNull(ImageFetcher.class.getResourceAsStream(key)));
            }
        };

        this.cache = CacheBuilder.newBuilder().build(loader);
    }

    public BufferedImage get(String key) {
        return cache.getUnchecked(key);
    }

    public long size() {
        return cache.size();
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public AnimatedTexture getAnimation(String key, int rows, int cols, long timeBetweenFrames) {
        BufferedImage image = get(key);

        return new AnimatedTexture(image, rows, cols, timeBetweenFrames);
    }

    public LoadingCache<String, BufferedImage> getCache() {
        return cache;
    }
}
