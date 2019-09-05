package trabalho_02_SD;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		try {	
			Cliente_Servidor cliente_servidor = new Cliente_Servidor();
			cliente_servidor.executarServidor(6668);
		} catch (Exception e) {
			System.out.println("Erro: " + e.getClass());
		}

	}
}