package ui.renderer;

import entity.Movie;

import java.util.List;

public class MovieRenderer {

    private final GeneralRenderer generalRenderer;

    public MovieRenderer(GeneralRenderer generalRenderer) {
        this.generalRenderer = generalRenderer;
    }

    public void printMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            generalRenderer.printNotFound("Фильмы");
            return;
        }
        System.out.printf("%-38s | %-20s | %-10s | %-15s | %s%n", "ID", "Название", "Длительность", "Жанр", "Возраст");
        generalRenderer.printSeparator(100);
        for (Movie m : movies) {
            System.out.printf("%-38s | %-20s | %-10s | %-15s | %d+%n",
                    m.getId(),
                    m.getTitle(),
                    m.getDuration().toMinutes() + " мин",
                    m.getGenre(),
                    m.getAgeRestriction());
        }
    }
}
