package com.pointless;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import javax.naming.Context;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class test extends Game {
	TextureRegion image;

	BitmapFont font;

	SpriteBatch batch;
	int numBytes  ;


	@Override

	public void create () {

		new Thread(new Runnable() {
			InputStream in = null;
			/** Downloads the content of the specified url to the array. The array has to be big enough. */

			private void download (final byte[] out, final String url) {


				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(url)
						.addConverterFactory(GsonConverterFactory.create())
						.build();

				RetrofitImageAPI service = retrofit.create(RetrofitImageAPI.class);

				Call<ResponseBody> call = service.getImageDetails();

				call.enqueue(new Callback<ResponseBody>() {

					@Override
					public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
						try {
							HttpURLConnection conn = null;

							conn = (HttpURLConnection)new URL(url).openConnection();

							conn.setDoInput(true);

							conn.setDoOutput(false);

							conn.setUseCaches(true);

							conn.connect();

							in = conn .getInputStream();

						//	 readBytes = 0;

							while (true) {

								int length = in.read(out, numBytes, out.length - numBytes);

								if (length == -1) break;

								numBytes += length;

							}

						} catch (Exception ex) {

						} finally {
							StreamUtils.closeQuietly(in);
						}
					}

					@Override
					public void onFailure(Throwable t) {

					}


	/*				*/


				});

			}



			@Override

			public void run () {

				byte[] bytes = new byte[200 * 1024]; // assuming the content is not bigger than 200kb.
						download(bytes, "https://www.gstatic.com/webp/gallery3/1.png");

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
