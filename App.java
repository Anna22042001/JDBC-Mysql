package com.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;
public class App 
{
    public static void main( String[] args )
    {
        int cmdNumber=0;
        String id, pw, dbname;

        Scanner scan = new Scanner(System.in);
        System.out.print("type your DB name : ");
        dbname = scan.nextLine();
        System.out.print("type your ID : ");
        id = scan.nextLine();
        System.out.print("type your PW : "); 
        pw = scan.nextLine();

        PreparedStatement pstmt = null;	
        ResultSet rs = null;

        Connector con = new Connector(dbname, id, pw);
        System.out.print("Your command: ");
        cmdNumber = scan.nextInt();
        try(Connection curCon = con.getCon(); Statement stmt = curCon.createStatement();)
        {
            while(true)
            {        
                System.out.println(""); 
                System.out.println("Select commands (1) Problem 1, (2) Problem 2, (3) Problem 3, (4) Problem 4, (5) Problem 5 : ");

                if(cmdNumber == 1)
                {
                    System.out.print("Speed: ");
                    Float speed = scan.nextFloat();
                    System.out.print("Ram: ");
                    Integer ram = scan.nextInt();
                    System.out.print("Hard drive: ");
                    Integer hd = scan.nextInt();
                    System.out.print("Screen: ");
                    Float screen = scan.nextFloat();
                    System.out.print("Price: ");
                    Integer price = scan.nextInt();
                    PreparedStatement pStatement = curCon.prepareStatement("SELECT * FROM laptop NATURAL JOIN product WHERE speed >= ? AND ram >= ? AND hd >= ? AND screen >= ? AND price <= ?");
                    pStatement.setFloat(1, speed);
                    pStatement.setInt(2, ram);
                    pStatement.setInt(3, hd);
                    pStatement.setFloat(4, screen);
                    pStatement.setInt(5, price);
                    ResultSet result = pStatement.executeQuery();

                    while(result.next()){
                        System.out.println(result.getString("speed") + " " +result.getString("ram") + " " + result.getInt("hd") + " " + result.getFloat("screen")+ " " + result.getInt("price"));
                    }
                    break;
                }
                
                else if(cmdNumber == 2)
                {
                    System.out.print("Maker: ");
                    String maker1 = new Scanner(System.in).nextLine();
                    System.out.print("Model: ");
                    Integer model = scan.nextInt();
                    System.out.print("Speed: ");
                    Float speed = scan.nextFloat();
                    System.out.print("Ram: ");
                    Integer ram = scan.nextInt();
                    System.out.print("Hard drive: ");
                    Integer hd = scan.nextInt();
                    System.out.print("Price: ");
                    Integer price = scan.nextInt();
                    PreparedStatement pStatement = curCon.prepareStatement("SELECT * FROM product WHERE model = ?");
                    pStatement.setInt(1, model);
                    ResultSet result = pStatement.executeQuery();
                    
                    if (result.next()){
                        System.out.println("WARNING!!!");
                        
                        }

                    else {
                        PreparedStatement pStatement2 = curCon.prepareStatement("insert into pc values(?, ?, ?, ?, ?)");
                        pStatement2.setInt(1, model);
                        pStatement2.setFloat(2, speed);
                        pStatement2.setInt(3, ram);
                        pStatement2.setInt(4, hd);
                        pStatement2.setInt(5, price);
                        pStatement2.executeUpdate();
                        PreparedStatement pStatement3 = curCon.prepareStatement("insert into product values(?, ?, ?)");
                        pStatement3.setString(1, maker1);
                        pStatement3.setInt(2, model);
                        pStatement3.setString(3, "pc");
                        pStatement3.executeUpdate();

                    }
                    System.out.println("");
                    ResultSet rst1 = stmt.executeQuery("SELECT * FROM product");
                    ResultSetMetaData rsmd1 = rst1.getMetaData();
                    int columnsnumber = rsmd1.getColumnCount();
                    System.out.println("Product table");
                    while(rst1.next()){
                        for(int i = 1; i<=columnsnumber; i++){
                            System.out.print(rst1.getString(i) + " ");
                            
                        }
                        System.out.println("");
                    }
                    System.out.println("");
                    System.out.println("PC table");
                    rst1 = stmt.executeQuery("SELECT * FROM pc");
                    rsmd1 = rst1.getMetaData();
                    int columnsnumber2 = rsmd1.getColumnCount();
                    while(rst1.next()){
                        for(int i = 1; i<=columnsnumber2; i++){
                            System.out.print(rst1.getString(i) + " ");
                        }
                        System.out.println("");
                    }
                    // while(result.next()){
                    //     System.out.println("ttttt");
                    //     System.out.println(result.getInt(1));
                    // }
                    break;
                }

                else if(cmdNumber == 3)
                {
                    System.out.print("Desired price: ");
                    Integer desired_price = scan.nextInt();
                    Integer min_difference = desired_price;
                    ResultSet rst2 = stmt.executeQuery("select price from pc");
                    while(rst2.next()){
                        
                        min_difference = Math.min(Math.abs(rst2.getInt("price")-desired_price), min_difference);
                    }
                    
                    ResultSet rst3 = stmt.executeQuery("select * from pc natural join product");
                    while(rst3.next()){
                        if (Math.abs(rst3.getInt("price") - desired_price) == min_difference){
                            System.out.println(rst3.getString("maker") + " " + rst3.getInt("model") + " " + rst3.getInt("ram"));
                        }
                    }
                    break;

                }

                else if(cmdNumber == 4)
                {
                    System.out.print("Maker: ");
                    String maker = new Scanner(System.in).nextLine();
                    PreparedStatement preparedStatement4 = curCon.prepareStatement("SELECT * FROM pc WHERE model IN (SELECT model FROM product WHERE maker = ?)");
                    preparedStatement4.setString(1, maker);
                    ResultSet rsSet4 = preparedStatement4.executeQuery();
                    while(rsSet4.next()){
                        System.out.println(rsSet4.getInt("model") + " " + rsSet4.getFloat("speed") + " " + rsSet4.getInt("ram") + " " +rsSet4.getInt("hd") + " "+rsSet4.getInt("price"));
                    }
                    PreparedStatement preparedStatement5 = curCon.prepareStatement("SELECT * FROM laptop WHERE model IN (SELECT model FROM product WHERE maker = ?)");
                    preparedStatement5.setString(1, maker);
                    ResultSet rsSet5 = preparedStatement5.executeQuery();
                    while(rsSet5.next()){
                        System.out.println(rsSet5.getInt("model") + " " + rsSet5.getFloat("speed") + " " + rsSet5.getInt("ram") + " " + rsSet5.getInt("hd") + " " + rsSet5.getFloat("screen") + " " + rsSet5.getInt("price"));
                    }
                    PreparedStatement preparedStatement6 = curCon.prepareStatement("SELECT * FROM printer WHERE model IN (SELECT model FROM product WHERE maker = ?)");
                    preparedStatement6.setString(1, maker);
                    ResultSet rsSet6 = preparedStatement6.executeQuery();
                    while(rsSet6.next()){
                        System.out.println(rsSet6.getInt("model") + " " + rsSet6.getInt("color") + " " + rsSet6.getString("type") + " " + rsSet6.getInt("price"));
                    }
                    break;
            } else{
                    System.out.print("Budget: ");
                    Integer budget = scan.nextInt();
                    System.out.print("Min speed: ");
                    Float min_speed = scan.nextFloat();
                    Integer min_pc_price = budget + 1;
                    Integer min_pc_model = 0;
                    PreparedStatement preparedStatement7 = curCon.prepareStatement("select * from pc where speed >= ?");
                    preparedStatement7.setFloat(1, min_speed);
                    
                    ResultSet rsSet7 = preparedStatement7.executeQuery();
                    while(rsSet7.next()){
                        Integer price = rsSet7.getInt("price");
                        Integer model = rsSet7.getInt("model");
                        if (price < min_pc_price){
                            min_pc_price = price;
                            min_pc_model = model;
                        }
                    }
                    Integer min_printer_price_nocolor = budget - min_pc_price + 1;
                    Integer min_printer_price_color = budget - min_pc_price + 1;
                    PreparedStatement preparedStatement8 = curCon.prepareStatement("select * from printer where price <= ?");
                    preparedStatement8.setFloat(1, budget - min_pc_price);
                    
                    ResultSet rsSet8 = preparedStatement8.executeQuery();
                    while(rsSet8.next()){
                        Integer price = rsSet8.getInt("price");
                        Integer color = rsSet8.getInt("color");
                        if ((color == 1) && (price < min_printer_price_color)){
                            min_printer_price_color = price;
                        }
                        else if ((color == 0) && (price < min_printer_price_nocolor))
                            min_printer_price_nocolor = price;
                    }
                    PreparedStatement preparedStatement9 = curCon.prepareStatement("select * from printer where price <= ?");
                    preparedStatement9.setFloat(1, budget - min_pc_price);
                    ResultSet rsSet9 = preparedStatement8.executeQuery();
                    if (min_printer_price_color < budget - min_pc_price + 1){
                        while(rsSet9.next()){
                            Integer price = rsSet9.getInt("price");
                            Integer color = rsSet9.getInt("color");
                            Integer model = rsSet9.getInt("model");
                            if ((color == 1) && (price == min_printer_price_color)){
                                System.out.println(min_pc_model + " " + model);
                            }
                        }
                    }else{
                        while(rsSet9.next()){
                            Integer price = rsSet9.getInt("price");
                            Integer color = rsSet9.getInt("color");
                            Integer model = rsSet9.getInt("model");
                            if ((color == 0) && (price == min_printer_price_nocolor)){
                                System.out.println(min_pc_model + " " + model);
                            }
                        }
                    }
                    break;
            }
        }
    }
        catch (SQLException s) {
            
            System.out.print(s.getMessage());
        }
    }
}

