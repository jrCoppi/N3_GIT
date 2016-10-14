package Principal;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import Outros.Cor;
import Padrao.Point4D;

/**
 * Classe principal da aplica��o, cont�m o mundo
 */
public class Mundo {
	private static Mundo instance;
	private Camera camera;
	public boolean alterarCor = false;
	public ObjetoGrafico nodoPrincipal;
	private boolean passouDoPrimeiroNivel = false;
	public ObjetoGrafico objSelecionadoMomento;
	public Point4D ponto1VerticeSelecionado;
	public Point4D ponto2VerticeSelecionado;

	// Modos 1 - Inserir, 2 = Editar
	public Integer modo = 1;

	public Boolean inserirRaiz = true;
	public boolean modoSelecao;

	public static Mundo getInstance() {
		if (instance == null) {
			instance = new Mundo();
		}
		return instance;
	}

	private Mundo() {
		this.camera = new Camera();
		this.nodoPrincipal = null;
	}

	// Desenha os elementos em tela
	public void desenhaTela(GL gl) {
		if (!this.isListaVazia()) {
			// Mostra a bbox do poligono selecionado
			//if(!modoSelecao){
				this.getPolignoSelecionado(false).mostrabBox(gl);
			//}
			
		/*	if(objSelecionadoMomento !=null){
				this.objSelecionadoMomento.mostrabBox(gl);
			}
			else
			{
				if(ponto1VerticeSelecionado!=null && ponto2VerticeSelecionado!=null)
				{ 	ObjetoGrafico obj = new ObjetoGrafico();
					obj.atribuirGL(gl);
					obj.addPonto(ponto1VerticeSelecionado);
//					obj.criabBox(ponto1VerticeSelecionado);
					objSelecionadoMomento = obj;
					addObjGraficoIrmao(obj);
					this.objSelecionadoMomento.criabBox(ponto1VerticeSelecionado);
					this.objSelecionadoMomento.mostrabBox(gl);
				}
			} */
				

			// Desenha ponto do nodo principal
			this.nodoPrincipal.atribuirGL(gl);
			this.nodoPrincipal.desenha(this.modo);

			// Desenha Pontos dos demais
			this.desenhaTelaRecursivo(gl, this.nodoPrincipal);
		}
	}

	public void desenhaTelaRecursivo(GL gl, ObjetoGrafico objGrafico) {
		// Primeiro percorre os irm�os
		for (ObjetoGrafico objeto : objGrafico.getListaIrmaos()) {
			objeto.atribuirGL(gl);
			objeto.desenha(this.modo);

			this.desenhaTelaRecursivo(gl, objeto);
		}

		// Depois percorre os filhos
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

		// Se tiver adicionando irm�o na raiz apenas (n�o saiu do primeiro
		// nivel) sempre vai estar no msm nivel do inicial
		if (this.passouDoPrimeiroNivel == false) {
			this.nodoPrincipal.getListaIrmaos().add(objGrafico);
			return;
		}

		// Se n�o vai adicionar um nivel acima do filho na lista de filhos
		this.getPolignoSelecionado(true).getFilhos().add(objGrafico);
	}

	public boolean isListaVazia() {
		return (this.nodoPrincipal == null);
	}

	public void addPonto(Point4D ponto) {
		this.getPolignoSelecionado(false).addPonto(ponto);
	}

	public void atualizabBox(Point4D ponto) {
		this.getPolignoSelecionado(false).atualizabBox(ponto);
	}

	public void removerPoligono() {
		if(this.getPolignoSelecionado(false).getListaPontos().size() == 0)
			return;
		
		for (int i =0; i <=  this.getPolignoSelecionado(false).getListaPontos().size(); i++) 
		{
			this.getPolignoSelecionado(false).removeUltimoPonto();
		}
		removerPoligono();
	}

	public void removePonto() {
		if (this.getPolignoSelecionado(false).getListaPontos().size() <= 2) {
			return;
		}
		this.getPolignoSelecionado(false).removePonto();
	}

	public void trocaPrimitiva() {
		if (this.isListaVazia()) {
			return;
		}
		this.getPolignoSelecionado(false).trocaPrimitiva();
	}

	public void setCor(float[] cor) {
		if (this.alterarCor)
			this.getPolignoSelecionado(false).setCor(cor);
		alterarCor = false;
	}

