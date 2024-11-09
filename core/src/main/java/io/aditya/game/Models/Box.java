package io.aditya.game.Models;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.Array;

public class Box
{
    ModelBuilder ModelBuilder = new ModelBuilder();
    public Model box = ModelBuilder.createBox(200f, 200f, 200f,
        new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("box.jpg")))),
        Usage.TextureCoordinates|Usage.Position | Usage.Normal);

//    Material blackMaterial = new Material(ColorAttribute.createDiffuse(Color.BLACK));
//        box.nodes.get(0).parts.get(4).material = blackMaterial;

}
