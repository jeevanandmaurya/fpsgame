package io.aditya.game.Models;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Ball
{
    ModelBuilder ModelBuilder = new ModelBuilder();

//    public Model ball = ModelBuilder.createSphere(20,20,20,30,30,
//        new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("ball2.jpg")))),
//        Usage.TextureCoordinates | Usage.Position | Usage.Normal);

    public Model ball = ModelBuilder.createCapsule(25,200,10,
        new Material(ColorAttribute.createDiffuse(Color.WHITE)),
        Usage.Position | Usage.Normal);


}
