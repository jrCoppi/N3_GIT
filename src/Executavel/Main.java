package Executavel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import Outros.Cor;
import Padrao.Point4D;
import Principal.Mundo;
import Principal.ObjetoGrafico;

//import Padrao.BoundingBox;
//import Padrao.Point4D;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private int antigoX, antigoY = 0;
	private double posicaoX = 0, posicaoY = 0;
	
	//Inicia opengl
	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	 
	//exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		glu.gluOrtho2D(0, 400, 400, 0);
		SRU();
		
		//Desenha a tela no mundo
		Mundo.getInstance().desenhaTela(gl);
		
		gl.glFlush();
	}	

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_P: 
				//Troca a primitiva atual
				if(Mundo.getInstance().isListaVazia()){
					return ;
				}
				
				Mundo.getInstance().trocaPrimitiva();
	    		glDrawable.display();
				break;
			case KeyEvent.VK_V: 
	
				if(Mundo.getInstance().listaPoligonos.get(Mundo.getInstance().listaPoligonos.size()-1).getListaPontos().size() <= 2){
					return ;
				}
				Mundo.getInstance().listaPoligonos.get(Mundo.getInstance().listaPoligonos.size()-1).removePonto();
				
	    		glDrawable.display();
				break;
			case KeyEvent.VK_C:
				if (!Mundo.getInstance().alterarCor)
					Mundo.getInstance().alterarCor = true;
				break;
			case KeyEvent.VK_F:
				Mundo.getInstance().inserirRaiz = !Mundo.getInstance().inserirRaiz;
				break;
				
			//Cores
			case KeyEvent.VK_1:
				Mundo.getInstance().setCor(Cor.VERMELHO);
				break;
			case KeyEvent.VK_2:
				Mundo.getInstance().setCor(Cor.VERDE);
				glDrawable.display();
				break;
			case KeyEvent.VK_3:
				Mundo.getInstance().setCor(Cor.AZUL);
				glDrawable.display();
				break;
			case KeyEvent.VK_4:
				Mundo.getInstance().setCor(Cor.AMARELO);
				glDrawable.display();
				break;
			case KeyEvent.VK_5:
				Mundo.getInstance().setCor(Cor.ROXO);
				glDrawable.display();
				break;
			case KeyEvent.VK_6:
				Mundo.getInstance().setCor(Cor.MARROM);
				glDrawable.display();
				break;
			case KeyEvent.VK_7:
				Mundo.getInstance().setCor(Cor.LARANJA);
				glDrawable.display();
				break;
			case KeyEvent.VK_8:
				Mundo.getInstance().setCor(Cor.BRANCO);
				glDrawable.display();
				break;
			case KeyEvent.VK_9:
				Mundo.getInstance().setCor(Cor.PRETO);
				glDrawable.display();
				break;
				
			
			//Movimentos em tela	
			case KeyEvent.VK_E:
				Mundo.getInstance().getPolignoSelecionado().exibeVertices();
				break;
			case KeyEvent.VK_M:
				Mundo.getInstance().getPolignoSelecionado().exibeMatriz();
				break;

			case KeyEvent.VK_R:
				Mundo.getInstance().getPolignoSelecionado().atribuirIdentidade();
				break;

			case KeyEvent.VK_RIGHT:
				Mundo.getInstance().getPolignoSelecionado().translacaoXYZ(2.0,0.0,0.0);
				break;
			case KeyEvent.VK_LEFT:
				Mundo.getInstance().getPolignoSelecionado().translacaoXYZ(-2.0,0.0,0.0);
				break;
			case KeyEvent.VK_UP:
				Mundo.getInstance().getPolignoSelecionado().translacaoXYZ(0.0,2.0,0.0);
				break;
			case KeyEvent.VK_DOWN:
				Mundo.getInstance().getPolignoSelecionado().translacaoXYZ(0.0,-2.0,0.0);
				break;

			case KeyEvent.VK_PAGE_UP:
				Mundo.getInstance().getPolignoSelecionado().escalaXYZ(2.0,2.0);
				break;
			case KeyEvent.VK_PAGE_DOWN:
				Mundo.getInstance().getPolignoSelecionado().escalaXYZ(0.5,0.5);
				break;

			case KeyEvent.VK_F1:
				Mundo.getInstance().getPolignoSelecionado().escalaXYZPtoFixo(0.5, new Point4D(-15.0,-15.0,0.0,0.0));
				break;
				
			case KeyEvent.VK_F2:
				Mundo.getInstance().getPolignoSelecionado().escalaXYZPtoFixo(2.0, new Point4D(-15.0,-15.0,0.0,0.0));
				break;
				
			case KeyEvent.VK_F3:
				Mundo.getInstance().getPolignoSelecionado().rotacaoZPtoFixo(10.0, new Point4D(-15.0,-15.0,0.0,0.0));
				break;
		}
	}
	
	//MATRIZ
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
		System.out.println(width);
		System.out.println(height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}
	
	public void mouseEntered(MouseEvent e) {}
	  
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}
	    
	public void mouseReleased(MouseEvent e) {}
	    
	//trabalhando com a bbox no ponto incial por enquanto
	public void mouseClicked(MouseEvent e) {
		
		//Bot�o direito
		if(e.getButton() == 3){
			Mundo.getInstance().modo = 1;
			glDrawable.display();
			return ;
		}
		
		//Chegou, bot�o esquerdo
		Point4D novoPonto = new Point4D(e.getX(),  e.getY(), 0, 1);
		
		if(Mundo.getInstance().modo == 1){
			
			//Cria uma bbox e adiciona na lista
			ObjetoGrafico poligono = new ObjetoGrafico();
			poligono.criabBox(novoPonto);
			
			
			if((!Mundo.getInstance().isListaVazia()) && (!Mundo.getInstance().inserirRaiz)){
				Mundo.getInstance().addObjGraficoFilho(poligono);
			} else {
				Mundo.getInstance().addObjGrafico(poligono);
			}
			/*
			//Se for inserir raiz reseta os selecionados e seta o novo como selecionado
			if(this.inserirRaiz){
				this.atualizaSelecionado();
				this.arrSelecionado[0] = this.listaPoligonos.size()-1;
			} else {
				
			}*/
			
		}
		//Arrumar para ver qual o oligono sendo editado (pai ou filho) e editar o mesmo e n�o sempre o pai
		//Atualiza bbox e a lista de pontos
		Mundo.getInstance().atualizabBox(novoPonto);
		Mundo.getInstance().addPonto(novoPonto);	
		
		Mundo.getInstance().modo = 2;
		
		glDrawable.display();
	}
	    
	public void mouseDragged(MouseEvent e) {}
	    
	//Quando o mouse � motivo atualiza o x do rastro
	//sempre atualiza para zero, caso esta em edi��o usa a posi��o do mouse
	public void mouseMoved(MouseEvent e) {

		if(Mundo.getInstance().isListaVazia()){
			return ;
		}
		
		int xRastro = 0;
		int yRastro = 0;
		if(Mundo.getInstance().modo == 2){
			xRastro = e.getX();
			yRastro = e.getY();
		}
		
		Mundo.getInstance().setxRastro(xRastro);
		Mundo.getInstance().setyRastro(yRastro);
		
		glDrawable.display();
	}
	
	public void SRU() {
		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin( GL.GL_LINES );
			gl.glVertex2f( -200.0f, 0.0f );
			gl.glVertex2f(  200.0f, 0.0f );
			gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin( GL.GL_LINES);
			gl.glVertex2f(  0.0f, -200.0f);
			gl.glVertex2f(  0.0f, 200.0f );
		gl.glEnd();
	}

}
