package Outros;

public class Cor {
	private static int vermelho=255,azul=255,verde = 255;	
	public static float[] AZUL = { 0, 0, azul };
	public static float[] VERMELHO = { vermelho, 0, 0 };
	public static float[] VERDE = { 0, verde, 0 };
	public static float[] AMARELO = combinarCoresRGB(VERDE, VERMELHO);
	public static float[] PRETO = { 0, 0, 0 };
	public static float[] BRANCO = combinarCoresRGB(VERDE,AZUL,VERMELHO);
	public static float[] MARROM = { vermelho/2, verde/4, 0 };
	public static float[] CINZA = {vermelho/2,verde/2,azul/2};
	public static float[] LARANJA = { vermelho, verde/2, 0 };
	public static float[] ROXO = { vermelho/2, 0, azul };
	
	
	
	/**
	 * Combina as cores para facilitar a chamada por constantes
	 * @param cores array com as cores
	 * @return
	 */
	private static float[] combinarCoresRGB(float[]...cores) {
		float[] corResultado = new float[3];
		
		for (int i = 0; i < cores.length; i++) {
			corResultado[0] += cores[i][0];
			corResultado[1] += cores[i][1];
			corResultado[2] += cores[i][2];
		}
	

		return corResultado;
	}
}
