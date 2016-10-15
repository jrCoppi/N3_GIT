package Executavel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import Outros.Cor;
import Padrao.Point4D;
import Principal.Mundo;

//import Padrao.BoundingBox;
//import Padrao.Point4D;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private int antigoX, antigoY = 0;
	private double posicaoX = 0, posicaoY = 0;
	
	/* Iniciar
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	 
	
	/* Mostra
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */
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

	
	/* Ao apertar mouse
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
			//Ações
			case KeyEvent.VK_P: 
				//Troca a primitiva atual
				Mundo.getInstance().trocaPrimitiva();
				break;
			case KeyEvent.VK_V: 
				Mundo.getInstance().removePonto();
				break;
			case KeyEvent.VK_DELETE: 
				Mundo.getInstance().removerPoligono();
				break;
			case KeyEvent.VK_C:
				if (!Mundo.getInstance().alterarCor)
					Mundo.getInstance().alterarCor = true;
				break;
			case KeyEvent.VK_F:
				System.out.println("f");
				Mundo.getInstance().inserirRaiz = !Mundo.getInstance().inserirRaiz;
				break;
				
			//Cores
			case KeyEvent.VK_1:
				Mundo.getInstance().setCor(Cor.VERMELHO);
				break;
			case KeyEvent.VK_2:
				Mundo.getInstance().setCor(Cor.VERDE);
				break;
			case KeyEvent.VK_3:
				Mundo.getInstance().setCor(Cor.AZUL);
				break;
			case KeyEvent.VK_4:
				Mundo.getInstance().setCor(Cor.AMARELO);
				break;
			case KeyEvent.VK_5:
				Mundo.getInstance().setCor(Cor.ROXO);
				break;
			case KeyEvent.VK_6:
				Mundo.getInstance().setCor(Cor.MARROM);
				break;
			case KeyEvent.VK_7:
				Mundo.getInstance().setCor(Cor.LARANJA);
				break;
			case KeyEvent.VK_8:
				Mundo.getInstance().setCor(Cor.BRANCO);
				break;
			case KeyEvent.VK_9:
				Mundo.getInstance().setCor(Cor.PRETO);
				break;
				
			
			//Mostrar
			case KeyEvent.VK_E:
				Mundo.getInstance().getPolignoSelecionado(false).exibeVertices();
				break;
			case KeyEvent.VK_M:
				Mundo.getInstance().getPolignoSelecionado(false).exibeMatriz();
				break;
				
			//identidade
			case KeyEvent.VK_R:
				Mundo.getInstance().getPolignoSelecionado(false).atribuirIdentidade();
				break;
			
			//Translação
			case KeyEvent.VK_RIGHT:
				Mundo.getInstance().getPolignoSelecionado(false).translacaoXYZ(2.0,0.0,0.0);
				break;
			case KeyEvent.VK_LEFT:
				Mundo.getInstance().getPolignoSelecionado(false).translacaoXYZ(-2.0,0.0,0.0);
				break;
			case KeyEvent.VK_UP:
				Mundo.getInstance().getPolignoSelecionado(false).translacaoXYZ(0.0,2.0,0.0);
				break;
			case KeyEvent.VK_DOWN:
				Mundo.getInstance().getPolignoSelecionado(false).translacaoXYZ(0.0,-2.0,0.0);
				break;
			
			//Escala
			case KeyEvent.VK_PAGE_UP:
				Mundo.getInstance().getPolignoSelecionado(false).escalaXYZ(2.0,2.0);
				break;
			case KeyEvent.VK_PAGE_DOWN:
				Mundo.getInstance().getPolignoSelecionado(false).escalaXYZ(0.5,0.5);
				break;
			//Fixo
			case KeyEvent.VK_F3:
				Point4D ponto = Mundo.getInstance().getPolignoSelecionado(false).bBox.obterCentro();
				ponto.inverterSinal(ponto);
				Mundo.getInstance().getPolignoSelecionado(false).escalaXYZPtoFixo(0.5, ponto);
				break;
				
			case KeyEvent.VK_F5:
				Point4D ponto2 = Mundo.getInstance().getPolignoSelecionado(false).bBox.obterCentro();
				ponto2.inverterSinal(ponto2);
				Mundo.getInstance().getPolignoSelecionado(false).escalaXYZPtoFixo(2.0, ponto2);
				break;

			//Rotação
			case KeyEvent.VK_F1:
				Point4D ponto3 = Mundo.getInstance().getPolignoSelecionado(false).bBox.obterCentro();
				ponto3.inverterSinal(ponto3);
				Mundo.getInstance().getPolignoSelecionado(false).rotacaoZPtoFixo(10.0, ponto3);
				break;
			case KeyEvent.VK_F2:
				Point4D ponto4 = Mundo.getInstance().getPolignoSelecionado(false).bBox.obterCentro();
				ponto4.inverterSinal(ponto4);
				Mundo.getInstance().getPolignoSelecionado(false).rotacaoZPtoFixo(-10.0, ponto4);
				break;
				
			case KeyEvent.VK_F4:
				System.out.println("F4");
				 if(Mundo.getInstance().modoSelecao){
					Mundo.getInstance().modoSelecao = false;
					Mundo.getInstance().objSelecionadoMomento = null;
				 }
				 else {
					 Mundo.getInstance().modoSelecao = true;
					 Mundo.getInstance().modo = 1;
				 }
				break;
		}
		glDrawable.display();
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
	    
	
	/* Ao clicar no mouse
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		
		//Botão direito
		if(e.getButton() == 3){
			Mundo.getInstance().modo = 1;
			glDrawable.display();
			return ;
		}
		
		Mundo.getInstance().mouseClique(e.getX(), e.getY());
		
		glDrawable.display();
	}
	    
	public void mouseDragged(MouseEvent e) {}
	    
	
	/* 
	 * Quando o mouse é motivo atualiza o x do rastro
	 * sempre atualiza para zero, caso esta em edição usa a posição do mouse
	 * @param java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
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
