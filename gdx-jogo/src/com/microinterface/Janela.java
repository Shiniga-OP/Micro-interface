package com.microinterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Janela extends Componente {
    public PainelFatiado visual;
    public float escala;

    public Janela(PainelFatiado visual, float x, float y, float l, float a, float escala) {
        super(x, y, l, a);
        this.visual = visual;
        this.escala = escala;
    }

    @Override
    public void desenhar(SpriteBatch pincel, float delta, float paiX, float paiY) {
        // desenha o fundo usando logica de fatiamento
        visual.desenhar(pincel, x, y, largura, altura, escala);

        // passa o tamanho da janela para os filhos(como texto) saberem onde se alinhar
        for(Componente filho : filhos) {
            filho.largura = this.largura;
            filho.altura = this.altura;
            filho.desenhar(pincel, delta, this.x, this.y);
        }
    }
}
