package TextBoard;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DB_Connect {
    private Connection conn = null;
    private Statement ps = null;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static final String URL = "jdbc:mysql://localhost:3306/textboard";


    public DB_Connect()
    {
        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            ps = conn.createStatement();
        }
        catch (Exception e)
        {
            try {
                assert conn != null;
                conn.close();
            }
            catch (SQLException se)
            {
                System.out.println("DB 연동 실패");
            }

        }
    }

    public boolean insert(String table,  ArrayList<Datas> datas) {

        try{
            String query = "insert into " + table + " values (";
            for(var data : datas) {
                if(data.getData() == null)
                {
                    query += "null,";
                    continue;
                }

                String str = data.getData().getClass().getName();
                if(str.equals("java.lang.String") ||
                        str.equals("java.time.LocalDateTime"))
                {
                    query += "'" + data.getData() + "',";
                }
                else{
                    query +=  data.getData()+ ",";
                }
            }
            query = query.substring(0,query.length() - 1);

            query += ")";

            ps.executeUpdate(query);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public ArrayList<String[]> select(String query, int size)
    {
        ArrayList<String[]> datas = new ArrayList<>();
        try
        {
            ResultSet res = ps.executeQuery(query);


            int idx;
            while (res.next()) {
                String[] str = new String[size];
                idx = 1;
                while (idx <= size)
                {
                    str[idx -1] = res.getString(idx);
                    idx++;
                }
                datas.add(str);
            }
        }
        catch (Exception e)
        {
            datas = null;
        }
        return datas;
    }
}


class Datas<T>
{
    private T data;

    public Datas(String str) {
        data = (T) str;
    }

    public Datas(T d)
    {
        this.data = d;
    }

    public T getData() {
        return data;
    }
}