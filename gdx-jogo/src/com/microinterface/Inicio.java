package com.microinterface;

import com.badlogic.gdx.Game;

public class Inicio extends Game {
    @Override
    public void create() {
        setScreen(new TelaTeste());
    }

    @Override
    public void render() {
        super.render(); 
    }
}
