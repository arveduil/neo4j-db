package pl.edu.agh.ki.bd2;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GraphDatabase graphDatabase = GraphDatabase.createDatabase();

        Solution solution = new Solution(graphDatabase);
        solution.databaseStatistics();
        //solution.runAllTests();

        ExtraTasks.createNewNodesForActorAndMovie(graphDatabase);
        ExtraTasks.fillNodesWithData(graphDatabase);
        ExtraTasks.actorsWhoPlayedInAtLeast6Movies(graphDatabase);
        ExtraTasks.avgMoviesPlayedForActorsWhoPlayedInAtLeast7Movies(graphDatabase);
        ExtraTasks.actorsWhoPlayedIn5MoviesAndDirectedAtLeastOne(graphDatabase);
        ExtraTasks.usersWhoRatedMovieWith3StarsAtLeast(graphDatabase);
        ExtraTasks.pathBetweenActorsWithoutMovies(graphDatabase);
        ExtraTasks.measureIndexTime(graphDatabase);

    }
}
