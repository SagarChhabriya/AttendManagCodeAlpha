
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;


public class Submit extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sampledb", "root", "root")) {

                Enumeration<String> parameterNames = request.getParameterNames();
                String[] ids= request.getParameterValues("IDNumber");
                String present = request.getParameter("Attendance");
                String absent = request.getParameter("Absent");
                Enumeration<String> Names = request.getAttributeNames();


                while (parameterNames.hasMoreElements()) {

                    String paramName = parameterNames.nextElement();


                    if (paramName.startsWith("attendance-")) {

                        int studentId = Integer.parseInt(paramName.replace("attendance-", ""));

                        String attendanceStatusValue = request.getParameter(paramName);


                        // Retrieve student details from corresponding form fields//note their names are specified in the doGet
                        int id = Integer.parseInt(request.getParameter("id-" + studentId));
                        String firstName = request.getParameter("first-name-" + studentId);
                        String lastName = request.getParameter("last-name-" + studentId);
                        String department = request.getParameter("department-" + studentId);
                        String attendanceStatus = attendanceStatusValue; // Use the extracted attendance status value

                        // Insert attendance record into the database
                        String insertSQL = "INSERT INTO APP.ATTENDANCE (ID, FIRST_NAME, LAST_NAME, DEPARTEMENT, ATTENDANCE) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                            preparedStatement.setInt(1, id);
                            preparedStatement.setString(2, firstName);
                            preparedStatement.setString(3, lastName);
                            preparedStatement.setString(4, department);
                            preparedStatement.setString(5, attendanceStatus);
                            preparedStatement.executeUpdate();
                        }
                    }
                }
            }
            response.sendRedirect("AttendancePage.html");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
            }
        }
    }
