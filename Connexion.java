import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
public class Connexion {

	public static void main(String[] args) {
		int M = 0;
		int N=0;
		double[] u= new double [100];
		double[] f= new double [100];
		double[] d= new double [100];
		double[][] c= new double [100][100];
		Connection connect = null;
		Statement statement = null;
		//PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmedian?user=root&password=1234&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ") ;
			statement= connect.createStatement();
			//read from database (table usine )
			resultSet= statement.executeQuery("select * from usine");
			while(resultSet.next()) {
				f[M]= resultSet.getDouble("cout_ouverture");
				u[M]= resultSet.getDouble("capacite");
				M+=1;
			}
			System.out.println(M);
			//read from database (table client )
			resultSet1= statement.executeQuery("select * from client");
			while(resultSet1.next()) {
				d[N]= resultSet1.getDouble("demande");
				N+=1;
			}
			System.out.println(N);
			//read from database (table distance )
			resultSet2= statement.executeQuery("select * from distance");
			while(resultSet2.next()) {
				int t= resultSet2.getInt("idu");
				int r= resultSet2.getInt("idc");
				c[r-1][t-1]=resultSet2.getDouble("cout_affectation");
				System.out.println(c[r-1][t-1]);
			}			
			
		}catch( Exception ex) {
			 ex.printStackTrace(); 
		}
	}
	

}
