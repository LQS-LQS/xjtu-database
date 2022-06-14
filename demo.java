import jdk.swing.interop.SwingInterOpUtils;

import  java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.sql.*;

public class demo {
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    //static final String DB_URL = "jdbc:postgresql://121.36.77.42:26000/mydb?ApplicationName=app1&useUnicode=true&characterEncoding=GBK&allowEncodingChanges=true";
    static final String DB_URL = "jdbc:postgresql://121.36.77.42:26000/mydb?ApplicationName=app1&useUnicode=true&characterEncoding=utf8";
    static final String USER = "lqs";
    static final String PASS = "abcd@123";
    public static String[] studentArr = {};
    public static String[] courseArr = {};
    public static String[] student5000Arr = {};
    public static String[] course1000Arr = {};
    public static String[] teacher1000Arr = {};
    public static HashSet snoSet = new HashSet<String>();
    public static HashSet SCset = new HashSet<String>();
    public static String[] sno5000Arr = {};
    public static String getRandomSno(){
        Random random = new Random();
        String str = "";
        for(int i=0;i<8;i++)
            str += random.nextInt(10);
        return str;
    }
    public static String getRandomSex(){
        Random random = new Random();
        int num = random.nextInt(10);
        String sex;
        if(num%2==0)    sex =  "男";
        else sex =  "女";
        try {
            return new String(sex.getBytes("UTF-8"),"UTF-8");
        }catch (Exception e){

        }
       return "";
    }
    public static String getRandomBdate(){
        String[] year = {"1997","1998","1999","2000","2001","2002","2003"};
        String[] month = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        String[] date = {"01","02","03","04","05","06","07","08","09","10",
                         "11","12","13","14","15","16","17","18","19","20",
                         "21","22","23","24","25","26","27","28"};

        Random random = new Random();
        int randomYear = random.nextInt(year.length);
        int randomMonth = random.nextInt(month.length);
        int randomDate = random.nextInt(date.length);

        return year[randomYear] + "-" + month[randomMonth] + "-" + date[randomDate];

    }
    public static double getRandomHeight(){
        //生成1.50到1.90的身高(小数点后两位)
        Random random = new Random();
        double randomHeight = random.nextDouble() * (1.90-1.50) + 1.50;
        String str = new DecimalFormat("0.00").format(randomHeight);
        return Double.parseDouble(str) ;
    }
    public static String getRandomDorm(){
        Random random = new Random();
        String[] position = {"东","南","西","北"};
        int randomPosition = random.nextInt(position.length);
        String dormNum = "";
        dormNum += random.nextInt(10);//楼层
        dormNum += random.nextInt(3);
        dormNum += random.nextInt(10);
        String dorm = position[randomPosition] + random.nextInt(21) + "舍" + dormNum;
        try {
            return new String(dorm.getBytes("UTF-8"),"UTF-8");
        }catch (Exception e){

        }
        return "";
    }
    public static int getRandomPeriod(){
        int[] arr = {20,30,40,50,60,70,80,90,100,110,120};
        Random random = new Random();
        int randomIndex = random.nextInt( arr.length );
        return arr[randomIndex];
    }
    public static double getRandomCredit(int period){
        String str = new DecimalFormat("0.0").format(period*1.0/20);
        return Double.parseDouble(str);
    }
    public static String getRandomSnoInSC(){
        Random random = new Random();
        int index = random.nextInt(sno5000Arr.length);
        return sno5000Arr[index];
    }
    public static String getRandomCnoInSC(){
        Random random = new Random();
        int index = random.nextInt(course1000Arr.length);
        String[] temp = course1000Arr[index].split("#\\u0024");
        return temp[1];
    }
    public static double getRandomGrade(){
        //返回50到100的随机成绩

        Random random = new Random();
        int randomTimes = random.nextInt(100) + 100 ;//生成一个100到200的随机数
        return randomTimes*0.5;
    }
    public static void main(String[] args){
        String  sqlOfS = "INSERT INTO S634\n VALUES\n",
                sqlOfC = "INSERT INTO C634\n VALUES\n",
                sqlOfSC = "INSERT INTO SC634\n VALUES\n";
        //读取学生文件
        try {
            File studentFile = new File("C:\\Users\\AIERXUAN\\Desktop\\STUDENT.txt");
            BufferedReader br = new BufferedReader(new FileReader(studentFile));
            String st;
            String studentStr = "";
            while( ( st=br.readLine() ) != null ){
                studentStr += st+",";
            }
            studentArr = studentStr.split(",");
            System.out.println(studentArr.length);
        }catch (FileNotFoundException e){
            System.out.println("FileNotFoundException异常");
        }catch (IOException e){
            System.out.println("IOException异常");
        }

        //读取课程文件
        try {
            File courseFile = new File("C:\\Users\\AIERXUAN\\Desktop\\COURSE.txt");
            BufferedReader br = new BufferedReader(new FileReader(courseFile));
            String st;
            String courseStr = "";
            while( ( st=br.readLine() ) != null ){
                courseStr += st+",";
            }
            courseArr =courseStr.split(",");
            System.out.println(courseArr.length);
        }catch (FileNotFoundException e){
            System.out.println("FileNotFoundException异常");
        }catch (IOException e){
            System.out.println("IOException异常");
        }

        //读入已经存在的学号文件，防止重复
        try {
            File snoFile = new File("C:\\Users\\AIERXUAN\\Desktop\\temp.txt");
            BufferedReader br = new BufferedReader(new FileReader(snoFile));
            String st;
            String snoStr = "";
            while( ( st=br.readLine() ) != null ){
                snoStr += st+",";
            }
            String[] snoArr = snoStr.split(",");
            for(int i=0;i<snoArr.length;i++){
                snoSet.add(snoArr[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //新建字符串进行插入，插入4000个学生，900个课程，30000个SC，同时写入到学好文件
        try{
            student5000Arr = Arrays.copyOfRange(studentArr,1000,5000);//1000-5000
            course1000Arr = Arrays.copyOfRange(courseArr,100,1000);//100-1000
            teacher1000Arr = Arrays.copyOfRange(studentArr,studentArr.length-1000,studentArr.length-100);//倒数后900个是充当老师
            sno5000Arr = Arrays.copyOfRange(studentArr,0,4000);//只是分配空间而已
            System.out.println(student5000Arr.length);//读了4000个学生的姓名
            System.out.println(course1000Arr.length);//读了900个课程的cno
            BufferedWriter out;
            out = new BufferedWriter(    new OutputStreamWriter( new FileOutputStream("C:\\Users\\AIERXUAN\\Desktop\\temp.txt"), StandardCharsets.UTF_8)   );
            //4000个学生的随机信息
            for(int i=0;i<4000;i++){
                String  sno = getRandomSno(),
                        sname = student5000Arr[i],
                        sex = getRandomSex(),
                        bdate = getRandomBdate(),
                        dorm = getRandomDorm();
                double height = getRandomHeight();
                if(sname==null || sname=="" || sname.equals("")) System.out.println(i);
                //处理sno重复的问题
                if( snoSet.contains(sno) ){
                    i--;
                    continue;
                }  else {
                    snoSet.add(sno);
                    sno5000Arr[i] = sno;
                    out.write(sno5000Arr[i] + "\n");
                }
                String singleInsertIntoS;
                if(i!=3999){
                    singleInsertIntoS =  "('" + sno + "','" + sname + "','" + sex + "','" + bdate + "'," + height + ",'" + dorm + "'),\n";
                }else{
                    singleInsertIntoS =  "('" + sno + "','" + sname + "','" + sex + "','" + bdate + "'," + height + ",'" + dorm + "')";
                }

                sqlOfS += singleInsertIntoS;
                //System.out.println( sno   + "," + sname  + "," + sex + "," + bdate + "," + height + "," + dorm  );
            }
            out.flush();
            out.close();
        }catch (Exception e){
            System.out.println("Exception");
            e.printStackTrace();
        }






        //900个课程的随机信息
        for(int i=0;i<900;i++){
            String[] temp = course1000Arr[i].split("#\\u0024");
            String  cno = temp[1],
                    cname = temp[0];

            int period = getRandomPeriod();
            double credit = getRandomCredit(period);
            String teacher = teacher1000Arr[i];

            String singleInsertIntoC;
            if(i!=899){
                singleInsertIntoC = "('" + cno + "','" + cname + "'," + period + "," + credit + ",'" + teacher + "'),\n";
            }else{
                singleInsertIntoC = "('" + cno + "','" + cname + "'," + period + "," + credit + ",'" + teacher + "')";
            }

            sqlOfC += singleInsertIntoC;
            //System.out.println(  cno + "," + cname + "," + period + "," + credit + "," + teacher );
        }

        //25000个SC
        for(int i=0; i<25000; i++){
            String  sno = getRandomSnoInSC(),
                    cno = getRandomCnoInSC();
            double grade = getRandomGrade();

            //处理SC重复的问题
            if( SCset.contains(sno+cno) ){
                i--;
                continue;
            }  else {
                SCset.add(sno+cno);
            }

            String singleInsertIntoSC;
            if(i!=24999){
                singleInsertIntoSC = "('" + sno + "','" + cno + "'," + grade + "),\n";
            }else{
                singleInsertIntoSC = "('" + sno + "','" + cno + "'," + grade + ")";
            }
            sqlOfSC += singleInsertIntoSC;
            //System.out.println( sno + "," + cno + "," + grade);
        }


        Connection conn = null;
        Statement stmt = null;
        //读取S表的学号
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql="SELECT SNO FROM S634";


            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                String sno = rs.getString("sno");
                snoSet.add(sno);
                // 输出数据
                //System.out.print("sno: " + sno + "\n");
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
                System.out.println("se2 error");
            }// 什么都不做

            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }




        /*----------------------------------------------------------------------------------------------------------*/
        //插入到S表
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;

            sql = sqlOfS;
            ResultSet rs = stmt.executeQuery(sql);

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
                System.out.println("se2 error");
            }// 什么都不做

            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        //插入到C表
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;

            sql = sqlOfC;
            ResultSet rs = stmt.executeQuery(sql);

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
                System.out.println("se2 error");
            }// 什么都不做

            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        /*插入到SC表*/
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;

            sql = sqlOfSC;
            ResultSet rs = stmt.executeQuery(sql);

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
                System.out.println("se2 error");
            }// 什么都不做

            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

}
