package servlet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import java.util.ArrayList;
import java.util.List;


/*
*classe StudentDAO: permet de gerer l 'acces a la bdd pour les étudiants
 */
public class StudentDAO {
    private static final MongoDatabase database = MongoConnexion.getDatabase();
    private static final MongoCollection<Document> collection = database.getCollection("students");



    /**
     * getAllStudent: recupere tout les étudiants de la base
     * @return La liste des etudiant sous forme d'objet
     */
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        FindIterable<Document> documents = collection.find();

        for (Document doc : documents) {
            students.add(new Student(doc.getInteger("id"), doc.getString("nom"), doc.getString("prenom"), doc.getDate("date_naissance"), doc.getInteger("numero"), doc.getString("adresse_mail")));
        }
        return students;
    }

    /**
     * Recherche d'un etudiant particulier
     * @param id identification de l'etudiant
     * @return l'objet correspondont , ou null si rien n'est trouvé
     */
    public static Student getStudentById(int id) {
        Document doc = collection.find(Filters.eq("id", id)).first();
        if (doc == null) return null;
        return new Student(
                doc.getInteger("id"),
                doc.getString("nom"),
                doc.getString("prenom"),
                doc.getDate("date_naissance"),
                doc.getInteger("numero"),
                doc.getString("adresse_mail")
        );
    }

    /**
     * Ajouter un nouvelle étudiant dans la bdd
     * @param student objet a inserer
     */
    public static void addStudent(Student student) {
        Document doc = new Document("id", student.getId())
                .append("nom", student.getNom()).append("prenom", student.getPrenom()).append("date_naissance", student.getDate_naissance()).append("numero", student.getNumero()).append("adresse_mail", student.getAdresse_mail());
        collection.insertOne(doc);
    }

    /**
     * Mise a jour d'un etudiant existant dans la bdd
     * @param student objet mis a jour
     */
    public static void updateStudent(Student student) {
        Document updatedDoc = new Document("id", student.getId())
                .append("nom", student.getNom())
                .append("prenom", student.getPrenom())
                .append("date_naissance", student.getDate_naissance())
                .append("numero", student.getNumero())
                .append("adresse_mail", student.getAdresse_mail());
        collection.replaceOne(Filters.eq("id", student.getId()), updatedDoc);
    }

    /**
     * Supprime un etudiant par rapport a l'id
     * @param id identifiant unique de l'etudiant
     */
    public static void deleteStudent(int id) {
        collection.deleteOne(Filters.eq("id", id));
    }
}

