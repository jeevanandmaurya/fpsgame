package io.aditya.game.Models;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Bullet
{
    ModelBuilder ModelBuilder = new ModelBuilder();

    public Model bullet = ModelBuilder.createBox(6f,6f,40f,
        new Material(ColorAttribute.createDiffuse(Color.WHITE)),
        Usage.TextureCoordinates | Usage.Position | Usage.Normal);



}
