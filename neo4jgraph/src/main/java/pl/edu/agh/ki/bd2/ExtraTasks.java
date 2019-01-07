package pl.edu.agh.ki.bd2;

public class ExtraTasks {
    public static void createNewNodesForActorAndMovie(GraphDatabase graphDatabase) {

        graphDatabase.runCypher("MATCH (a:Actor {name: 'Piotr Boszczyk'})-[r:ACTS_IN]-> (m:Movie {title: 'PioBos'}) DELETE  r,m,a " );
        graphDatabase.runCypher("MATCH (a:Actor {name: 'Piotr Boszczyk'}) DELETE a " );
        graphDatabase.runCypher("MATCH (a:Actor {name: 'Piotr Boszczyk'}) DELETE a " );



       System.out.println(graphDatabase.runCypher("CREATE (m:Movie {title: 'PioBos'})\n" +
                "RETURN m.title"
        ));
        System.out.println(graphDatabase.runCypher("CREATE (m:Actor {name: 'Piotr Boszczyk'})\n" +
                "RETURN m.name"
        ));
    System.out.println(graphDatabase.runCypher("MERGE (a:Actor {name: 'Piotr Boszczyk'})-[:ACTS_IN]-> (m:Movie {title: 'PioBos'})\n"));
    }

    public static void fillNodesWithData(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("MATCH  (a:Actor {name: 'Piotr Boszczyk'})-[:ACTS_IN]-> (m:Movie {title: 'PioBos'}) " +
                                "SET a.birthplace='Kielce',  a.birthday = '640130400000', m.studio = 'Studioo', m.genre='Biography'" +
                "RETURN a, m"));
    }

    public static void actorsWhoPlayedInAtLeast6Movies(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("MATCH  (a:Actor)-[:ACTS_IN]->(m:Movie) " +
                "WITH a, collect(m) as movies \n" +
                "WHERE length(movies) >= 6 RETURN a.name"));
    }

    public static void avgMoviesPlayedForActorsWhoPlayedInAtLeast7Movies(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("MATCH  (a:Actor)-[:ACTS_IN]->(m:Movie) " +
                "WITH a, collect(m) as movies \n" +
                "WHERE length(movies) > 6 " +
                "WITH length(movies) as movies \n" +
                "return avg(movies)"));
    }

    public static void actorsWhoPlayedIn5MoviesAndDirectedAtLeastOne(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("MATCH  (md:Movie)<-[:DIRECTED]-(a:Actor)-[:ACTS_IN]->(m:Movie) " +
                "WITH a, collect(m) as movies \n" +
                "WHERE length(movies) > 4 RETURN a.name, length(movies) ORDER BY length(movies)"));
    }

    public static void usersWhoRatedMovieWith3StarsAtLeast(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("MATCH  p=(u1:User)-[:FRIEND]->(u2:User)-[r:RATED]->(m:Movie) " +
                "WHERE r.stars >= 3 " +
                "RETURN u2.name,m.title ,r.stars"));
    }

    public static void pathBetweenActorsWithoutMovies(GraphDatabase graphDatabase) {
     //   System.out.println(graphDatabase.runCypher("MATCH p=shortestPath( (a:Actor {name: 'Emma Watson'})-[*]-(b:Actor {name: 'Daniel Radcliffe'} ))  "+
    //            "WHERE NONE( n IN nodes(p) WHERE n:Movie)  return p LIMIT 1"));
    }

    public static void measureIndexTime(GraphDatabase graphDatabase){
        long matchTimeDurationWithoutIndex = measureQuery(graphDatabase,"PROFILE MATCH (a:Actor) WHERE a.name ='Piotr Boszczyk' RETURN a.name");
        System.out.println("Match Actor duration without index: \n "+  matchTimeDurationWithoutIndex);

        createIndex(graphDatabase);
        System.out.println("Added index" );

        long matchTimeDurationWithIndex = measureQuery(graphDatabase,"PROFILE MATCH (a:Actor) WHERE a.name ='Piotr Boszczyk' RETURN a.name");
        System.out.println("Match Actor duration with index: \n "+ matchTimeDurationWithIndex );

        System.out.println("Match Actor duration difference \n "+ (matchTimeDurationWithoutIndex-matchTimeDurationWithIndex) );

        dropIndex(graphDatabase);
        long shortestPathWithoutIndex =measureQuery(graphDatabase,"PROFILE MATCH p=shortestPath( (a:Actor {name: 'Emma Watson'})-[*]-(b:Actor {name: 'Daniel Radcliffe'} ))  RETURN p");

        createIndex(graphDatabase);
        long shortestPathWithIndex =measureQuery(graphDatabase,"PROFILE MATCH p=shortestPath( (a:Actor {name: 'Emma Watson'})-[*]-(b:Actor {name: 'Daniel Radcliffe'} ))  RETURN p");
        System.out.println("Shortest path duration difference \n "+ (shortestPathWithoutIndex-shortestPathWithIndex));

        dropIndex(graphDatabase);
    }

    private static void createIndex(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("CREATE INDEX ON :Actor(name) "));
    }

    private static void dropIndex(GraphDatabase graphDatabase) {
        System.out.println(graphDatabase.runCypher("DROP INDEX ON :Actor(name) "));
    }


    private static long measureQuery(GraphDatabase graphDatabase, String query){
        long startTime = System.nanoTime();
        System.out.println(graphDatabase.runCypher(query));
        long endTime = System.nanoTime();

        return (endTime-startTime);
    }
}
