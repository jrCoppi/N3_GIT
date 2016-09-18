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


	public void addPonto(Point4D ponto) {
		this.listaPontos.add(ponto);
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

	//Cria bBox criando um espa�o de -100 at� +100 do x/y atual
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
