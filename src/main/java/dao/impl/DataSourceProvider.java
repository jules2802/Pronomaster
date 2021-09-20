package dao.impl;

import org.mariadb.jdbc.MariaDbDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlets.PutScoreMatchServlet;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceProvider {

	private static MariaDbDataSource dataSource;
	private static Properties properties = new Properties();
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceProvider.class);

	public static DataSource getDataSource() throws SQLException{
		try {
			properties.load(DataSourceProvider.class.getResourceAsStream("/application.properties"));

			if (dataSource == null) {
				dataSource = new MariaDbDataSource();
				dataSource.setServerName(properties.getProperty("servername"));
				dataSource.setPort(Integer.parseInt(properties.getProperty("port")));
				dataSource.setDatabaseName(properties.getProperty("databasename"));
				dataSource.setUser(properties.getProperty("user"));
				if( properties.getProperty("password").isEmpty()){
					dataSource.setPassword("");
				}else{
				dataSource.setPassword(properties.getProperty("password"));}
				LOG.info("Instanciation des parametres pour la liaison avec la base de données");
			}
			LOG.debug("La connexion a bien ete effectuee : {}", dataSource);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return dataSource;
	}
}
