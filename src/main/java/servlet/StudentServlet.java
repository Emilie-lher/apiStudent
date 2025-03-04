package servlet;
import servlet.StudentDAO;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/students/*")
public class StudentServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Retourne tous les étudiants
            List<Student> students = StudentDAO.getAllStudents();
            resp.getWriter().write(gson.toJson(students));
        } else {
            // Récupérer un étudiant par ID
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Student student = StudentDAO.getStudentById(id);

                if (student != null) {
                    resp.getWriter().write(gson.toJson(student));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Étudiant non trouvé\"}");
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"ID invalide\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Student student = gson.fromJson(req.getReader(), Student.class);

        if (student == null || Objects.isNull(student.getId())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Données invalides\"}");
            return;
        }

        if (StudentDAO.getStudentById(student.getId()) != null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"L'étudiant existe déjà\"}");
            return;
        }

        StudentDAO.addStudent(student);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID requis pour la mise à jour\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Student existingStudent = StudentDAO.getStudentById(id);

            if (existingStudent == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Étudiant non trouvé\"}");
                return;
            }

            Student updatedStudent = gson.fromJson(req.getReader(), Student.class);
            updatedStudent.setId(id);
            StudentDAO.updateStudent(updatedStudent);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Étudiant mis à jour avec succès\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID invalide\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID requis pour la suppression\"}");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));

            if (StudentDAO.getStudentById(id) == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Étudiant non trouvé\"}");
                return;
            }

            StudentDAO.deleteStudent(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Étudiant supprimé avec succès\"}");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID invalide\"}");
        }
    }
}
