package Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal da aplicação, contêm o mundo
 */
public class Mundo {
	private static Mundo instance;
	private Camera camera;
	private List<ObjetoGrafico> listaObjGrafico;
	private ObjetoGrafico poligonoSelecionado;
	
	public static Mundo getInstance(){
		if(instance == null){
			instance = new Mundo();
		}
		return instance;
	}
	
	private Mundo() {
		this.camera = new Camera();
		this.poligonoSelecionado = null;
		this.listaObjGrafico = new ArrayList<ObjetoGrafico>();
	}

	public List<ObjetoGrafico> getListaObjGrafico() {
		return listaObjGrafico;
	}

	public void addObjGrafico(ObjetoGrafico objGrafico) {
		this.listaObjGrafico.add(objGrafico);
	}

	public ObjetoGrafico getPoligonoSelecionado() {
		return poligonoSelecionado;
	}

	public void setPoligonoSelecionado(ObjetoGrafico poligonoSelecionado) {
		this.poligonoSelecionado = poligonoSelecionado;
	}
	
	public void removerPoligono(){
		
	}
	
	
	
	
	
}
