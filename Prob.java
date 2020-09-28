package excel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class Prob {
	public static void main(String[] args) throws Exception  {
		double x;
		double y;
		double dis;
		double u,c;
		double capacite ,demande;
		int i = 0,j =0;
		ResultSet resultSet1 =null;
		double[][] d= new double [600][600];
		try {
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmedian?user=root&password=1234&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ") ;
			Statement statement = connection.createStatement();
			Class.forName("com.mysql.cj.jdbc.Driver");
			resultSet1 = statement.executeQuery("select * from client cl join usine us ");

			while (resultSet1.next() && i<600 ) {
				/**** calcul de la distance ****/
				x = Math.pow(resultSet1.getDouble("xu")-resultSet1.getDouble("xc"),2);
				y = Math.pow(resultSet1.getDouble("yu")-resultSet1.getDouble("yc"),2);
				dis = Math.sqrt ( x+y);
				d[i][j+2]= dis;
				/**** Les id ****/
				u =(int)resultSet1.getInt("us.idu");
				d[i][j]= u;
				c = resultSet1.getInt("cl.idc");
				d[i][j+1]= c;
				capacite = resultSet1.getDouble("us.capacite");
				d[i][j+3]= capacite;
				demande = resultSet1.getDouble("cl.demande");
				d[i][j+4]=demande;
				i+=1;
				System.out.println(i);
			}
			/**** remplissage de la table distance ****/
			for (int k=0  ; k<600 ; k++) {
					String sql = String.format("INSERT INTO distance (idu,idc,cout,capacit_us,demande_cl) VALUES ('%d','%d','%f','%f','%f')",(int)d[k][j] ,(int)d[k][j+1],d[k][j+2],d[k][j+3],d[k][j+4]);
					int count = statement.executeUpdate(sql);
					if (count>0) {
						System.out.println("C'est bon ");
						
					}
				}
			
		}catch( Exception ex) {
			 ex.printStackTrace();
				
			}
			
	}
}
