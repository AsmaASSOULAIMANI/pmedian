import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


import ilog.concert.*;
import ilog.cplex.*;


public class Median {

	public static void main(String[] args) {
		resolution(5);

	}
	public  static void resolution(double p  ) {
		try {
			int M = 0;
			int N=0;
			double[] u= new double[20];
			double[] d= new double [30];
			double[][] c= new double [30][20];
			Connection connect = null;
			Statement statement = null;
			ResultSet resultSet2 = null;
			try {
				connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/pmedian?user=root&password=1234&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC ") ;
				statement= connect.createStatement();
				Statement statement1 = connect.createStatement();
				Statement statement2 = connect.createStatement();

				Class.forName("com.mysql.cj.jdbc.Driver");
				resultSet2= statement.executeQuery("select * from distance");
				ResultSet resultSet1 = statement1.executeQuery("select * from usine");
				ResultSet resultSet = statement2.executeQuery("select * from client");
				while(resultSet2.next()) {
					int t= resultSet2.getInt("idu");
					int r= resultSet2.getInt("idc");
					c[r-1][t-1]=Double.parseDouble(resultSet2.getString("cout").replace(",",".") );
				}
				while (resultSet1.next()) {
					u[M]=Double.parseDouble(resultSet1.getString("capacite").replace(",",".") );
					M+=1;
				}

				while (resultSet.next()) {
					d[N]=Double.parseDouble(resultSet.getString("demande").replace(",",".") );
					N+=1;
				}
			}catch( Exception ex) {
				 ex.printStackTrace(); 
			}					
			IloCplex cplex = new IloCplex();
			// variable 
			IloNumVar[][] x = new IloNumVar[N][M];
			for ( int i=0 ; i<N; i++) {
				for ( int j=0 ; j<M; j++) {
					x[i][j]= cplex.boolVar();
				}
			}
			IloNumVar[] y = new IloNumVar[M];
			for (int j = 0; j < M; j++) {
					y[j]= cplex.boolVar();
			}			
			// la fonction objective
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i=0; i<N; i++) {
        		for (int j=0; j<M; j++) {
	    			objective.addTerm(d[i]*c[i][j],x[i][j]);
        		}
        	}
			cplex.addMinimize(objective);
			//Les contraintes 
			
			/**************  Contrainte (2)   *************/
			for (int i=0; i<30; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int j=0; j<20; j++) {
					expr.addTerm(1.0, x[i][j]);
				}	
				cplex.addEq(expr, 1.0);
			}
			/**************  Contrainte (5)   *************/
			IloLinearNumExpr expr = cplex.linearNumExpr();
			for (int j=0; j<20; j++) {
				expr.addTerm(1.0, y[j]);
			}
			cplex.addEq(expr, p);
			
			/**************  Contrainte (4)   *************/
			for (int i=0; i<30; i++) {
				for (int j=0; j<20; j++) {
					cplex.addLe(x[i][j], y[j]);
				}
			}
			
			/**************  Contrainte (3)   *************/
			for (int j=0; j<20; j++) {
				IloLinearNumExpr exprs = cplex.linearNumExpr();
				for (int i=0 ; i<30; i++) {
					exprs.addTerm( d[i], x[i][j]);
				}
				cplex.addLe(exprs,cplex.prod(u[j], y[j]));
			}
			
			///*******Résoudre le probléme*******\\\
			if (cplex.solve()) {
				
        		System.out.println("obj = "+cplex.getObjValue());
        	}
        	else {
        		System.out.println("problem not solved");
        	}
        	cplex.end();
			
			
		}catch(IloException exe) {
			exe.printStackTrace();
		}
	}
}
