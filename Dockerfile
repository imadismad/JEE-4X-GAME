# Use the official Tomcat image with Java 17
FROM tomcat:10.1-jre17

# Set the working directory (optional but helpful for clarity)
WORKDIR /usr/local/tomcat

# Clean the default Tomcat webapps folder to remove default applications
RUN rm -rf webapps/*

# Copy your WAR file (app.war) to Tomcat's webapps directory
COPY target/app.war /usr/local/tomcat/webapps/

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat when the container runs
CMD ["catalina.sh", "run"]
