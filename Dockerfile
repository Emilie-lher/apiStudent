
# Utilisation de l'image officielle de Tomcat
FROM tomcat:10-jdk8-openjdk

# Copier le fichier WAR généré dans le dossier de Tomcat
COPY target/apiStudent-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/apiStudent.war

# Exposer le port 8080 pour accéder à l'API
EXPOSE 8080

# Démarrer Tomcat
CMD ["catalina.sh", "run"]
