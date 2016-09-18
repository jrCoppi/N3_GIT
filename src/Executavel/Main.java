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
	private boolean testeUmPoligono = false ;
	private List<ObjetoGrafico> listaPoligonos;
	
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
		glu.gluOrtho2D(-400.0f, 400.0f, -400.0f, 400.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		

		SRU();

		System.out.println(this.posicaoX);
		System.out.println(this.posicaoY);
		
		if(!this.listaPoligonos.isEmpty()){
			this.listaPoligonos.get(this.listaPoligonos.size()-1).mostrabBox(this.gl);
		}
		
		gl.glFlush();
	}	

	private void recalculaBBox () {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}
	
	public void mouseEntered(MouseEvent e) {
		 System.out.println("4");
	}
	  
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if(this.testeUmPoligono == true){
		    int movtoX = e.getX() - antigoX;
		    int movtoY = e.getY() - antigoY;
		    this.posicaoX += movtoX;
		    this.posicaoY -= movtoY;
		    System.out.println("1");
		    
		    glDrawable.display();
		   
		    this.antigoX = e.getX();
		    this.antigoY = e.getY();
	
			glDrawable.display();
		}
	}
	    
	public void mouseReleased(MouseEvent e) {}
	    
	//trabalhando com a bbox no ponto incial por enquanto
	public void mouseClicked(MouseEvent e) {
		
		//Cria uma bbox e adiciona na lista
		if(this.testeUmPoligono == false){
			ObjetoGrafico poligono = new ObjetoGrafico();
			poligono.criabBox(this.posicaoX, this.posicaoY);
			this.listaPoligonos.add(poligono);
		}
		
		//Cria dois pontos na mesma posição
		this.listaPoligonos.get(0).addPonto(this.posicaoX, this.posicaoY);	
		
		this.testeUmPoligono = true; 
		glDrawable.display();
	}
	    
	public void mouseDragged(MouseEvent e) {
	}
	    
	public void mouseMoved(MouseEvent e) {}
	
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
