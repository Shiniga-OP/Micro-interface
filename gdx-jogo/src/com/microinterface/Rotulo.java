package com.microinterface;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Rotulo extends Componente {
    public String texto;
    private BitmapFont fonte;
    private GlyphLayout medidor;
    private float escala;

    public Rotulo(String texto, BitmapFont fonte, float escala) {
        super(0, 0, 0, 0);
        this.texto = texto;
        this.fonte = fonte;
        this.escala = escala;
        this.medidor = new GlyphLayout();
    }

    @Override
    public void desenhar(SpriteBatch pincel, float delta, float paiX, float paiY) {
        // aplica a escala antes de medir e desenhar
        fonte.getData().setScale(escala);
        medidor.setText(fonte, texto);

        // centraliza com base no tamanho escalonado
        float posX = paiX + (largura / 2) - (medidor.width / 2);
        float posY = paiY + (altura / 2) + (medidor.height / 2);

        fonte.draw(pincel, texto, posX, posY);

        // reinicia a escala para não afetar outros textos por acidente
        fonte.getData().setScale(1.0f);
    }
}
