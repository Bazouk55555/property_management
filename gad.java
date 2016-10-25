import java.sql.SQLException;

public class gad {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "xxx";
		String user = "xxx";
		String passwd = "xxx";
		Connect con= new Connect(url,user,passwd);
		try {
			Fenetre fen=new Fenetre(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
