package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    Texture drop;
    Texture bucketImage;
    Rectangle bucket;
    OrthographicCamera camera;
    Array<Rectangle> raindrops;
    long lastDropTime;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        drop = new Texture("drop.png");
        bucketImage = new Texture("bucket.png");

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new Array<>();
        spawnRaindrop();
    }

    @Override
    public void render() {
//        ScreenUtils.clear(1, 0, 0, 1);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : raindrops) {
            batch.draw(drop, raindrop.x, raindrop.y);
        }
        batch.end();

//        batch.begin();
//        batch.draw(img, 0, 0);
//        batch.draw(bucketImage,0,1);
//        batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 300 * Gdx.graphics.getDeltaTime();

        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 800 - 64) bucket.x = 800 - 64;

        if ((TimeUtils.nanoTime() - lastDropTime > 1000000000)) {
            spawnRaindrop();
        }

        // 雨滴随机生成，下落到地面消失不见
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 15 < 0) {
                iter.remove();
            }
            if (raindrop.overlaps(bucket)) {
                iter.remove();
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        drop.dispose();
        bucketImage.dispose();
    }

    private void spawnRaindrop() {
        Rectangle rainDrop = new Rectangle();
        rainDrop.x = MathUtils.random(0, 800 - 64);
        rainDrop.y = 480;
        rainDrop.width =15;
        rainDrop.height = 15;
        raindrops.add(rainDrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
