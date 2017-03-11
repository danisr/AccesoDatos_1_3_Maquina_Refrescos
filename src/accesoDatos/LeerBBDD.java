package accesoDatos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class LeerBBDD implements Datos {
	private Connection conexion;
	Deposito deposito;
	Dispensador dispensador;

	public LeerBBDD() {
		System.out.println("ACCESO A DATOS - LEER DE BBDD");
		try {
			HashMap<String, String> hmap = loadFichero("configbbdd.ini");
			Class.forName("com.mysql.jdbc.Driver");
			String pwdOriginal = "root";
			conexion = DriverManager.getConnection(hmap.get("url"), hmap.get("login"), pwdOriginal);
			System.out.println("Lectura de BBDD correcta");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver no cargado");
		} catch (SQLException e) {
			System.out.println("Error de conexion con SQL");
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String> loadFichero(String nombreFichero) throws IOException {
		HashMap<String, String> hmDatos = new HashMap<String, String>();
		FileReader fReader = new FileReader(nombreFichero);
		BufferedReader bReader = new BufferedReader(fReader);
		String line = bReader.readLine();
		while (line != null) {
			String[] arrLine = line.split("=");
			hmDatos.put(arrLine[0], arrLine[1]);
			line = bReader.readLine();
		}
		bReader.close();
		fReader.close();
		return hmDatos;
	}

	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();
		try {
			if (conexion != null) {
				String query = "SELECT * FROM maqexp.monedas;";
				PreparedStatement pstmt = conexion.prepareStatement(query);
				ResultSet rset = pstmt.executeQuery();
				while (rset.next()) {
					String nombre = rset.getString("Nombre");
					int valor = rset.getInt("Valor");
					int inicial = rset.getInt("Cantidad");
					Deposito depAux = new Deposito(nombre, valor, inicial);
					depositosCreados.put(valor, depAux);
				}
				pstmt.close();
				rset.close();
			} else {
				System.out.println("Conexion Nula");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(depositosCreados);
		return depositosCreados;
	}

	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();
		try {
			if (conexion != null) {
				String query = "SELECT * FROM maqexp.productos;";
				PreparedStatement pstmt = conexion.prepareStatement(query);
				ResultSet rset = pstmt.executeQuery();
				while (rset.next()) {
					int claveInt = rset.getInt("idProductos");
					String clave = claveInt + "";
					String nombre = rset.getString("Nombre");
					int p = rset.getInt("Precio");
					int inicial = rset.getInt("Cantidad");
					Dispensador dispAux = new Dispensador(clave, nombre, p, inicial);
					dispensadoresCreados.put(clave, dispAux);
				}
				pstmt.close();
				rset.close();
			} else {
				System.out.println("Conexion Nula");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(dispensadoresCreados);
		return dispensadoresCreados;
	}

	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean ok = false;
		try {
			Iterator it = depositos.values().iterator();
			String query = "UPDATE maqexp.monedas SET Cantidad=? WHERE Nombre=?;";
			PreparedStatement pstmt = conexion.prepareStatement(query);

			while (it.hasNext()) {
				Deposito dep = (Deposito) it.next();
				pstmt.setInt(1, dep.getCantidad());
				pstmt.setString(2, dep.getNombreMoneda());
				pstmt.executeUpdate();
			}
			pstmt.close();
			System.out.println("Update de depositos realizado correctamente");
			ok = true;
		} catch (SQLException e) {
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}

	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean ok = false;
		try {
			Iterator it = dispensadores.values().iterator();
			String query = "UPDATE maqexp.productos SET Cantidad=? WHERE idProductos=?;";
			PreparedStatement pstmt = conexion.prepareStatement(query);
			while (it.hasNext()) {
				Dispensador disp = (Dispensador) it.next();
				pstmt.setInt(1, disp.getCantidad());
				pstmt.setString(2, disp.getClave());
				pstmt.executeUpdate();
			}
			pstmt.close();
			System.out.println("Update de dispensador realizado correctamente");
			ok = true;
		} catch (SQLException s) {
			s.printStackTrace();
			ok = false;
		}
		return ok;
	}
}