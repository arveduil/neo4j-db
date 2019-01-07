package pl.edu.agh.ki.bd2;

import java.sql.Connection;
import java.sql.DriverManager;

public class Solution {

    private  GraphDatabase graphDatabase;
    public Solution(GraphDatabase graphDatabase){
        this.graphDatabase = graphDatabase;
    }
    public void databaseStatistics() {
        System.out.println(graphDatabase.runCypher("CALL db.labels()"));
        System.out.println(graphDatabase.runCypher("CALL db.relationshipTypes()"));
    }

    public void runAllTests() {
        System.out.println(findActorByName("Emma Watson"));
        System.out.println(findMovieByTitleLike("Star Wars"));
        System.out.println(findRatedMoviesForUser("maheshksp"));
        System.out.println(findCommonMoviesForActors("Emma Watson", "Daniel Radcliffe"));
        System.out.println(findMovieRecommendationForUser("emileifrem"));
    }

    private String findActorByName(final String actorName) {
       return graphDatabase.runCypher("MATCH (n:Person)\n" +
               "WHERE n.name = '"+actorName+"' \n" +
            "RETURN n "
            );
}

    private String findMovieByTitleLike(final String movieName)
    {
        return graphDatabase.runCypher("MATCH (m:Movie)\n" +
                "WHERE m.title STARTS WITH '"+movieName+"' \n" +
                "RETURN m.title"
        );
    }

    private String findRatedMoviesForUser(final String userLogin) {
        return graphDatabase.runCypher("MATCH (u:User {login: '"+userLogin+"'})\n" +
            "MATCH (u)-[r:RATED]->(m:Movie)\n" +
                "RETURN *;"
        );
    }

    private String findCommonMoviesForActors(String actorOne, String actrorTwo) {
        return graphDatabase.runCypher("MATCH (a1:Actor)-[:ACTS_IN]->(m:Movie)<-[:ACTS_IN]-(a2:Actor)\n" +
                "WHERE a1.name =  '"+actorOne+"' AND a2.name ='"+ actrorTwo + "' \n" +
                "RETURN m.title"
        );
    }

    private String findMovieRecommendationForUser(final String userLogin) {
        return graphDatabase.runCypher("MATCH (u:Person)-[:RATED]->(m:Movie)<-[:DIRECTED]-(d:Director)-[:DIRECTED]->(mr:Movie)\n" +
                "WHERE u.login = 'emileifrem' \n" +
                "RETURN mr.title"
        );
    }

}
