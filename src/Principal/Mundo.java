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
		this.listaPoligonos.get(this.listaPoligonos.size()-1).getFilhos().add(objGrafico);
	}
	
	public boolean isListaVazia(){
		return (Mundo.getInstance().getListaObjGrafico().size() == 0);
	}

	public ObjetoGrafico getPoligonoSelecionado() {
		return poligonoSelecionado;
	}

	public void setPoligonoSelecionado(ObjetoGrafico poligonoSelecionado) {
		this.poligonoSelecionado = poligonoSelecionado;
	}
	
	public void addPonto(Point4D ponto) {
		this.listaPoligonos.get(this.listaPoligonos.size()-1).addPonto(ponto);
	}
	
	public void atualizabBox(Point4D ponto){
		this.listaPoligonos.get(this.listaPoligonos.size()-1).atualizabBox(ponto);
	}
	
	public void removerPoligono(){
		
	}
	
	//Retorna o poligono pai selecionado para desenhar bbox
	private ObjetoGrafico getPolignoPaiSelecionado()
	{
		for (ObjetoGrafico objetoGrafico : listaPoligonos) {
			if(objetoGrafico.isSelecionado())
				return objetoGrafico;
		}
		return listaPoligonos.get(listaPoligonos.size()-1); // nao encontrou um selecionado entao retorna o ultimo;
	}
	
	//Retorna o poligono selecionado
	public ObjetoGrafico getPolignoSelecionado()
	{
		/*for (ObjetoGrafico objetoGrafico : listaPoligonos) {
			if(objetoGrafico.isSelecionado())
				return objetoGrafico;
		}*/
		return listaPoligonos.get(listaPoligonos.size()-1); // nao encontrou um selecionado entao retorna o ultimo;
	}
	
	public void trocaPrimitiva(){
		this.listaPoligonos.get(this.listaPoligonos.size()-1).trocaPrimitiva();
	}
	
	public void setCor(float[] cor){
		if (Mundo.getInstance().alterarCor)
			getPolignoPaiSelecionado().setCor(cor);
		alterarCor = false;
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
		this.listaPoligonos.get(this.listaPoligonos.size()-1).setxRastro(xRastro);
	}

	public void setyRastro(double yRastro) {
		this.listaPoligonos.get(this.listaPoligonos.size()-1).setyRastro(yRastro);
	}
	
}
