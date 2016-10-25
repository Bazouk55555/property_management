import java.sql.Connection;
import java.sql.DriverManager;

public class Connect { 
	private Connection conn;
	
	public Connect(String url,String user,String passwd){
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver O.K.");
	
			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");         
	         
	    } 	catch (Exception e) {
	    	e.printStackTrace();
	    }    
	}
	
	public Connection getConn()
	{
		return conn;
	}
}