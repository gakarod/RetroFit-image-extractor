package com.pointless;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;

import javax.naming.Context;
import javax.swing.text.html.HTML;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.URL;



import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Pixmap;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;


import com.badlogic.gdx.utils.StreamUtils;

/**
 * Created by vaibh on 09-04-2018.
 */

public class main extends Game {
    TextureRegion image;

    BitmapFont font;

    SpriteBatch batch;



    @Override

    public void create () {

        new Thread(new Runnable() {

            /** Downloads the content of the specified url to the array. The array has to be big enough. */

            private int download (byte[] out, String url) {

                InputStream in = null;

                try {

                    HttpURLConnection conn = null;

                    conn = (HttpURLConnection)new URL(url).openConnection();

                    conn.setDoInput(true);

                    conn.setDoOutput(false);

                    conn.setUseCaches(true);

                    conn.connect();

                    in = conn.getInputStream();

                    int readBytes = 0;

                    while (true) {

                        int length = in.read(out, readBytes, out.length - readBytes);

                        if (length == -1) break;

                        readBytes += length;

                    }

                    return readBytes;

                } catch (Exception ex) {

                    return 0;

                } finally {

                    StreamUtils.closeQuietly(in);

                }

            }



            @Override

            public void run () {

                byte[] bytes = new byte[200 * 1024]; // assuming the content is not bigger than 200kb.

                int numBytes = download(bytes, "http://78.media.tumblr.com/tumblr_kstpz9IxDK1qzefipo1_250.gif");

                if (numBytes != 0) {

                    // load the pixmap, make it a power of two if necessary (not needed for GL ES 2.0!)

                    Pixmap pixmap = new Pixmap(bytes, 0, numBytes);

                    final int originalWidth = pixmap.getWidth();

                    final int originalHeight = pixmap.getHeight();

                    int width = MathUtils.nextPowerOfTwo(pixmap.getWidth());

                    int height = MathUtils.nextPowerOfTwo(pixmap.getHeight());

                    final Pixmap potPixmap = new Pixmap(width, height, pixmap.getFormat());

                    potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());

                    pixmap.dispose();

                    Gdx.app.postRunnable(new Runnable() {

                        @Override

                        public void run () {

                            image = new TextureRegion(new Texture(potPixmap), 0, 0, originalWidth, originalHeight);

                        }

                    });

                }

            }

        }).start();



        font = new BitmapFont();

        batch = new SpriteBatch();

    }



    @Override

    public void render () {

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if (image != null) {

            batch.begin();

            batch.draw(image, 100, 100);

            batch.end();

        } else {

            batch.begin();

            font.draw(batch, "Downloading...", 100, 100);

            batch.end();

        }

    }

}