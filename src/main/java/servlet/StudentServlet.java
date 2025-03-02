package servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.ErrorMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/students/*")
public class StudentServlet extends HttpServlet {


        private final List<Student> students = new ArrayList<>();
        private final Gson gson = new Gson();

        @Override
        public void init() throws ServletException {
            // Ajout d'exemples d'étudiants dans la liste pour tester
            students.add(new Student(1, "Doe"));
            students.add(new Student(2, "Smith"));
        }
//methode get pour lister tout les etudiant et un etudiant avec son id
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            String pathInfo = request.getPathInfo(); // Récupère l'ID s'il est présent

            if (pathInfo == null || pathInfo.equals("/")) {
                // 📌 Retourner tous les étudiants (format JSON avec Gson)
                String json = gson.toJson(students);
                out.print(json);
            } else {
                // 📌 Retourner un étudiant par ID
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    try {
                        int id = Integer.parseInt(pathParts[1]);
                        Student student = students.stream()
                                .filter(s -> s.getId() == id)
                                .findFirst()
                                .orElse(null);

                        if (student != null) {
                            out.print(gson.toJson(student));
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.print(gson.toJson(new ErrorMessage("Étudiant non trouvé")));
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print(gson.toJson(new ErrorMessage("ID invalide")));
                    }
                }


            }
            out.flush();
        }

    //methode post
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader()) {
            Student newStudent = gson.fromJson(reader, Student.class);

            if (newStudent.getId() <= 0 || newStudent.getNom() == null || newStudent.getNom().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new ErrorMessage("Données invalides")));
                return;
            }

            // Vérifier si l'étudiant existe déjà
            boolean exists = students.stream().anyMatch(s -> s.getId() == newStudent.getId());
            if (exists) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write(gson.toJson(new ErrorMessage("Un étudiant avec cet ID existe déjà")));
                return;
            }

            students.add(newStudent);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(newStudent));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorMessage("Erreur interne du serveur")));
        }
    }

//methode  PUT

 //methode Delete
    }
