package Principal;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import Outros.Cor;
import Padrao.BoundingBox;
import Padrao.Point4D;

public class ObjetoGrafico {

	private List<Point4D> listaPontos;
	//3 - Line Strip, 2 - Line Loop
	private int primitiva;
	private BoundingBox bBox; 
	private Cor cor;
	private double xRastro,yRastro;
	
	public ObjetoGrafico() {
		this.listaPontos = new ArrayList<Point4D>();
		this.primitiva = 2;
		this.bBox = null;
		this.cor = new Cor();
		this.xRastro = 0;
		this.yRastro = 0;
	}
	
	public int getTamanhoLista() {
		return this.getListaPontos().size();
	}

	public List<Point4D> getListaPontos() {
		return listaPontos;
	}


	//Sempre que adicionar um ponto substitui o ultimo da lista e adiciona uma copia
	public void addPonto(Point4D ponto) {

		
		if(this.listaPontos.size() == 0){
			this.listaPontos.add(ponto);
			this.listaPontos.add(ponto);
			return ;
		}
		
		//Substitui o ultimo atual
		this.listaPontos.set(this.listaPontos.size()-1, ponto);
		
		//Adiciona a copia
		this.listaPontos.add(ponto);
		
		//Atualiza os pontos de rasto
		this.xRastro = 0;
		this.yRastro = 0;
	}
	
	//Remove os dois ultimos pontos e duplica o anteior a eles
	public void removePonto(){
		//remove os 2
		this.listaPontos.remove(this.listaPontos.size()-1);
		this.listaPontos.remove(this.listaPontos.size()-1);
		
		//duplica o anterior
		this.listaPontos.add(this.listaPontos.get(this.listaPontos.size()-1));
		
		//Atualiza os pontos de rasto
		this.xRastro = 0;
		this.yRastro = 0;
	}

	public int getPrimitiva() {
		return primitiva;
	}

	private void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}

	public BoundingBox getbBox() {
		return bBox;
	}

	public void criabBox(Point4D ponto) {
		this.bBox = new BoundingBox(ponto.GetX(),ponto.GetY(),ponto.GetZ(),ponto.GetX(),ponto.GetY(),ponto.GetZ());
		this.atualizabBox(ponto);
	}
	
	public void atualizabBox(Point4D ponto){
		this.bBox.atualizarBBox(ponto);
	}
	
	public void mostrabBox(GL gl){
		this.bBox.desenharOpenGLBBox(gl);
	}

	public Cor getCor() {
		return cor;
	}

	public void trocaPrimitiva(){
		if(this.getPrimitiva() == 3){
			this.setPrimitiva(2);
		} else {
			this.setPrimitiva(3);
		}
	}

	public double getxRastro() {
		return xRastro;
	}

	public void setxRastro(double xRastro) {
		this.xRastro = xRastro;
	}

	public double getyRastro() {
		return yRastro;
	}

	public void setyRastro(double yRastro) {
		this.yRastro = yRastro;
	}
}
