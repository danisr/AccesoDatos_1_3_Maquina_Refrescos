package accesoDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class LeerFichero implements Datos {
	BufferedReader bReader = null;
	BufferedWriter bWriter = null;
	FileReader fReader = null;
	Deposito deposito;
	Dispensador dispensador;

	public LeerFichero() {
		System.out.println("ACCESO A DATOS - LEER DE FICHEROS");
	}

	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();
		try {
			fReader = new FileReader("Ficheros/monedas.txt");
			bReader = new BufferedReader(fReader);
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] aux = line.split(";");
				String nombre = (aux[0]);
				int valor = Integer.parseInt(aux[1]);
				int inicial = Integer.parseInt(aux[2]);
				Deposito depAux = new Deposito(nombre, valor, inicial);
				depositosCreados.put(valor, depAux);
			}
			bReader.close();
			fReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(depositosCreados);
		return depositosCreados;
	}

	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();
		try {
			fReader = new FileReader("Ficheros/productos.txt");
			bReader = new BufferedReader(fReader);
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] aux = line.split(";");
				String clave = (aux[0]);
				String nombre = (aux[1]);
				int p = Integer.parseInt(aux[2]);
				int inicial = Integer.parseInt(aux[3]);
				Dispensador dispAux = new Dispensador(clave, nombre, p, inicial);
				dispensadoresCreados.put(clave, dispAux);
			}
			bReader.close();
			fReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(dispensadoresCreados);
		return dispensadoresCreados;
	}

	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean ok = false;
		try {
			bWriter = new BufferedWriter(new FileWriter("Ficheros/monedas.txt"));
			for (Entry<Integer, Deposito> entrada : depositos.entrySet()) {
				Deposito depAux = entrada.getValue();
				bWriter.write(depAux.getNombreMoneda() + ";" + depAux.getValor() + ";" + depAux.getCantidad());
				bWriter.newLine();
				System.out.println("Metodo Guardar Deposito Realizado");
			}
			bWriter.close();
			ok = true;
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
			ok = false;
		}
		return ok;
	}

	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean ok = false;
		try {
			bWriter = new BufferedWriter(new FileWriter("Ficheros/productos.txt"));
			for (Entry<String, Dispensador> entrada : dispensadores.entrySet()) {
				Dispensador dispAux = entrada.getValue();
				bWriter.write(dispAux.getClave() + ";" + dispAux.getNombreProducto() + ";" + dispAux.getPrecio() + ";"
						+ dispAux.getCantidad());
				bWriter.newLine();
				System.out.println("Metodo Guardar Dispensador Realizado");
			}
			bWriter.close();
			ok = true;
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
			ok = false;
		}
		return ok;
	}
}
