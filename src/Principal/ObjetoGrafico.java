package Principal;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import Outros.Cor;
import Padrao.BoundingBox;
import Padrao.Point4D;
import Padrao.Transformacao4D;

public class ObjetoGrafico {

	private List<Point4D> listaPontos;
	private float tamanho = 2.0f;
	public BoundingBox bBox; 
	private float[] cor;
	private double xRastro,yRastro;
	private boolean selecionado;
	private int primitiva;
	private List<ObjetoGrafico> filhos;
	private List<ObjetoGrafico> listaIrmaos;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private static Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private static Transformacao4D matrizTmpEscala = new Transformacao4D();	
	private static Transformacao4D matrizGlobal = new Transformacao4D();
	GL gl;
	
	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public ObjetoGrafico() {
		this.listaPontos = new ArrayList<Point4D>();
		this.primitiva =  GL.GL_LINE_STRIP;
		this.bBox = null;
		this.cor = Cor.PRETO;
		this.xRastro = 0;
		this.yRastro = 0;
		this.setFilhos(new ArrayList<ObjetoGrafico>());
		this.setListaIrmaos(new ArrayList<ObjetoGrafico>());
	}
	
	public void setListaPontos(List<Point4D> listaPontos) {
		this.listaPontos = listaPontos;
	}

	public void atribuirGL(GL gl) {
		this.gl = gl;
	}
	
	public int getTamanhoLista() {
		return this.getListaPontos().size();
	}

	public List<Point4D> getListaPontos() {
		return listaPontos;
	}

	
	/**
	 * Verifica se clicamos dentro da bbox
	 * @param x - x mouse
	 * @param y - y mouse
	 * @return
	 */
	public boolean cliqueEstaNaBBox(int x,int  y) {
		
		if ((bBox.obterMaiorX() > x && bBox.obterMenorX() < x) && (bBox.obterMaiorY() > y && bBox.obterMenorY() < y))
			return true;
		
		return false;
	}
	
	
	public void removeUltimoPonto(){
		//remove os 2
		this.listaPontos.remove(this.listaPontos.size()-1);
	}
	
	
	/**
	 * Sempre que adicionar um ponto substitui o ultimo da lista e adiciona uma copia
	 * @param ponto - ponto novo
	 */
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
	
	
	/**
	 * Remove os dois ultimos pontos e duplica o anteior a eles
	 */
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
	
	
	/**
	 * Desenha os elementos deste objeto em tela
	 * @param modo - modo da tela
	 */
	public void desenha(Integer modo) {
		float[] cor = this.getCor();
		this.gl.glColor3f(cor[0],cor[1],cor[2]);	
		this.gl.glLineWidth(tamanho);
		this.gl.glPointSize(tamanho);

		this.gl.glPushMatrix();
		this.gl.glMultMatrixd(matrizObjeto.GetDate(), 0);
		this.gl.glBegin(this.getPrimitiva());
		
			for ( Point4D ponto : this.getListaPontos()) {
				this.gl.glVertex2d(ponto.GetX(), ponto.GetY());
			}
			
			if((this.getxRastro() > 0) && (modo == 2)){
				this.gl.glVertex2d(this.getxRastro(), this.getyRastro());
			}
		this.gl.glEnd();
		
		this.gl.glPopMatrix();
	}
	
	/**
	 * Faz a translação em XYZ
	 * @param tx
	 * @param ty
	 * @param tz
	 */
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(tx,ty,tz);
		matrizObjeto = matrizTranslate.transformMatrix(matrizObjeto);	
		
