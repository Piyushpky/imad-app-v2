package com.hp.wpp.avatar.registration.device.hsql;

import java.sql.Connection;
import java.sql.DriverManager;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

public class HSQLDBSetup {
	
	public HSQLDBSetup(String context, String connectionUrl, String driverName, String userName, String password, String changeLog) {
		JdbcConnection hsqlConnection = null;

        try {
            ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();

            Class.forName(driverName);
            Connection connection = DriverManager.getConnection(connectionUrl, userName, password);

            hsqlConnection = new JdbcConnection(connection);

            Liquibase liquibase = new Liquibase(changeLog, resourceAccessor, hsqlConnection);
            liquibase.dropAll();
            liquibase.update(context);

        } catch (Exception ex) {
            throw new RuntimeException("Error during database initialization", ex);
        } finally {
            if (hsqlConnection != null)
                try {
                    hsqlConnection.close();
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
        }
	}

}
