package trabalho_02_SD;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente_Servidor{
	
	
	
	private ServerSocket servidor;

	public void enviarMinhaFofoca(String mensagem){
		String ip = ThreadWork.getMeuIp();
		String array[] = new String[3];
		array = ip.split("[.]");
		int meuIp = Integer.parseInt(array[3]);
		try {
			String cont = "";
			String proximoIp = "";
			for (int i = meuIp-1; i > meuIp - 50; i--) {
				cont = "192.168.1." + i;
				System.out.println("Analisando o IP... " + cont + " para enviar a mensagem!");
				if (InetAddress.getByName(cont).isReachable(100)) {
					System.out.println("IP Vizinho Abaixo: " + cont + " OK");
					proximoIp = cont;
					break;
				}
			}
			if(proximoIp == "") {
				System.out.println("não há ninguém para enviar!");
			}else {
				Socket cliente = new Socket(proximoIp, 6668);
				PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
				System.out.println("Enviando minha mensagem...");
				saida.println(ip + ";" + mensagem);
				System.out.println(ip + ";" + mensagem);
			}
		} catch (UnknownHostException e) {	
			System.out.println("Erro: "+ e.getMessage() );
		} catch (IOException e) {
			System.out.println("Erro: "+ e.getMessage() );
		}
	}
	
	public void executarServidor(int porta) {
		try {
			servidor = new ServerSocket(porta);
			System.out.println("Servidor Ligado!");
			System.out.println("PORTA " + porta + " ABERTA!");
			enviarMinhaFofoca("LUCAS");
			while (true) {
				System.out.println("Aguardando hosts...");
				Socket cliente = null;
				Thread clienteThread = new ThreadWork(cliente);
				clienteThread.start();
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
		}finally {
			
		}
	}
	
}
