package io.aditya.game.Models;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.VertexAttributes.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class Gun extends ApplicationAdapter
{

    public Model gun;

    @Override
    public void create() {


        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder meshBuilder;
        Node node1 = modelBuilder.node();
        node1.translation.set(0,0,-40f);
        meshBuilder = modelBuilder.part("barrel", GL20.GL_TRIANGLES,  Usage.TextureCoordinates | Usage.Position | Usage.Normal,  new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("gun.jpg")))));
        meshBuilder.box(15f, 20f, 80f);

        Node node2 = modelBuilder.node();
        node2.translation.set(0,-15,0);
        meshBuilder = modelBuilder.part("grip", GL20.GL_TRIANGLES, Usage.TextureCoordinates | Usage.Position | Usage.Normal,  new Material(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("gun.jpg")))));
        meshBuilder.box(15f, 30f, 15f);

        gun=modelBuilder.end();


    }


    @Override
    public void dispose() {

            gun.dispose();

    }
}
