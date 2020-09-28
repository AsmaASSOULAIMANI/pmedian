package excel;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Client {
	public static void main(String[] args) throws Exception  {
		XSSFWorkbook fichierIn;
		XSSFSheet feuilleIn;
		XSSFRow ligneIn;
		Cell cellIn;
		ArrayList<String> values = new ArrayList<String>();
		try {
			FileInputStream fis = new  FileInputStream(new File ("client.xlsx"));
			fichierIn= new XSSFWorkbook(fis);
			int nbf= fichierIn.getNumberOfSheets();
			
			System.out.println(nbf);
			
					feuilleIn = fichierIn.getSheetAt(0);
					int v = feuilleIn.getLastRowNum();
					System.out.println(v);
					
					for(java.util.Iterator<Row> ligneIt=feuilleIn.iterator();ligneIt.hasNext();) {
						ligneIn = (XSSFRow) ligneIt.next();
						values.clear();
						for(java.util.Iterator<Cell> cellIt = ligneIn.iterator(); cellIt.hasNext(); ) {
							cellIn = (Cell) cellIt.next();
							switch(cellIn.getCellType()) {
							case STRING:
								values.add(cellIn.getStringCellValue());
								break;
							case NUMERIC:
								values.add(Integer.toString((int) cellIn.getNumericCellValue()));
								break;
							}
							
						}
						// insertion dans la base
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmedian?user=root&password=1234&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ") ;
						Statement statement = connection.createStatement();
						String sql = String.format("INSERT INTO client (  name_clt, demande , xc , yc) VALUES ('%s','%d','%d','%d')", values.get(0), Integer.parseInt(values.get(1)),Integer.parseInt(values.get(2)),Integer.parseInt(values.get(3)));
						int count = statement.executeUpdate(sql);
						if (count>0) {
							System.out.println("C'est bon ");
							
						}	
						
					}
					
			}finally {
							
			}			
			
	}
}
