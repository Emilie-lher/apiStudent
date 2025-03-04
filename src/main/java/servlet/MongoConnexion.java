package servlet;
import com.mongodb.ConnectionString;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoConnexion {
    private static final String HOST = "mongodb"; // Nom du service dans Docker
    private static final int PORT = 27017;
    private static final String DATABASE_NAME = "studentdb";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "adminpass";
    private static MongoDatabase database;

    static {
        try {
            // Connexion avec authentification
            MongoCredential credential = MongoCredential.createCredential(USERNAME, DATABASE_NAME, PASSWORD.toCharArray());
            MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://" + USERNAME + ":" + PASSWORD + "@" + HOST + ":" + PORT + "/" + DATABASE_NAME + "?authSource=admin"));

            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("✅ Connexion à MongoDB réussie !");
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion à MongoDB : " + e.getMessage());
        }  }

    public static MongoDatabase getDatabase() {
        return database;
    }
}