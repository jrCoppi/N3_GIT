package Principal;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import Outros.Cor;
import Padrao.Point4D;

/**
 * Classe principal da aplicação, contêm o mundo
 */
public class Mundo {
	private static Mundo instance;
	private Camera camera;
	private ObjetoGrafico poligonoSelecionado;
	public List<ObjetoGrafico> listaPoligonos;
	public boolean alterarCor=false;
	
	//Para acessar o selecionado, 1 -nivel do pai, 2 -filho ....
	private int[] arrSelecionado;
	
	//Modos 1 - Inserir, 2 = Editar
	public Integer modo = 1;
	
	public Boolean inserirRaiz = true;
	
	public static Mundo getInstance(){
		if(instance == null){
			instance = new Mundo();
		}
		return instance;
	}
	
	private Mundo() {
		this.camera = new Camera();
		this.poligonoSelecionado = null;
		this.listaPoligonos = new ArrayList<ObjetoGrafico>();
		this.arrSelecionado = new int[10];
	}
	
	//Desenha os elementos em tela
	public void desenhaTela(GL gl){
		if(!this.listaPoligonos.isEmpty()){
			this.getPolignoPaiSelecionado().mostrabBox(gl);
			
			//Desenha Pontos
			for (int i = 0; i < this.listaPoligonos.size(); i++) {
				this.listaPoligonos.get(i).atribuirGL(gl);
				this.listaPoligonos.get(i).desenha(this.modo);
			}
		}
	}

	public List<ObjetoGrafico> getListaObjGrafico() {
		return listaPoligonos;
	}

	public void addObjGrafico(ObjetoGrafico objGrafico) {
		this.listaPoligonos.add(objGrafico);
	}
	
	public void addObjGraficoFilho(ObjetoGrafico objGrafico) {
		this.getPolignoSelecionado().getFilhos().add(objGrafico);
	}
	
	public boolean isListaVazia(){
		return (this.getListaObjGrafico().size() == 0);
	}

	public ObjetoGrafico getPoligonoSelecionado() {
		return poligonoSelecionado;
	}

	public void setPoligonoSelecionado(ObjetoGrafico poligonoSelecionado) {
		this.poligonoSelecionado = poligonoSelecionado;
	}
	
	public void addPonto(Point4D ponto) {
		this.getPolignoSelecionado().addPonto(ponto);
	}
	
	public void atualizabBox(Point4D ponto){
		this.getPolignoSelecionado().atualizabBox(ponto);
	}
	
	public void removerPoligono(){
		
	}
	
	public void removePonto(){
		if(this.getPolignoSelecionado().getListaPontos().size() <= 2){
			return ;
		}
		this.getPolignoSelecionado().removePonto();
	}
	
	public void trocaPrimitiva(){
		if(this.isListaVazia()){
			return ;
		}
		this.getPolignoSelecionado().trocaPrimitiva();
	}
	
	public void setCor(float[] cor){
		if (this.alterarCor)
			getPolignoPaiSelecionado().setCor(cor);
		alterarCor = false;
	}
	
	//Retorna o poligono pai selecionado para desenhar bbox
	private ObjetoGrafico getPolignoPaiSelecionado(){
		for (ObjetoGrafico objetoGrafico : listaPoligonos) {
			if(objetoGrafico.isSelecionado())
				return objetoGrafico;
		}
		return listaPoligonos.get(listaPoligonos.size()-1); // nao encontrou um selecionado entao retorna o ultimo;
	}
	
	//Retorna o poligono selecionado
	public ObjetoGrafico getPolignoSelecionado(){
		ObjetoGrafico retorno = null;
		for (ObjetoGrafico objeto : this.listaPoligonos) {
			if(objeto.isSelecionado()){
				return objeto;
			}
			
			retorno = this.getPoligonoSelecionadoRecursivo(objeto);
		}
		
		// nao encontrou um selecionado entao retorna o ultimo;
		if(retorno == null){
			retorno =  listaPoligonos.get(listaPoligonos.size()-1);
		}
		
		return retorno;
	}
	
	//Verifica os filhos dos objetos recursivamente para achar o selecionado
	public ObjetoGrafico getPoligonoSelecionadoRecursivo(ObjetoGrafico objetoGrafico){
		for (ObjetoGrafico objeto : objetoGrafico.getFilhos()) {
			if(objeto.isSelecionado()){
				return objeto;
			}
			return this.getPoligonoSelecionadoRecursivo(objeto);
		}
		return null;
	}
	
	//zera a lista de selecionados
	private void atualizaSelecionado(){
		this.arrSelecionado = new int[10];
	
		for (ObjetoGrafico objeto : this.listaPoligonos) {
			objeto.setSelecionado(false);
			this.atualizaPoligonoSelecionado(objeto);
		}
	}
	
	//Atualiza os filhos recursivamente como não selecionados
	private void atualizaPoligonoSelecionado(ObjetoGrafico objeto){
		
		for (ObjetoGrafico objetoFilho : objeto.getFilhos()) {
			objetoFilho.setSelecionado(false);
			this.atualizaPoligonoSelecionado(objetoFilho);
		}
	}

	private void adicionaSelecionado(int posicao){
		for (int i = 0; i < this.arrSelecionado.length; i++) {
			//if(this.arrSelecionado[i] == null){
				
			//}
		}
	}
	
	public void setxRastro(double xRastro) {
		this.getPolignoSelecionado().setxRastro(xRastro);
	}

	public void setyRastro(double yRastro) {
		this.getPolignoSelecionado().setyRastro(yRastro);
	}
	
	//Clicou no botçao esquerdo
	public void mouseClique(int mouseX, int mouseY){

		//Chegou, botão esquerdo
		Point4D novoPonto = new Point4D(mouseX,  mouseY, 0, 1);
		
		if(this.modo == 1){
			
			//Cria uma bbox e adiciona na lista
			ObjetoGrafico poligono = new ObjetoGrafico();
			poligono.criabBox(novoPonto);
			
			
			if((!this.isListaVazia()) && (!this.inserirRaiz)){
				this.addObjGraficoFilho(poligono);
			} else {
				this.addObjGrafico(poligono);
			}
			/*
			//Se for inserir raiz reseta os selecionados e seta o novo como selecionado
			if(this.inserirRaiz){
				this.atualizaSelecionado();
				this.arrSelecionado[0] = this.listaPoligonos.size()-1;
			} else {
				
			}*/
			
		}
		//Arrumar para ver qual o oligono sendo editado (pai ou filho) e editar o mesmo e não sempre o pai
		//Atualiza bbox e a lista de pontos
		this.atualizabBox(novoPonto);
		this.addPonto(novoPonto);	
		
		this.modo = 2;
	}
	
}