	// Retorna o poligono pai selecionado para desenhar bbox
	/*
	 * private ObjetoGrafico getPolignoPaiSelecionado(){ for (ObjetoGrafico
	 * objetoGrafico : this.nodoPrincipal.getListaIrmaos()) {
	 * if(objetoGrafico.isSelecionado()) return objetoGrafico; } return
	 * this.nodoPrincipal.getListaIrmaos().get(this.nodoPrincipal.getListaIrmaos
	 * ().size()-1); // nao encontrou um selecionado entao retorna o ultimo; }
	 */

	// Retorna o poligono selecionado
	public ObjetoGrafico getPolignoSelecionado(boolean retornaPai) {
		ObjetoGrafico retorno = null;

		if (this.isListaVazia()) {
			return null;
		}

		retorno = this.getPoligonoSelecionadoRecursivo(this.nodoPrincipal, retornaPai);

		// nao encontrou um selecionado entao retorna o ultimo;
		if (retorno == null) {

			if ((this.nodoPrincipal.getListaIrmaos().isEmpty()) || (this.nodoPrincipal.isSelecionado())){
				return this.nodoPrincipal;
			}

			retorno = this.nodoPrincipal.getListaIrmaos().get(this.nodoPrincipal.getListaIrmaos().size() - 1);
		}

		return retorno;
	}

	// Verifica os filhos dos objetos recursivamente para achar o selecionado
	public ObjetoGrafico getPoligonoSelecionadoRecursivo(ObjetoGrafico objetoGrafico, boolean retornaPai) {
		ObjetoGrafico retorno;

		// Primeiro percorre os irm�os
		for (ObjetoGrafico objeto : objetoGrafico.getListaIrmaos()) {
			if (objeto.isSelecionado()) {
				retorno = objeto;

				// Se for pra retornar o pai
				if (retornaPai) {
					retorno = objetoGrafico;
				}
				return retorno;
			}

			// Tenta percorrer o irm�o, caso n�o ache continua pro prox irmao
			retorno = this.getPoligonoSelecionadoRecursivo(objeto, retornaPai);
			if (retorno != null) {
				return retorno;
			}
		}

		// Depois percorre os filhos
		for (ObjetoGrafico objeto : objetoGrafico.getFilhos()) {
			if (objeto.isSelecionado()) {
				retorno = objeto;

				// Se for pra retornar o pai
				if (retornaPai) {
					retorno = objetoGrafico;
				}
				return retorno;
			}

			// Tenta percorrer o filho, caso n�o ache continua pro prox filho
			retorno = this.getPoligonoSelecionadoRecursivo(objeto, retornaPai);
			if (retorno != null) {
				return retorno;
			}
		}
		return null;
	}

	// Atualiza os filhos recursivamente como n�o selecionados
	public void atualizaPoligonoSelecionado() {

		// Vazio nenhum selecionado
		if (this.isListaVazia()) {
			return;
		}

		this.atualizaPoligonoSelecionadoRecursivo(this.nodoPrincipal);
	}