		this.translacaoFilhos(tx, ty, tz, this);
	}
	
	
	/**
	 * Faz a translação dos filhos em xyz
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param objetoPai
	 */
	private void translacaoFilhos(double tx, double ty, double tz, ObjetoGrafico objetoPai){
		for (ObjetoGrafico objeto : objetoPai.getFilhos()) {
			objeto.translacaoXYZ(tx, ty, tz);
		}
	}

	
	/**
	 * Faz a escala em xy
	 * @param Sx
	 * @param Sy
	 */
	public void escalaXYZ(double Sx,double Sy) {
		Transformacao4D matrizScale = new Transformacao4D();		
		matrizScale.atribuirEscala(Sx,Sy,1.0);
		matrizObjeto = matrizScale.transformMatrix(matrizObjeto);
		
		this.escalaFilhos(Sx,Sy,this);
	}
	
	/**
	 * Escala os filhos em xy
	 * @param Sx
	 * @param Sy
	 * @param objetoPai
	 */
	private void escalaFilhos(double Sx,double Sy, ObjetoGrafico objetoPai) {
		for (ObjetoGrafico objeto : objetoPai.getFilhos()) {
			objeto.escalaXYZ(Sx, Sy);
		}
	}
	
	public void atribuirIdentidade() {
		matrizObjeto.atribuirIdentidade();
	}
	
	
	/**
	 * Escala em xyz para centro da bbox
	 * @param escala
	 * @param ptoFixo
	 */
	public void escalaXYZPtoFixo(double escala, Point4D ptoFixo) {
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.GetX(),ptoFixo.GetY(),ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		//
		matrizTmpEscala.atribuirEscala(escala, escala, 1.0);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.GetX(),ptoFixo.GetY(),ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);
		//
		
		matrizObjeto = matrizObjeto.transformMatrix(matrizGlobal);
		
		this.escalaFixaFilho(escala, this);
	}
	
	/**
	 * Escala os filhos para ponto fixo
	 * @param escala
	 * @param objetoPai
	 */
	public void escalaFixaFilho(double escala, ObjetoGrafico objetoPai){
		for (ObjetoGrafico objeto : objetoPai.getFilhos()) {
			Point4D ponto = objeto.bBox.obterCentro();
			ponto.inverterSinal(ponto);
			objeto.escalaXYZPtoFixo(escala, ponto);
		}
	}
	
	/**
	 * Rotaciona para o centro da bbox
	 * @param angulo
	 * @param ptoFixo
	 */
	public void rotacaoZPtoFixo(double angulo, Point4D ptoFixo) {
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.GetX(),ptoFixo.GetY(),ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirRotacaoZ(Transformacao4D.DEG_TO_RAD * angulo);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.GetX(),ptoFixo.GetY(),ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		matrizObjeto = matrizObjeto.transformMatrix(matrizGlobal);
		
		this.rotacaoFilho(angulo, this);
	}

	/**
	 * Rotaciona os filhos para um ponto fixo
	 * @param angulo
	 * @param objetoPai
	 */
	public void rotacaoFilho(double angulo, ObjetoGrafico objetoPai){
		for (ObjetoGrafico objeto : objetoPai.getFilhos()) {
			Point4D ponto = objeto.bBox.obterCentro();
			ponto.inverterSinal(ponto);
			objeto.rotacaoZPtoFixo(angulo, ponto);
		}
	}
	
	public void exibeMatriz() {
		matrizObjeto.exibeMatriz();
	}

	
	public void exibeVertices() {
	/*	System.out.println("P0[" + vertices[0].obterX() + "," + vertices[0].obterY() + "," + vertices[0].obterZ() + "," + vertices[0].obterW() + "]");
		System.out.println("P1[" + vertices[1].obterX() + "," + vertices[1].obterY() + "," + vertices[1].obterZ() + "," + vertices[1].obterW() + "]");
		System.out.println("P2[" + vertices[2].obterX() + "," + vertices[2].obterY() + "," + vertices[2].obterZ() + "," + vertices[2].obterW() + "]");
		System.out.println("P3[" + vertices[3].obterX() + "," + vertices[3].obterY() + "," + vertices[3].obterZ() + "," + vertices[3].obterW() + "]");
//		System.out.println("anguloGlobal:" + anguloGlobal);*/
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

	public float[] getCor()
	{
		return cor;
	}

	public void setCor(float[] cor) {
		this.cor = cor;
	}

	/**
	 * Troca a primitiva
	 */
	public void trocaPrimitiva(){
		if(this.getPrimitiva() == GL.GL_LINE_LOOP){
			this.setPrimitiva(GL.GL_LINE_STRIP);
		} else {
			this.setPrimitiva(GL.GL_LINE_LOOP);
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

	public List<ObjetoGrafico> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<ObjetoGrafico> filhos) {
		this.filhos = filhos;
	}

	public List<ObjetoGrafico> getListaIrmaos() {
		return listaIrmaos;
	}

	public void setListaIrmaos(List<ObjetoGrafico> listaIrmaos) {
		this.listaIrmaos = listaIrmaos;
	}
	
	public void addIrmao(ObjetoGrafico irmao) {
		this.listaIrmaos.add(irmao);
	}
}
