package Principal;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import Outros.Cor;
import Padrao.BoundingBox;
import Padrao.Point4D;

public class ObjetoGrafico {

	private List<Point4D> listaPontos;
	private int primitiva;
	private BoundingBox bBox; 
	private Cor cor;
	
	public ObjetoGrafico() {
		this.listaPontos = new ArrayList<Point4D>();
		this.primitiva = 0;
		this.bBox = null;
		this.cor = new Cor();
	}
	
	public List<Point4D> getListaPontos() {
		return listaPontos;
	}

	//Sempre que adicionar um ponto substitui o ultimo da lista e adiciona uma copia
	public void addPonto(double posicaoX, double posicaoY) {
		Point4D novoPonto = new Point4D(posicaoX, posicaoY, 0, 1);
		
		
		if(this.listaPontos.size() == 0){
			this.listaPontos.add(novoPonto);
			this.listaPontos.add(novoPonto);
			return ;
		}
		
		//Substitui o ultimo atual
		this.listaPontos.set(this.listaPontos.size()-1, novoPonto);
		
		//Adiciona a copia
		this.listaPontos.add(novoPonto);
	}

	public int getPrimitiva() {
		return primitiva;
	}

	public void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}

	public BoundingBox getbBox() {
		return bBox;
	}

	//Cria bBox criando um espaço de -100 até +100 do x/y atual
	public void criabBox(double x, double y) {
		double invervalo = 100;
		double smallerX = x - invervalo;
		double smallerY = y - invervalo;
		double smallerZ = 0;
		double greaterZ = 0;
		double greaterX = x + invervalo;
		double greaterY = y + invervalo; 
		
		
		this.bBox = new BoundingBox(smallerX,smallerY,smallerZ,greaterX,greaterY,greaterZ);
		this.bBox.processarCentroBBox();
	}
	
	public void mostrabBox(GL gl){
		this.bBox.desenharOpenGLBBox(gl);
	}

	public Cor getCor() {
		return cor;
	}

	private void trocaPrimitiva(){
		
	}
}
