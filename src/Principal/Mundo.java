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
	public boolean alterarCor=false;
	public ObjetoGrafico nodoPrincipal;
	private boolean passouDoPrimeiroNivel = false;
	
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
		this.nodoPrincipal = null;
	}
	
	//Desenha os elementos em tela
	public void desenhaTela(GL gl){
		if(!this.isListaVazia()){
			//Mostra a bbox do poligono selecionado
			this.getPolignoSelecionado(false).mostrabBox(gl);
			
			//Desenha ponto do nodo principal
			this.nodoPrincipal.atribuirGL(gl);
			this.nodoPrincipal.desenha(this.modo);
			
			//Desenha Pontos dos demais
			this.desenhaTelaRecursivo(gl,this.nodoPrincipal);
		}
	}
	
	public void desenhaTelaRecursivo(GL gl,ObjetoGrafico objGrafico ){
		//Primeiro percorre os irmãos
		for (ObjetoGrafico objeto : objGrafico.getListaIrmaos()) {
			objeto.atribuirGL(gl);
			objeto.desenha(this.modo);
			
			this.desenhaTelaRecursivo(gl, objeto);
		}
		
		//Depois percorre os filhos
		for (ObjetoGrafico objeto : objGrafico.getFilhos()) {
			objeto.atribuirGL(gl);
			objeto.desenha(this.modo);
			
			this.desenhaTelaRecursivo(gl, objeto);
		}
	}
	
	public void addObjGraficoFilho(ObjetoGrafico objGrafico) {
		this.passouDoPrimeiroNivel = true;
		this.getPolignoSelecionado(false).getFilhos().add(objGrafico);
	}
	
	public void addObjGraficoIrmao(ObjetoGrafico objGrafico) {
		
		//Se tiver adicionando irmão na raiz apenas (não saiu do primeiro nivel) sempre vai estar no msm nivel do inicial
		if(this.passouDoPrimeiroNivel == false){
			this.nodoPrincipal.getListaIrmaos().add(objGrafico);	
			return ;
		}
		
		//Se não vai adicionar um nivel acima do filho na lista de filhos
		this.getPolignoSelecionado(true).getFilhos().add(objGrafico);		
	}
	
	public boolean isListaVazia(){
		return (this.nodoPrincipal == null);
	}
	
	public void addPonto(Point4D ponto) {
		this.getPolignoSelecionado(false).addPonto(ponto);
	}
	
	public void atualizabBox(Point4D ponto){
		this.getPolignoSelecionado(false).atualizabBox(ponto);
	}
	
	public void removerPoligono(){
		
	}
	
	public void removePonto(){
		if(this.getPolignoSelecionado(false).getListaPontos().size() <= 2){
			return ;
		}
		this.getPolignoSelecionado(false).removePonto();
	}
	
	public void trocaPrimitiva(){
		if(this.isListaVazia()){
			return ;
		}
		this.getPolignoSelecionado(false).trocaPrimitiva();
	}
	
	public void setCor(float[] cor){
		if (this.alterarCor)
			this.getPolignoSelecionado(false).setCor(cor);
		alterarCor = false;
	}
	
	//Retorna o poligono pai selecionado para desenhar bbox
	/*private ObjetoGrafico getPolignoPaiSelecionado(){
		for (ObjetoGrafico objetoGrafico : this.nodoPrincipal.getListaIrmaos()) {
			if(objetoGrafico.isSelecionado())
				return objetoGrafico;
		}
		return this.nodoPrincipal.getListaIrmaos().get(this.nodoPrincipal.getListaIrmaos().size()-1); // nao encontrou um selecionado entao retorna o ultimo;
	}*/
	
	//Retorna o poligono selecionado
	public ObjetoGrafico getPolignoSelecionado(boolean retornaPai){
		ObjetoGrafico retorno = null;
		
		if(this.isListaVazia()){
			return null;
		}
		
		retorno = this.getPoligonoSelecionadoRecursivo(this.nodoPrincipal,retornaPai);

		
		// nao encontrou um selecionado entao retorna o ultimo;
		if(retorno == null){
			
			if(this.nodoPrincipal.getListaIrmaos().isEmpty()){
				return this.nodoPrincipal;
			}
			
			retorno =  this.nodoPrincipal.getListaIrmaos().get(this.nodoPrincipal.getListaIrmaos().size()-1);
		}
		
		return retorno;
	}
	
	//Verifica os filhos dos objetos recursivamente para achar o selecionado
	public ObjetoGrafico getPoligonoSelecionadoRecursivo(ObjetoGrafico objetoGrafico,boolean retornaPai){
		ObjetoGrafico retorno;
		
		//Primeiro percorre os irmãos
		for (ObjetoGrafico objeto : objetoGrafico.getListaIrmaos()) {
			if(objeto.isSelecionado()){
				retorno = objeto;
				
				//Se for pra retornar o pai
				if(retornaPai){
					retorno = objetoGrafico;
				}
				return retorno;
			}
			
			//Tenta percorrer o irmão, caso não ache continua pro prox irmao
			retorno = this.getPoligonoSelecionadoRecursivo(objeto,retornaPai);
			if(retorno != null){
				return retorno;
			}
		}
		
		//Depois percorre os filhos
		for (ObjetoGrafico objeto : objetoGrafico.getFilhos()) {
			if(objeto.isSelecionado()){
				retorno = objeto;
				
				//Se for pra retornar o pai
				if(retornaPai){
					retorno = objetoGrafico;
				}
				return retorno;
			}
			
			//Tenta percorrer o filho, caso não ache continua pro prox filho
			retorno = this.getPoligonoSelecionadoRecursivo(objeto,retornaPai);
			if(retorno != null){
				return retorno;
			}
		}
		return null;
	}
	
	//Atualiza os filhos recursivamente como não selecionados
	private void atualizaPoligonoSelecionado(){

		//Vazio nenhum selecionado
		if(this.isListaVazia()){
			return ;
		}
		
		this.atualizaPoligonoSelecionadoRecursivo(this.nodoPrincipal);
	}
	
	//Atualiza os filhos recursivamente como não selecionados
	private void atualizaPoligonoSelecionadoRecursivo(ObjetoGrafico ObjetoGrafico){
		
		for (ObjetoGrafico objeto : ObjetoGrafico.getListaIrmaos()) {
			if(objeto.isSelecionado()){
				objeto.setSelecionado(false);
				return ;
			}
			
			this.atualizaPoligonoSelecionadoRecursivo(objeto);
		}
		
		for (ObjetoGrafico objetoFilho : ObjetoGrafico.getFilhos()) {
			
			if(objetoFilho.isSelecionado()){
				objetoFilho.setSelecionado(false);
				return ;
			}

			this.atualizaPoligonoSelecionadoRecursivo(objetoFilho);
		}
	}

	public void setxRastro(double xRastro) {
		this.getPolignoSelecionado(false).setxRastro(xRastro);
	}

	public void setyRastro(double yRastro) {
		this.getPolignoSelecionado(false).setyRastro(yRastro);
	}
	
	//Clicou no botçao esquerdo
	public void mouseClique(int mouseX, int mouseY){

		//Chegou, botão esquerdo
		Point4D novoPonto = new Point4D(mouseX,  mouseY, 0, 1);
		
		if(this.modo == 1){
			
			//Cria uma bbox e adiciona na lista
			ObjetoGrafico poligono = new ObjetoGrafico();
			poligono.criabBox(novoPonto);
			

			//Se a lista não tiver vazia
			if(!this.isListaVazia()){
				
				if(this.inserirRaiz){
					
					//Se tiver no modo de raiz insere um irmão
					this.addObjGraficoIrmao(poligono);
				} else {
					
					//Se não, insere um novo filho
					this.addObjGraficoFilho(poligono);
				}
				
				
			} else {
				//Primeiro poligno
				this.nodoPrincipal = poligono;
			}
			
			
			//Zera os selecionados
			this.atualizaPoligonoSelecionado();
			
			//Seta o novo como selecionado
			poligono.setSelecionado(true);
		}

		//Atualiza bbox e a lista de pontos
		this.getPolignoSelecionado(false).atualizabBox(novoPonto);
		this.getPolignoSelecionado(false).addPonto(novoPonto);	
		
		this.modo = 2;
	}
	
}