class Connector {

    private Connection con;
    private String url;
    private String id;
    private String pwd;

    public Connector(String dbname, String id, String pwd){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.url = "jdbc:mysql://localhost:3306/" + dbname + "?characterEncoding=UTF-8&serverTimezone=UTC";
            this.id = id;
            this.pwd = pwd;
        } catch (ClassNotFoundException e){ }

        try {
            System.out.println("loading Connection...");
            this.con = DriverManager.getConnection(this.url, this.id, this.pwd);
            System.out.println("Connection success!");
        } catch (SQLException s){ 
            System.out.println(s.getMessage());
            System.out.print(s.getSQLState());
        }
    }

    public Connection getCon(){
        return this.con;
    }

    public void setCon(String newDbname, String newId, String newPwd){
        
        this.url = "jdbc:mysql://localhost:3306/" + newDbname + "?characterEncoding=UTF-8&serverTimezone=UTC";
        // mysql 8 이상을 사용할 때에 serverTimezone을 명시하지 않으면 error 발생
        this.id = newId;
        this.pwd = newPwd;

        try {
            this.con = DriverManager.getConnection(this.url, this.id, this.pwd);
        } catch (SQLException s) {             
            System.out.println(s.getMessage());
            System.out.print(s.getSQLState());
        }
    }

    public void closeCon(){
        try{
            this.con.close();
            System.out.println("connection is closed.");
            System.out.println(this.con.isClosed());
        } catch (SQLException s){ }
    }
}

