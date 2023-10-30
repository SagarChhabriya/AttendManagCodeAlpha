/*
*   This servlet creates a member class i.e., the static inner class. It handles the connection with the database and retrieves the data from the APP.STUDENTS
*   Table and inserts it in the HTML table which is visible to the user. It also defines the accessors i.e., the getter methods.
*/

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

    public class Attendance extends HttpServlet {
    //    member class
        static class Student {
            private final int id;
            private final String firstName;
            private final String lastName;
            private final String department;

            public Student(int id, String firstName, String lastName, String department) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.department = department;
            }


            public int getId() {
                return id;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public String getDepartment() {
                return department;
            }
        }
            private ArrayList<Student> students; // Declare students variable at the class level

        @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sampledb", "root", "root")) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM APP.STUDENTS");

                ArrayList<Student> students = new ArrayList<>();

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String firstName = resultSet.getString("FIRST_NAME");
                    String lastName = resultSet.getString("LAST_NAME");
                    String department = resultSet.getString("DEPARTEMENT");

                    students.add(new Student(id, firstName, lastName, department));
                }
                /*Create a web page inside the Servlet.*/

                out.println("<html><head><title>Submit | Attendance Management System</title></head><body>");
                out.println("<style>");
                out.println("body { background: linear-gradient(to bottom, #95CEEB, #f0f0f0); display: flex; flex-direction: column; justify-content: flex-start; align-items: center; height: 100vh; margin: 0; }");
                out.println("header { background-color: #37474F; padding: 10px; text-align: center; width: 100%; }");
                out.println("header h1 { margin: 0; color: #fff; font-size: 24px; font-weight: bold; color: white; text-shadow: 2px 2px 4px #000000; }");
                out.println("nav { background-color: #37474F; text-align: center; width: 100%; }");
                out.println("ul { list-style-type: none; padding: 0; }");
                out.println("li { display: inline; margin: 0 10px; }");
                out.println("li a { color: #fff; text-decoration: none; font-weight: bold; font-size: 18px; transition: color 0.3s; }");
                out.println("li a:hover { color: #87CEEB; }");
                out.println("#Homepage { background-color: #fff; padding: 20px; border-radius: 15px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.2); text-align: center; width: 70%; margin-top: 40px; }");
                out.println("#Homepage h2 { font-size: 24px; color: #37474F; }");
                out.println("#Homepage p { font-size: 32px; color: #555; }");
                out.println("table { font-family: arial, sans-serif; border-collapse: collapse; width: 100%; margin-top: 10px; }");
                out.println("td, th { border: 2px solid #dddddd; text-align: left; padding: 8px; }");
                out.println("tr:nth-child(even) { background-color: #dddddd; }");
                out.println("</style>");
                out.println("<header>\n<h1>Attendance Management System</h1>\n</header>");
                out.println("""
                             <nav>
                                    <ul>
                                        <li><a href="index.html">Home</a></li>
                                        <li><a href="AddStudent.html">Add Student</a></li>
                                        <li><a href="AttendancePage.html">Attendance</a></li>
                                    </ul>
                                </nav>""");
                out.println("<table>");
                out.println("<tr><th style='width:5%'>IDNumber</th><th style='width:25%'>FirstName</th><th style='width:25%'>LastName</th><th style='width:25%'>Department</th><th style='width:20%'>Attendance</th></tr>");

                /*Extract all the records from the APP.STUDENTS table and insert it in the HTML Table*/
                            for (Student student : students) {
                    out.println("<tr>");
                    out.println("<td class='student-id'>" + student.getId() + "</td>");
                    out.println("<input type='hidden' name='id-" + student.getId() + "' value='" + student.getId() + "'>");
                    out.println("<td class='first-name'>" + student.getFirstName() + "</td>");
                    out.println("<input type='hidden' name='first-name-" + student.getId() + "' value='" + student.getFirstName() + "'>");
                    out.println("<td class='last-name'>" + student.getLastName() + "</td>");
                    out.println("<input type='hidden' name='last-name-" + student.getId() + "' value='" + student.getLastName() + "'>");
                    out.println("<td class='department'>" + student.getDepartment() + "</td>");
                    out.println("<input type='hidden' name='department-" + student.getId() + "' value='" + student.getDepartment() + "'>");
                    out.println("<td>");
                    out.println("<input type='checkbox' name='attendance-" + student.getId() + "' value='Present'>Present");
                    out.println("<input type='checkbox' name='attendance-" + student.getId() + "' value='Absent'>Absent");
                    out.println("</td>");
                    out.println("</tr>");
                }

                out.println("</table>");

              out.println("<form action=\"Attendance\" method=\"get\" style=\"display: inline; margin-right: 10px;\">");
              out.println("<input type=\"submit\" value=\"Refresh\" style=\"background-color: #4CAF50; color: white; padding: 10px 15px; border: none; cursor: pointer;\">");
              out.println("</form>");

              out.println("<form action=\"Submit\" method=\"post\" style=\"display: inline; margin-right: 10px;\">");
              out.println("<input type=\"submit\" value=\"Submit Attendance\" style=\"background-color: #008CBA; color: white; padding: 10px 15px; border: none; cursor: pointer;\">");
              out.println("</form>");


                // Back button to return to the previous page
              out.println("<a href=\"AttendancePage.html\" style=\"display: inline; text-decoration: none;\">\n<button style=\"background-color: #f44336; color: white; padding: 10px 15px; border: none; cursor: pointer;\">Back</button>\n</a>");

                out.println("</body></html>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    }