	// Atualiza os filhos recursivamente como n�o selecionados
	public void atualizaPoligonoSelecionadoRecursivo(ObjetoGrafico ObjetoGrafico) {

		for (ObjetoGrafico objeto : ObjetoGrafico.getListaIrmaos()) {
			if (objeto.isSelecionado()) {
				objeto.setSelecionado(false);
				return;
			}

			this.atualizaPoligonoSelecionadoRecursivo(objeto);
		}

		for (ObjetoGrafico objetoFilho : ObjetoGrafico.getFilhos()) {

			if (objetoFilho.isSelecionado()) {
				objetoFilho.setSelecionado(false);
				return;
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

	// Clicou no bot�ao esquerdo
	public void mouseClique(int mouseX, int mouseY, GL gl) {

		if(modoSelecao){
			selecionarObjetoGrafico( mouseX,  mouseY,gl);
			return ;
		}
		
		// Chegou, bot�o esquerdo
		Point4D novoPonto = new Point4D(mouseX, mouseY, 0, 1);
		
		if (this.modo == 1) {

			// Cria uma bbox e adiciona na lista
			ObjetoGrafico poligono = new ObjetoGrafico();
			poligono.criabBox(novoPonto);

			// Se a lista n�o tiver vazia
			if (!this.isListaVazia()) {

				if (this.inserirRaiz) {

					// Se tiver no modo de raiz insere um irm�o
					this.addObjGraficoIrmao(poligono);
				} else {

					// Se n�o, insere um novo filho
					this.addObjGraficoFilho(poligono);
				}

			} else {
				// Primeiro poligno
				this.nodoPrincipal = poligono;
			}

			// Zera os selecionados
			this.atualizaPoligonoSelecionado();

			// Seta o novo como selecionado
			poligono.setSelecionado(true);
		}

		this.getPolignoSelecionado(false).atualizabBox(novoPonto);
		this.getPolignoSelecionado(false).addPonto(novoPonto);

		this.modo = 2;
	}
	
	/**
	 * Seleciona um objeto gr�fico utilizando coordenadas do mouse do usu�rio
	 * @param x - eixo x do mouse na tela
	 * @param y - eixo y do mouse na tela
	 */
	private void selecionarObjetoGrafico(int x, int y, GL gl) {
		objSelecionadoMomento = null;
		if(nodoPrincipal.cliqueEstaNaBBox(x,y))
		{
			if(buscarInterseccaoPontos(nodoPrincipal,x,y) % 2 != 0)
			{//impar, poligno selecionado
				
				this.atualizaPoligonoSelecionado();
				nodoPrincipal.setSelecionado(true);
				//objSelecionadoMomento = nodoPrincipal;
				
				return;
			}
			//pegar v�rtice mais proximo ao clique
			
		}
		
		for (ObjetoGrafico obj : this.nodoPrincipal.getListaIrmaos()) 
		{
			if(obj.cliqueEstaNaBBox(x,y))
			{
				if(buscarInterseccaoPontos(obj,x,y) % 2 != 0)
				{
					this.atualizaPoligonoSelecionado();
					obj.setSelecionado(true);
					//objSelecionadoMomento = obj;
					return;
				}
			//pegar v�rtice mais proximo ao clique
			}
		}
		
		for (ObjetoGrafico obj : this.nodoPrincipal.getFilhos()) 
		{
			if(obj.cliqueEstaNaBBox(x,y))
			{
				if(buscarInterseccaoPontos(obj,x,y) % 2 != 0)
				{
					this.atualizaPoligonoSelecionado();
					obj.setSelecionado(true);
					//objSelecionadoMomento = obj;
					return;
				}
			//pegar v�rtice mais proximo ao clique
			}
		}
		
	}

	/**
	 * M�todo para verificar se o clique do usu�rio se interseciona nas interse��es do poligno
	 * @param obj - Objeto gr�fico
	 * @param xi - X da interse��o do clique
	 * @param yi - Y da interse��o do clique
	 * @return quantidade de pariedades encontradas. Se �mpar, objeto selecionado
	 */
	private int buscarInterseccaoPontos(ObjetoGrafico obj, int xi, int yi) {
		int quantidadePariedades=0;
		for (int i = 0; i < obj.getListaPontos().size()-1; i++) 
		{
			
			Point4D p1 = obj.getListaPontos().get(i);
			Point4D p2 = obj.getListaPontos().get(i+1);
			double ti = intersecaoY(p1.GetY(),p2.GetY(),yi);
			if(ti>0 && ti<=1) //h� interse��o de y
			{
				ponto1VerticeSelecionado = p1;
				ponto2VerticeSelecionado = p2;
				if(verificarPariedadeX(p1.GetX(),p2.GetX(),xi,ti))
					quantidadePariedades++;
			}
		}
		return quantidadePariedades;
		
	}

	/**
	 *  verifica a quantidade de X intersecinados na reta y
	 * @param x1 - primeiro ponto de reta do objeto gr�fico
	 * @param x2 - segundo ponto de reta do objeto gr�fico
	 * @param xi X da interse��o do clique
	 * @param ti - Linha intersecao y
	 * @return true se existir pariedade
	 */
	private boolean verificarPariedadeX(double x1, double x2, int xi, double ti) {
		
		double x = (x1) + (x2-x1) *ti;
		return x>xi;
		
	}

	/**
	 * Descobrir qual a linha que montada a partir de dois pontos de um objeto gr�fico
	 * @param y1 - primeiro ponto y de reta do objeto gr�fico
	 * @param y2 - segundo ponto y de reta do objeto gr�fico
	 * @param yi - ponto de interse��o y do clique do usu�rio
	 * @return o valor de TI
	 */
	private double intersecaoY(double y1, double y2, int yi) {
		
		double ti = (yi-y1) / (y2 -y1);		
		
		return ti;
	}
}
