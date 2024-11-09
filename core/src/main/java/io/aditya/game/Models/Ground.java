package io.aditya.game.Models;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Ground
{
    ModelBuilder ModelBuilder = new ModelBuilder();
    public Vector3 groundSize = new Vector3(50000f,4f,50000f);
    public Model ground = ModelBuilder.createBox(groundSize.x,groundSize.y,groundSize.z,
        new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("ground3.jpg")))),
        Usage.TextureCoordinates|Usage.Position | Usage.Normal);



}
