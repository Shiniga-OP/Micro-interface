package com.microinterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class GerenciadorUI {
    public ArrayList<Componente> componentes = new ArrayList<Componente>();
    public ArrayList<CaixaDialogo> dialogos = new ArrayList<CaixaDialogo>();
    public CampoTexto campoEmFoco = null;

    public void add(Componente componente) {
        componentes.add(componente);
    }

    public void rm(Componente componente) {
        componentes.remove(componente);
    }

    public void addDialogo(CaixaDialogo dialogo) {
        dialogos.add(dialogo);
    }

    public void rmDialogo(CaixaDialogo dialogo) {
        dialogos.remove(dialogo);
    }

    public void limpar() {
        componentes.clear();
        dialogos.clear();
        campoEmFoco = null;
    }

    public void defFocoTexto(CampoTexto campo) {
        if(campoEmFoco != null && campoEmFoco != campo) {
            campoEmFoco.defFoco(false);
        }
        campoEmFoco = campo;
    }

    public boolean processarToque(float x, float y, boolean pressionado) {
        for(int i = dialogos.size() - 1; i >= 0; i--) {
            CaixaDialogo dialogo = dialogos.get(i);
            if(dialogo.ativa && dialogo.aoTocar(x, y, pressionado)) {
                return true;
            }
        }
        for(int i = componentes.size() - 1; i >= 0; i--) {
            Componente comp = componentes.get(i);

            if(comp instanceof CampoTexto) {
                CampoTexto campo = (CampoTexto) comp;
                if(campo.contem(x, y) && !pressionado) {
                    defFocoTexto(campo);
                }
            }
            if(comp.aoTocar(x, y, pressionado)) return true;
        }
        if(!pressionado && campoEmFoco != null) {
            boolean tocouNoCampo = campoEmFoco.contem(x, y);
            if(!tocouNoCampo) {
                campoEmFoco.defFoco(false);
                campoEmFoco = null;
            }
        }
        return false;
    }

    public void processarArraste(float x, float y) {
        for(int i = dialogos.size() - 1; i >= 0; i--) {
            CaixaDialogo dialogo = dialogos.get(i);
            if(dialogo.ativa) {
                dialogo.aoArrastar(x, y);
            }
        }
    }

    public boolean processarTecla(int keycode) {
        if(campoEmFoco != null) {
            return campoEmFoco.processarTecla(keycode);
        }
        return false;
    }

    public boolean processarCaractere(char caractere) {
        if(campoEmFoco != null) {
            return campoEmFoco.processarCaractere(caractere);
        }
        return false;
    }

    public void desenhar(SpriteBatch pincel, float delta) {
        for(int i = 0; i < componentes.size(); i++) {
            componentes.get(i).desenhar(pincel, delta, 0, 0);
        }
        for(int i = 0; i < dialogos.size(); i++) {
            CaixaDialogo dialogo = dialogos.get(i);
            if(dialogo.ativa) {
                dialogo.desenhar(pincel, delta, 0, 0);
            }
        }
    }

    public boolean temDialogoAtivo() {
        for(int i = 0; i < dialogos.size(); i++) {
            if(dialogos.get(i).ativa) {
                return true;
            }
        }
        return false;
    }
}
