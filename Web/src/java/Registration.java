/*
   * This is the java servlet which is called when the 'Register' button of the AddStudent.html web-page.
   * This servlet includes the implementations of the core java i.e., the use of Java Networking. It handles
   * the 'Request' and sends the request to the server in this case to the Apache Tomcat 10 Server to 
   * make the dynamic web project and make it live.
*/



import jakarta.servlet.*; //the astrisk is used to import all the classes/sub-packages of the servlet(sub-packge of the jakarata)
import jakarta.servlet.http.*;
import java.awt.HeadlessException;
import java.sql.*;
import java.io.*;
import javax.swing.JOptionPane;
public class Registration extends HttpServlet{
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
           /*The parameters are the names of the input fields  of the AddStudent.html's form which is defined in the main body.*/
        response.setContentType("text/html");
        PrintWriter  pw = response.getWriter();
        String id = request.getParameter("id");
        String firstName = request.getParameter("fn");
        String lastName = request.getParameter("ln");
        String dept = request.getParameter("dept");
        
        try {
            /*The try block handles the Checked Exceptions when the Connection call is made to the derby driver class*/
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            /*
            *    This is the try-with-resources statement. It initialize the connection (reference variable) with the connection to the sampledb schema of SQL Server Flavor
            *   root is the name of user and the third parameter is the password which is also root. Note: The Connection is interface and connection is object.
            */
            try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sampledb","root","root")) {
                /*statement is the reference variable of the Statement interface.*/
                 Statement statement=connection.createStatement();
                
                 /*PreparedStatement is an interface in the SQL package and the statement in this case inserts the students into the APP.STUDENTS table*/
                PreparedStatement ps = connection.prepareStatement("insert into APP.STUDENTS values(?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(id));
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, dept);
                int i = ps.executeUpdate();
                
        /* Redirect back to the index/home page. Invoked after the data is entered to the Table*/
             response.sendRedirect(request.getContextPath() + "/index.html");
             /*This statement displays the Message Dialog box that i records interted. i is initialized by the preparedStatment when its update is executed*/
                JOptionPane.showMessageDialog(null,  i + " Records Inserted Successfully.");
            }
              
            
        } 
        /*This catch block is designed to catch the multiple Excpetion instead of defiinging the separate catch blocks for the each exception.
        * The | is known as pipe operator which allows the catch block to make a pipeline of the different exceptions.
        */
        catch (HeadlessException | ClassNotFoundException | NumberFormatException | SQLException e) {
            /*This Dialog box will prompt the exception message.*/
           JOptionPane.showMessageDialog(null,e);
           
        }
        
    }

}