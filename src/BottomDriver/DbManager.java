package BottomDriver;

import java.io.ByteArrayInputStream;
import java.sql.*;

/**
 * Copyright (C), 20019-2020, HeFei.
 * FileName: FingerManager
 * It's a Data Base Manager to control the StudioCheck Data Base.
 *
 * @author Wispytrace
 * @Date   2019/11/15
 * @version 1.00
 */
public class DbManager {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306";
    private static String DBNAME = "StudioCheck";
    private  Connection connect;

    public void setUrl(String ip_port){
        URL = "jdbc:mysql://"+ ip_port;
    }
    public void setDbname(String dbname){
        DBNAME = dbname;
    }
    public void dbConnect(String user, String password) throws Exception{
        Class.forName(DRIVER);
        connect = DriverManager.getConnection(URL, user, password);
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接失败, 请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        ResultSet ret = statement.executeQuery("show databases like \'" + DBNAME + '\'');
        if (!ret.next()){
            throw new Exception("数据库不存在");
        }
        statement.executeQuery("use "+ DBNAME);
    }

    public void dbClose() throws Exception{
        connect.close();
    }

    public void dbInsert(int id, byte[] name, byte[] sno,int permission, byte[] tutor, byte[] team,
                         int status, byte[] phone, byte[] password) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement sql = connect.prepareStatement("insert into Staff values(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        sql.setInt(1, id);
        sql.setBytes(2, name);
        sql.setBytes(3, sno);
        sql.setInt(4, permission);
        sql.setBytes(5, tutor);
        sql.setBytes(6, team);
        sql.setInt(7, status);
        sql.setBytes(8, phone);
        sql.setBytes(9, password);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, String name, String sno,int permission, String tutor, String team,
                         int status, String phone, String password) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement sql = connect.createStatement();
        name = ",'" + name + "'";
        sno = ",'" + sno + "'";
        tutor = ",'" + tutor + "'";
        team = ",'" + team + "'";
        phone = ",'"+ phone + "'";
        password = ",'" + password + "'";
        sql.execute("insert into Staff values ("+ id + name + sno +","+ permission + tutor + team + "," +
                status + phone + password+")");
    }
    public void dbInsert(String table, String field, String values) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement sql = connect.createStatement();
        sql.execute("insert into " + table + " (" + field+") "+ " values " + "(" + values + ")");
    };
    public void dbInsert(int recordid, int id) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement sql = connect.prepareStatement("insert into AttendanceRecord (recordid, id) values(?, ?)");
        sql.setInt(1, recordid);
        sql.setInt(2, id);
        sql.executeUpdate();
        sql.close();
    }
    public void dbInsert(int id, byte[] template) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }

        PreparedStatement sql = connect.prepareStatement("insert into FingerBase values(?, ?)");
        ByteArrayInputStream templateStream = new ByteArrayInputStream(template);
        sql.setInt(1, id);
        sql.setBlob(2, templateStream);
        templateStream.close();
        sql.executeUpdate();
        sql.close();
    }
    public void dbUpdate(String table, String field, String value, String condition) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        statement.execute("update " + table + " set " + field + "=" + value + " where " + condition);
    }
    public void dbUpdateChar(String table, String field, String value, String condition) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        PreparedStatement statement = connect.prepareStatement("update  "+table+" set "+ field + "=? where " + condition);
        statement.setBytes(1, value.getBytes());
        statement.executeUpdate();
        statement.close();
    }
    public void dbDelete(String table, String field, String value)throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        statement.execute("delete from  " + table + " where " + field + "=" + value);
    }
    public ResultSet dbSearch(String table, String field, String condition ) throws Exception{
        if ((connect == null) || connect.isClosed()){
            throw new Exception("数据库连接已被关闭,请重新打开软件!");
        }
        Statement statement = connect.createStatement();
        ResultSet result = statement.executeQuery("select " + field + " from " + table + " " + condition );
        return result;
    }
    public int dbGetMaxId(String table) throws Exception{
        int result = 0;
        ResultSet resultSet = dbSearch(table, "*", "");
        resultSet.last();
        result = resultSet.getRow()+1;
        return result;
    }
}
