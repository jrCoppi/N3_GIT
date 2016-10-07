package Executavel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import Outros.Cor;
import Padrao.Point4D;
import Principal.ObjetoGrafico;

//import Padrao.BoundingBox;
//import Padrao.Point4D;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private int antigoX, antigoY = 0;
	private double posicaoX = 0, posicaoY = 0;
	private List<ObjetoGrafico> listaPoligonos;
	
	
	//Modos 1 - Inserir, 2 = Editar
	private Integer modo = 1;
	
	//Inicia opengl
	public void init(GLAutoDrawable drawable) {
		this.listaPoligonos = new ArrayList<ObjetoGrafico>();
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
		//enquanto movimentar o mouse vai desenhando do ultimo ponto até a posição do mouse
		
		if(!this.listaPoligonos.isEmpty()){
			this.listaPoligonos.get(this.listaPoligonos.size()-1).mostrabBox(this.gl);
			
			//Desenha Pontos
			for (int i = 0; i < this.listaPoligonos.size(); i++) {
				ArrayList<Point4D> listaPontosAtual = (ArrayList<Point4D>) this.listaPoligonos.get(i).getListaPontos();
				
				float[] cor = listaPoligonos.get(i).getCor();
				gl.glColor3f(cor[0],cor[1],cor[2]);				
				//Verifica qual primitiva usar
				if(this.listaPoligonos.get(i).getPrimitiva() == 2){
					gl.glBegin(GL.GL_LINE_LOOP);
				} else {
					gl.glBegin(GL.GL_LINE_STRIP);
				}
				
					for ( Point4D ponto : listaPontosAtual) {
						gl.glVertex2d(ponto.GetX(), ponto.GetY());
					}
					
					if((this.listaPoligonos.get(i).getxRastro() > 0) && (this.modo == 2)){
						gl.glVertex2d(this.listaPoligonos.get(i).getxRastro(), this.listaPoligonos.get(i).getyRastro());
					}
					
				gl.glEnd();
			}
		}
		
		gl.glFlush();
	}	
private boolean alterarCor=false;

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_P: 
				//Troca a primitiva atual
				if(this.listaPoligonos.size() == 0){
					return ;
				}
				
				this.listaPoligonos.get(this.listaPoligonos.size()-1).trocaPrimitiva();
	    		glDrawable.display();
				break;
			case KeyEvent.VK_V: 
	
				if(this.listaPoligonos.get(this.listaPoligonos.size()-1).getListaPontos().size() <= 2){
					return ;
				}
				this.listaPoligonos.get(this.listaPoligonos.size()-1).removePonto();
				
	    		glDrawable.display();
				break;
		case KeyEvent.VK_C:
			if (!alterarCor)
				alterarCor = true;
			break;
		case KeyEvent.VK_1:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.VERMELHO);
			alterarCor = false;
			break;
		case KeyEvent.VK_2:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.VERDE);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_3:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.AZUL);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_4:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.AMARELO);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_5:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.ROXO);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_6:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.MARROM);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_7:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.LARANJA);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_8:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.BRANCO);
			alterarCor = false;
			glDrawable.display();
			break;
		case KeyEvent.VK_9:
			if (alterarCor)
				getPolignoSelecionado().setCor(Cor.PRETO);
			alterarCor = false;
			glDrawable.display();
			break;
				
				
				
				
	    		
				
		}
	}
	private ObjetoGrafico getPolignoSelecionado()
	{
		for (ObjetoGrafico objetoGrafico : listaPoligonos) {
			if(objetoGrafico.isSelecionado())
				return objetoGrafico;
		}
		return listaPoligonos.get(listaPoligonos.size()-1); // nao encontrou um selecionado entao retorna o ultimo;
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
	
	public void mouseEntered(MouseEvent e) {
	}
	  
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
	}
	    
	public void mouseReleased(MouseEvent e) {}
	    
	//trabalhando com a bbox no ponto incial por enquanto
	public void mouseClicked(MouseEvent e) {
		
		//Botão direito
		if(e.getButton() == 3){
			this.modo = 1;
			glDrawable.display();
			return ;
		}
		
		//Chegou, botão esquerdo
		Point4D novoPonto = new Point4D(e.getX(),  e.getY(), 0, 1);
		
		if(this.modo == 1){
			
			//Cria uma bbox e adiciona na lista
			ObjetoGrafico poligono = new ObjetoGrafico();
			poligono.criabBox(novoPonto);
			this.listaPoligonos.add(poligono);
		}
		
		//Atualiza bbox e a lista de pontos
		this.listaPoligonos.get(this.listaPoligonos.size()-1).atualizabBox(novoPonto);
		this.listaPoligonos.get(this.listaPoligonos.size()-1).addPonto(novoPonto);	
		
		this.modo = 2;
		
		glDrawable.display();
	}
	    
	public void mouseDragged(MouseEvent e) {

	}
	    
	//Quando o mouse é motivo atualiza o x do rastro
	//sempre atualiza para zero, caso esta em edição usa a posição do mouse
	public void mouseMoved(MouseEvent e) {

		if((this.listaPoligonos == null) || (this.listaPoligonos.size() == 0)){
			return ;
		}
		
		int xRastro = 0;
		int yRastro = 0;
		if(this.modo == 2){
			xRastro = e.getX();
			yRastro = e.getY();
		}
		
		this.listaPoligonos.get(this.listaPoligonos.size()-1).setxRastro(xRastro);
		this.listaPoligonos.get(this.listaPoligonos.size()-1).setyRastro(yRastro);
		
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
