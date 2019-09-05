package trabalho_02_SD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class ThreadWork extends Thread implements Runnable{

	private Socket cliente;
	public static String ipVizinhoCima = "";
	public static String ipVizinhoBaixo = "";

	public static void enviarMensagem(String mensagem, String ip, String meuIp) throws IOException {
		try {
			Socket cliente = new Socket(ip, 6668);
			PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
			if(cliente.isConnected()) {
				saida.println(mensagem+";"+meuIp);
				System.out.println("Enviando... " + mensagem+";"+meuIp);
				cliente.close();
			}	
		}catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

	public static void getIpVizinhos(int ip) throws UnknownHostException, IOException {
		for (int i = ip+1; i < ip + 10; i++) {
			ipVizinhoCima = "192.168.1." + i;
			System.out.println("Analisando... " + ipVizinhoCima);
			if (InetAddress.getByName(ipVizinhoCima).isReachable(100)) {
				System.out.println("IP Vizinho Acima: " + ipVizinhoCima + " OK");
				break;
			}
		}
		for (int i = ip; i > ip-20; i--) {
			ipVizinhoBaixo = "192.168.1." + i;
			System.out.println("Analisando... " + ipVizinhoBaixo);
			if (InetAddress.getByName(ipVizinhoBaixo).isReachable(100)) {
				System.out.println("IP Vizinho Abaixo: " + ipVizinhoBaixo + " OK");
				break;
			}
		}
		if (ipVizinhoCima.equals(null)) {
			System.out.println("Sem vizinhos acima do seu IP!!!");
		}
		if (ipVizinhoBaixo.equals(null)) {
			System.out.println("Sem vizinhos abaixo do seu IP!!!");
		}
	}

	public static String getMeuIp() {
		String ipAddress = null;
		Enumeration<NetworkInterface> net = null;
		try {
			net = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		while (net.hasMoreElements()) {
			NetworkInterface element = net.nextElement();
			Enumeration<InetAddress> addresses = element.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress ip = addresses.nextElement();

				if (ip.isSiteLocalAddress()) {
					ipAddress = ip.getHostAddress();
				}
			}
		}
		return ipAddress;
	}

	public ThreadWork(Socket cliente) {
		this.cliente = cliente;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			String ipCliente = cliente.getInetAddress().getHostAddress();
			String ipString = getMeuIp();
			int meuIp, ipClient;
			String array[] = new String[3];
			System.out.println("Meu IP é: " + ipString);
			array = ipString.split("[.]");
			meuIp = Integer.parseInt(array[3]);
			
			getIpVizinhos(meuIp);
			System.out.println("Um host foi detectado, com o IP - " + ipCliente);
			array = ipCliente.split("[.]");
			ipClient = Integer.parseInt(array[3]);
			String linha = entrada.readLine();
			System.out.println("Recebido: " + linha.toUpperCase());
			if (ipClient > meuIp) {
				enviarMensagem(linha, ipVizinhoBaixo, ipString);
			} else {
				enviarMensagem(linha, ipVizinhoCima, ipString);
			}
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				cliente.close();
			} catch (IOException e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
	}
}