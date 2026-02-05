package com.microinterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class TelaDemo implements Screen, InputProcessor {
    public SpriteBatch pincel;
    public ShapeRenderer pincelFormas;
    public BitmapFont fonte;
    public OrthographicCamera camera;
    public Viewport vista;
    public Vector3 toqueAuxiliar = new Vector3();

    public GerenciadorUI gerenciadorUI;
    public PainelFatiado visualJanela;
    public PainelFatiado visualBotao;

    public float escalaPixel = 4.0f;

    public CaixaDialogo dialogoConfirmacao;
    public CaixaDialogo dialogoEntrada;
    public CampoTexto campoNome;
    public Rotulo rotuloResposta;

    public void show() {
        pincel = new SpriteBatch();
        pincelFormas = new ShapeRenderer();
        fonte = new BitmapFont();
        fonte.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        camera = new OrthographicCamera();
        vista = new ScreenViewport(camera);
        vista.apply(true);

        gerenciadorUI = new GerenciadorUI();

        try {
            Texture textura = new Texture(Gdx.files.internal("ui/teste.png"));
            textura.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            visualJanela = new PainelFatiado(textura);
            visualBotao = new PainelFatiado(textura);

            criarInterface();
        } catch(Exception e) {
            Gdx.app.log("ERRO", "Recursos nao encontrados: " + e.getMessage());
        }
        Gdx.input.setInputProcessor(this);
    }

    public void criarInterface() {
        final float larguraTela = Gdx.graphics.getWidth();
        final float alturaTela = Gdx.graphics.getHeight();

        Janela janelaPrincipal = new Janela(visualJanela, -300, -150, 600, 300, escalaPixel);

        Rotulo titulo = new Rotulo("DEMO", fonte, escalaPixel);
        titulo.x = 0;
        titulo.y = 250;
        titulo.largura = 600;
        titulo.altura = 40;
        janelaPrincipal.add(titulo);

        Painel painelBotoes = new Painel(10, 10, 580, 200);
        painelBotoes.defEspaco(10);

        Acao acaoDialogo = new Acao() {
            public void exec() {
                dialogoConfirmacao.centralizar(larguraTela, alturaTela);
                CaixaDialogo.Fechar callback = new CaixaDialogo.Fechar() {
                    public void aoFechar(boolean confirmou) {
                        if(confirmou) {
                            Gdx.app.log("INFO", "Usuario confirmou!");
                            rotuloResposta.texto = "CONFIRMOU";
                        } else {
                            Gdx.app.log("INFO", "Usuario cancelou");
                            rotuloResposta.texto = "CANCELOU";
                        }
                    }
                };
                dialogoConfirmacao.mostrar("Confirma?", "Deseja sair?", callback);
            }
        };
        Botao botaoDialogo = new Botao("Dialogo", visualBotao, fonte, 0, 0, 180, 50, escalaPixel, acaoDialogo);
        painelBotoes.addAncorado(botaoDialogo, Ancoragem.SUPERIOR_ESQUERDO, 0, 0);

        Acao acaoInput = new Acao() {
            public void exec() {
                dialogoEntrada.centralizar(larguraTela, alturaTela);
                dialogoEntrada.mostrar("Nome", "Digite:", null);
				Gdx.input.setOnscreenKeyboardVisible(true);
            }
        };

        Botao botaoInput = new Botao("Entrada", visualBotao, fonte, 0, 0, 180, 50, escalaPixel, acaoInput);
        painelBotoes.addAncorado(botaoInput, Ancoragem.SUPERIOR_DIREITO, 0, 0);

        Acao acaoTeste = new Acao() {
            public void exec() {
                rotuloResposta.texto = "Clicado!";
                Gdx.app.log("INFO", "Botao teste clicado!");
            }
        };
        Botao botaoTeste = new Botao("Teste", visualBotao, fonte, 0, 0, 180, 50, escalaPixel, acaoTeste);
        painelBotoes.addAncorado(botaoTeste, Ancoragem.CENTRO, 0, 0);

        Acao acaoLimpar = new Acao() {
            public void exec() {
                rotuloResposta.texto = "...";
                campoNome.texto = "";
            }
        };
        Botao botaoLimpar = new Botao("Limpar", visualBotao, fonte, 0, 0, 120, 50, escalaPixel, acaoLimpar);
        painelBotoes.addAncorado(botaoLimpar, Ancoragem.INFERIOR_ESQUERDO, 0, 0);

        rotuloResposta = new Rotulo("...", fonte, escalaPixel * 0.7f);
        rotuloResposta.largura = 350;
        rotuloResposta.altura = 50;
        painelBotoes.addAncorado(rotuloResposta, Ancoragem.INFERIOR_DIREITO, 0, 0);

        janelaPrincipal.add(painelBotoes);
        gerenciadorUI.add(janelaPrincipal);

        dialogoConfirmacao = new CaixaDialogo(visualJanela, fonte, escalaPixel, pincelFormas);
        dialogoConfirmacao.addOk(visualBotao);
        dialogoConfirmacao.addCancelar(visualBotao);
        gerenciadorUI.addDialogo(dialogoConfirmacao);

        dialogoEntrada = new CaixaDialogo(visualJanela, fonte, escalaPixel, pincelFormas);
        dialogoEntrada.largura = 500;
        dialogoEntrada.altura = 300;

        campoNome = new CampoTexto(visualBotao, fonte, 50, 120, 400, 50, escalaPixel);
        campoNome.padrao = "Digite...";
        campoNome.limiteCaracteres = 20;

			campoNome.mudanca = new CampoTexto.Texto() {
            public void aoMudar(String novoTexto) {
                Gdx.app.log("INPUT", "Texto: " + novoTexto);
            }
        };
        dialogoEntrada.add(campoNome);

        Acao acaoConfirmar = new Acao() {
            public void exec() {
                String nome = campoNome.texto;
                if(!nome.isEmpty()) {
                    rotuloResposta.texto = "Ola " + nome;
                    Gdx.app.log("INFO", "Nome: " + nome);
                } else {
                    rotuloResposta.texto = "Vazio!";
                }
                dialogoEntrada.aoTocar(0, 0, false);
            }
        };
        dialogoEntrada.adicionarBotao("OK", visualBotao, Ancoragem.CENTRO_DIREITO, -10, acaoConfirmar);

        Acao acaoCancelar = new Acao() {
            public void exec() {
                campoNome.texto = "";
                rotuloResposta.texto = "Cancelado";
            }
        };
        dialogoEntrada.adicionarBotao("Cancel", visualBotao, Ancoragem.CENTRO_ESQUERDO, 10, acaoCancelar);

        gerenciadorUI.addDialogo(dialogoEntrada);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        pincel.setProjectionMatrix(camera.combined);
        pincelFormas.setProjectionMatrix(camera.combined);

        pincel.begin();
        gerenciadorUI.desenhar(pincel, delta);
        pincel.end();
    }
	@Override
    public boolean touchDown(int x, int y, int p, int b) {
        camera.unproject(toqueAuxiliar.set(x, y, 0));
        gerenciadorUI.processarToque(toqueAuxiliar.x, toqueAuxiliar.y, true);
        return true;
    }
	@Override
    public boolean touchUp(int x, int y, int p, int b) {
        camera.unproject(toqueAuxiliar.set(x, y, 0));
        gerenciadorUI.processarToque(toqueAuxiliar.x, toqueAuxiliar.y, false);
        return true;
    }
	@Override
    public boolean touchDragged(int x, int y, int p) {
        camera.unproject(toqueAuxiliar.set(x, y, 0));
        gerenciadorUI.processarArraste(toqueAuxiliar.x, toqueAuxiliar.y);
        return true;
    }
	@Override
    public boolean keyDown(int c) {
        return gerenciadorUI.processarTecla(c);
    }
	@Override
    public boolean keyTyped(char c) {
        return gerenciadorUI.processarCaractere(c);
    }
	@Override
    public void resize(int v, int h) {
        vista.update(v, h);
    }
	@Override
    public void dispose() {
        pincel.dispose();
        pincelFormas.dispose();
        fonte.dispose();
    }
	@Override
    public void pause() {}
    public void resume() {}
    public void hide() {}
    public boolean keyUp(int k) { return false; }
    public boolean mouseMoved(int x, int y) { return false; }
    public boolean scrolled(float a, float b) { return false; }
}

