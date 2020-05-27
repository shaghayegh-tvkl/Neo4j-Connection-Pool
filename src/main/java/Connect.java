/**
 * @author Shaghayegh on 5/23/2020
 */

import org.neo4j.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.neo4j.driver.*;
import org.neo4j.driver.net.ServerAddress;

import static org.neo4j.driver.SessionConfig.builder;
import static org.neo4j.driver.Values.parameters;

public class Connect {
    static String Neo4jServerIP = "";
    static String virtualUri = "bolt://" + Neo4jServerIP + ":7687";
    static String username = "";
    static String password = "";
    static Driver driver;

    static private Driver createDriver() {
        Config config = Config.builder()
                .withMaxConnectionLifetime(30, TimeUnit.MINUTES)
                .withMaxConnectionPoolSize(50)
                .withConnectionAcquisitionTimeout(2, TimeUnit.MINUTES)
                .build();

        return GraphDatabase.driver(virtualUri, AuthTokens.basic(username, password), config);
    }

    static private List<Record> getStudents(String id, Driver driver) {
        List<Record> students = null;
        try (Session session = driver.session()) {
            Result result;
            String query = "MATCH (s1:student {student_id:'" + id + "' })-[c:CHAT]->(s2:student),(s2:student)-[c1:CHAT]->(s1:student{student_id:'" + id + "' })  \n"
                    + " RETURN s2.phone_number AS phone_number";

            result = session.run(query);

            students = result.list();
            return students;

        } catch (Exception exception) {
            exception.printStackTrace();
            return students;
        }
    }

    public static void main(String[] args) {
        String id = new Scanner(System.in).nextLine();
        driver = createDriver();
        List<Record> students = getStudents(id, driver);
        for(Record student: students){
            System.out.println(student);
        }
    }
}