package io.aditya.game;
import io.aditya.game.Models.*;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Scene implements ApplicationListener, InputProcessor {


    //Scale
    //1m = 100f
    //1cm=1f
    //avg height of human 1.6m = 160cm=160f
    //FirstScreen
    FirstScreen screen;
    Vector3 camPos;
    //Scene
    ModelBatch ModelBatch = new ModelBatch();
    Array<ModelInstance> ModelInstance = new Array<ModelInstance>();
    //Physics
    int gravity = 10;

    //Background
    SpriteBatch bgBatch,crosshairBatch;
    Texture bgTexture,crosshairTexture;

    //Models
    //Ground
    Model groundModel;
    ModelInstance groundI;
    Vector3 groundPos;
    Vector3 groundSize;

    //Box
    Model boxModel;
    Array<ModelInstance> boxI = new Array<ModelInstance>();
    Array<Vector3> boxPos = new Array<Vector3>();

    //Ball
    int ballIndex;
    Model ballModel;
    ModelInstance ballI;
    Vector3 ballPos;
    float ballVel = 10f;
    boolean redBall=false;
    float fadeTime=0;

    //Gun
    boolean fire=false;
    boolean fired=false;
    Gun gun;
    Model gunModel;
    ModelInstance gunI;
    Vector3 gunPos;
    Vector3 gunDir;
    Vector3 gunVel;
    Vector3 gunAcc;
   // float gunAngle=0;
    float wX=0;
    float torque=0;
    float loadindTime=0;
    float reloadingTime=0;
    int chamberBulletIndex=-1;//no bullet in chamber

    //Bullet
    int magCapacity=20;
    int magBulletCount = 20;
    Model bulletModel;
    Array<ModelInstance> bulletI = new Array<ModelInstance>();
    Array<Vector3> bulletPos = new Array<Vector3>();
    Array<Vector3> bulletDistTravelled = new Array<Vector3>();
    Array<Vector3> bulletVel = new Array<Vector3>();
    Array<Vector3> bulletAcc = new Array<Vector3>();


    private int width;

    private int height;
    Sound gunshot,hitsound;

    @Override
    public void create() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        screen = new FirstScreen();
        camPos=new Vector3(0f, 200f, 100f);


        //bg
        bgBatch=new SpriteBatch();
        bgTexture=new Texture(Gdx.files.internal("background.jpg"));

        //crosshair
        crosshairBatch=new SpriteBatch();
        crosshairTexture=new Texture(Gdx.files.internal("background.jpg"));

        //Models
        ModelBatch = new ModelBatch();


        //Sound
        gunshot = Gdx.audio.newSound(Gdx.files.internal("pistol-shot.mp3"));
        hitsound = Gdx.audio.newSound(Gdx.files.internal("hit-sound.mp3"));
        //Ball
        ballModel = new Ball().ball;
        ballI = new ModelInstance(ballModel);
        ballPos = new Vector3(-10, 100, -200);
        ballI.transform.setTranslation(ballPos);


        //Ground
        groundModel = new Ground().ground;
        groundSize=new Ground().groundSize;
        groundI = new ModelInstance(groundModel);
        groundPos = new Vector3(0, -2, 0);
        groundI.transform.setTranslation(groundPos);

        //Box
        boxModel = new Box().box;
        for (int i = 0; i < 10; i++) {

            boxI.add(new ModelInstance(boxModel));
            boxPos.add(new Vector3(-5000+i*1000, 100, -1000 - (int)(i%2) *1000));
            boxI.get(i).transform.setTranslation(boxPos.get(i));

        }


        //Gun
        gun = new Gun();
        gun.create();
        gunModel = gun.gun;
        gunI = new ModelInstance(gunModel);
        // camPos = new Vector3(0f, 200f, 100f);
        gunPos = new Vector3(0, 150, 5);
        camPos=new Vector3(gunPos.x,gunPos.y+50,gunPos.z-95);
        gunVel=new Vector3(0,0,0);
        gunAcc=new Vector3(0.1f,0.1f,0);
        gunI.transform.setTranslation(gunPos);
        wX=0;//Gained from bullet momentum


        //Bullet
        bulletModel = new Bullet().bullet;
        magBulletCount=20;//Loading bullets in Mag
        for (int i = 0; i < magBulletCount; i++) {

            bulletI.add(new ModelInstance(bulletModel));
            bulletPos.add(gunPos.cpy());//In Chamber
            bulletPos.get(i).y --;//In Magazine
            bulletDistTravelled.add(new Vector3(0,0,0));
            bulletVel.add(new Vector3(0,0,0));
            bulletAcc.add(new Vector3(0,0,0));
            bulletI.get(i).transform.setTranslation(bulletPos.get(i));

            System.out.println("ball pos z " + i + " = " + bulletPos.get(i).z);
        }
        //Loading 1 bullet in Chamber
        bulletPos.get(0).y++;
        chamberBulletIndex=0;//0 is index of bullet in chamber


        for (int i = 0; i < magCapacity; i++) {
            ModelInstance.add(bulletI.get(i));
        }
        ModelInstance.add(gunI);
        ModelInstance.add(ballI);
        for (int i = 0; i < boxI.size; i++) {
            ModelInstance.add(boxI.get(i));
        }
        ModelInstance.add(groundI);

        //input
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {


        float delta = Gdx.graphics.getDeltaTime();
        bgBatch.begin();
        bgBatch.draw(bgTexture,0,0,width,height);
        bgBatch.end();
        //ball motion
        ballPos.x += ballVel;
        if (ballPos.x >= 1000f || ballPos.x <= -1000f) {
            ballVel = -ballVel;
        }
        ballI.transform.setTranslation(ballPos);

        //Gun Translation and Rotation
        gunPos.x += gunVel.x;
        gunPos.z += gunVel.z;
        gunPos.y += gunVel.y;
    //Attaching cam to gun
        camPos=new Vector3(gunPos.x,gunPos.y+50,gunPos.z+95);

        gunVel.y -= 0.1f;
        if (gunPos.y < 150) {
            gunVel.y = 0;
            gunPos.y = 150;
        }
        gunI.transform.setToRotation(1, 0, 0, wX).translate(gunPos);
        wX -= torque;
      //aw  System.out.println("wx=" + wX + " torque= " + torque);
        if (wX <= 0) {
            torque = 0;
            wX = 0;

        }
        //System.out.println("wx=" + wX + " torque= " + torque);
        //   System.out.println("GUNPOS-> x="+gunPos.x + " y="+gunPos.y + " z="+gunPos.z);
        //   System.out.println("time->="+time + " timePeriod->="+timePeriod + "bulletCount->="+bulletCount);


        //Bullet

        //Resetting Position of Bullets
        for (int i = 0; i < magCapacity; i++) {

            if (bulletDistTravelled.get(i).z < -5000) {
                bulletPos.get(i).set(gunPos.cpy());
                bulletPos.get(i).y--;
                bulletDistTravelled.get(i).z = 0;
                bulletVel.get(i).z = 0;
                bulletVel.get(i).y = 0;
                bulletAcc.get(i).y = 0;
            }

            if (bulletVel.get(i).z == 0) {
                bulletPos.get(i).set(gunPos.cpy());
                if (i != chamberBulletIndex) {
                    bulletPos.get(i).y--;
                }
            }
        }

//        Loading bullets into Magazine
        for (int i = 0; i < magCapacity; i++) {
            if (bulletPos.get(i).y == gunPos.y - 1 && bulletVel.get(i).z == 0 && chamberBulletIndex==-1) {
                bulletPos.get(i).y++;
                chamberBulletIndex=i;
                fire = false;
                fired = false;
            }
        }

//            int fireFreq = 5;//1 bullet per seconds}
//            float timePeriod = (1f / fireFreq);
//            time += delta;
//            if (time >= timePeriod && bulletCount != 0) {
//                time = 0;
//                bulletCount--;
//
//            }

        //Loading bullets in Chamber

        //Fire
        if (fire && !fired) {
            fire = false;
            for(int i = 0; i < magCapacity; i++) {
                if (bulletPos.get(i).y == gunPos.y && bulletVel.get(i).z == 0) {
                    bulletVel.get(i).z =-500;
                    bulletAcc.get(i).y = -0.1f;//Gravity
                    wX = 20;
                    torque = 1;
                    gunshot.play(0.2f);
                    break;
                    }
                }

            chamberBulletIndex=-1;
            fired = true;
            System.out.println("Firing");
        }
//            if (fired) {
//                for (int i = 0; i < bulletI.size; i++) {
//                    if (fired  && bulletVel.get(i).z == 0 && bulletPos.get(i).y ==-1) {
//                        {
//                            fired = false;
//                            bulletVel.get(i).y = 0;
//                            bulletCount++;
//                            Mag--;
//
//                        }
//                        }
//                    }
//            }
//        System.out.println("fire="+fire+" fired="+fired);
//
        //Bullet Motion
        for (int i = 0; i < bulletPos.size; i++) {
            //Bullet Collision
            for (int j = 0; j < -(bulletVel.get(i).z)/10; j++) {
                bulletPos.get(i).z -= 10;
                for (int k = 0; k <magCapacity; k++) {
                    if (collision(ballPos,bulletPos.get(k),100) && !redBall) {
                        ModelInstance.get(21).materials.get(0).set(new ColorAttribute(ColorAttribute.Diffuse, Color.RED));
                        hitsound.play(1f);
                        redBall=true;
                    }
                }
            }
            //horizontal motion
            bulletDistTravelled.get(i).z += bulletVel.get(i).z;
            //bulletPos.get(i).z += bulletVel.get(i).z;

            //vertical motion
            bulletDistTravelled.get(i).y = bulletVel.get(i).y;
            bulletVel.get(i).y += bulletAcc.get(i).y;
            bulletPos.get(i).y += bulletVel.get(i).y;

            //Transforming bullet
            bulletI.get(i).transform.setTranslation(bulletPos.get(i));
            //  System.out.println("vel " +i+" ->z= " + bulletVel.get(i).z+" pos-> "+bulletPos.get(i).x+" "+bulletPos.get(i).y+" "+bulletPos.get(i).z );
        }

        //Vector3(-10, 10, -150);
        //Bullet Collision
//        for (int i = 0; i <magCapacity; i++) {
//            if ( collision(ballPos,bulletPos.get(i),100) && !redBall) {
//                ModelInstance.get(21).materials.get(0).set(new ColorAttribute(ColorAttribute.Diffuse, Color.RED));
//                hitsound.play(1f);
//                redBall=true;
//            }
//        }

        if (redBall){
            fadeTime+=delta;
            if (fadeTime>=1){
                ModelInstance.get(21).materials.get(0).clear();
                //ModelInstance.get(21).materials.get(0).set(new ColorAttribute(ColorAttribute.Diffuse, Color.WHITE));
                fadeTime=0;
                redBall=false;
            }
        }



        System.out.println("FPS= "+1.0f/delta);
        System.out.println(" Bullet distance travelled= "+bulletDistTravelled.get(0).z);

//        crosshairBatch.begin();
//        crosshairBatch.draw(crosshairTexture,width/2,height/2,100,100);
//        crosshairBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

        ballModel.dispose();
        groundModel.dispose();
        boxModel.dispose();
        gunModel.dispose();
        bulletModel.dispose();


    }


    public boolean collision(Vector3 objectPos,Vector3 bulletPos,int radius) {

        if(bulletPos.x>=objectPos.x-radius &&
            bulletPos.x<=objectPos.x+radius &&
            bulletPos.z>=objectPos.z-radius &&
            bulletPos.z<=objectPos.z+radius )
        {
            return true;
        }else {
            return false;
        }

    }


    @Override
    public boolean keyDown(int keycode) {

            switch (keycode){

                case Input.Keys.W:
                    gunVel.z=-10;
                    break;
                case Input.Keys.A:
                    gunVel.x=-10;
                    break;
                case Input.Keys.S:
                    gunVel.z=10;
                    break;
                case Input.Keys.D:
                    gunVel.x=10;
                    break;
                case Input.Keys.SPACE:
                    gunVel.y=5;

            }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch(keycode) {
            case Input.Keys.W:
                gunVel.z = -0;
                break;
            case Input.Keys.A:
                gunVel.x = -0;
                break;
            case Input.Keys.S:
                gunVel.z = 0;
                break;
            case Input.Keys.D:
                gunVel.x = 0;
                break;

        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.LEFT) {
            System.out.println("Left mouse button clicked");
            fire = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
