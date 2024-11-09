package io.aditya.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen{

    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    Vector3 camPos;

    //Scene
    Scene Scene = new Scene();

    @Override
    public void show() {
        // Prepare your screen here.

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camPos = new Vector3(0f, 200f, 100f);
        cam.position.set(camPos);
        cam.lookAt(0,180,0);
        cam.near = 1f;
        cam.far = 100000f;
        cam.update();

        //SceneCreated
        Scene.create();
        System.out.println("Hello World");


        camController = new CameraInputController(cam);
        camController.pinchZoomFactor=10f;
       //Gdx.input.setInputProcessor(camController);


    }

    @Override
    public void render(float delta) {



        camController.update();

        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        //Scene
        Scene.render();
        Scene.ModelBatch.begin(cam);
        Scene.ModelBatch.render(Scene.ModelInstance, environment);
        Scene.ModelBatch.end();

        cam.position.set(Scene.camPos);
        cam.update();

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {

        //Scene
        Scene.dispose();
        // This method is called when disposing a screen.
    }
}
