package com.microinterface;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class GerenciadorUI {
	public ArrayList<Componente> componentes = new ArrayList<Componente>();
	public ArrayList<CaixaDialogo> dialogos = new ArrayList<CaixaDialogo>();
	public CampoTexto campoEmFoco = null;
	public Componente componenteCapturado = null;

	public void add(Componente componente) {
		componentes.add(componente);
		// registra o gerenciador em campos de texto
		registrarCamposTexto(componente);
	}

	public void rm(Componente componente) {
		componentes.remove(componente);
	}

	public void addDialogo(CaixaDialogo dialogo) {
		dialogos.add(dialogo);
		// registra o gerenciador em campos de texto dos dialogos
		registrarCamposTexto(dialogo);
	}

	public void rmDialogo(CaixaDialogo dialogo) {
		dialogos.remove(dialogo);
	}

	// registra recursivamente o gerenciador em todos os CampoTexto
	public void registrarCamposTexto(Componente componente) {
		if(componente instanceof CampoTexto) {
			((CampoTexto) componente).gerenciador = this;
		}

		// verifica se o componente tem filhos
		if(componente instanceof Painel) {
			Painel painel = (Painel) componente;
			for(Componente filho : painel.filhos) {
				registrarCamposTexto(filho);
			}
		} else if(componente instanceof CaixaDialogo) {
			CaixaDialogo dialogo = (CaixaDialogo) componente;
			for(Componente filho : dialogo.componentes) {
				registrarCamposTexto(filho);
			}
		}
	}

	public void limpar() {
		componentes.clear();
		dialogos.clear();
		campoEmFoco = null;
		componenteCapturado = null;
	}

	public void defFocoTexto(CampoTexto campo) {
		if(campoEmFoco != null && campoEmFoco != campo) {
			campoEmFoco.defFoco(false);
		}
		campoEmFoco = campo;
	}

	public boolean processarToque(float x, float y, boolean pressionado) {
		// primeiro verifica dialogos
		for(int i = dialogos.size() - 1; i >= 0; i--) {
			CaixaDialogo d = dialogos.get(i);
			if(d.ativa && d.aoTocar(x, y, pressionado)) return true;
		}

		// se ta soltando o toque(pressionado = false)
		if(!pressionado) {
			// se tem um componente capturado, envia o evento so pra ele
			if(componenteCapturado != null) {
				boolean resultado = componenteCapturado.aoTocar(x, y, false);
				componenteCapturado = null;
				return resultado;
			}
			// se não tem componente capturado, processa normalmente
		}
		// processamento normal(tanto pra pressionar quanto para soltar sem captura)
		for(int i = componentes.size() - 1; i >= 0; i--) {
			Componente c = componentes.get(i);
			if(c.contem(x, y)) {
				if(c.aoTocar(x, y, pressionado)) {
					// so captura se ta pressionando E o componente precisa de arraste
					if(pressionado && c.capturaArraste()) {
						componenteCapturado = c;
					}
					return true;
				}
			}
		}
		return false;
	}

	public void processarArraste(float x, float y) {
		// primeiro processa dialogos
		for(int i = dialogos.size() - 1; i >= 0; i--) {
			CaixaDialogo dialogo = dialogos.get(i);
			if(dialogo.ativa) {
				dialogo.aoArrastar(x, y);
			}
		}
		// se tem um componente capturado que precisa de arraste, envia o evento
		if(componenteCapturado != null) {
			componenteCapturado.aoTocar(x, y, true);
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

