import repository.HallRepository;
import repository.MovieRepository;
import repository.ScreeningRepository;
import repository.TicketRepository;
import repository.impls.HallRepositoryImpl;
import repository.impls.MovieRepositoryImpl;
import repository.impls.ScreeningRepositoryImpl;
import repository.impls.TicketRepositoryImpl;
import service.CrudService;
import service.HallService;
import service.MovieService;
import service.ScreeningService;
import service.TicketService;
import service.impls.CrudServiceImpl;
import service.impls.HallServiceImpl;
import service.impls.MovieServiceImpl;
import service.impls.ScreeningServiceImpl;
import service.impls.TicketServiceImpl;
import ui.ConsoleApplication;
import ui.InputHandler;
import ui.handlers.HallsHandler;
import ui.handlers.MoviesHandler;
import ui.handlers.ScreeningsHandler;
import ui.handlers.TicketsHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.HallRenderer;
import ui.renderer.MovieRenderer;
import ui.renderer.ScreeningRenderer;
import ui.renderer.TicketRenderer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        MovieRepository movieRepo = new MovieRepositoryImpl();
        ScreeningRepository screeningRepo = new ScreeningRepositoryImpl();
        TicketRepository ticketRepo = new TicketRepositoryImpl();
        HallRepository hallRepo = new HallRepositoryImpl();

        CrudService<entity.Movie> movieCrud = new CrudServiceImpl<>(movieRepo);
        CrudService<entity.Hall> hallCrud = new CrudServiceImpl<>(hallRepo);
        CrudService<entity.Screening> screeningCrud = new CrudServiceImpl<>(screeningRepo);
        CrudService<entity.Ticket> ticketCrud = new CrudServiceImpl<>(ticketRepo);

        MovieService movieService = new MovieServiceImpl(movieCrud, null);
        HallService hallService = new HallServiceImpl(hallCrud, null);
        TicketService ticketService = new TicketServiceImpl(ticketCrud, screeningCrud);
        ScreeningService screeningService = new ScreeningServiceImpl(screeningCrud, movieService, hallService, ticketService);

        movieService = new MovieServiceImpl(movieCrud, screeningService);
        hallService = new HallServiceImpl(hallCrud, screeningService);

        Scanner scanner = new Scanner(System.in);
        InputHandler inputHandler = new InputHandler(scanner);

        GeneralRenderer generalRenderer = new GeneralRenderer();
        MovieRenderer movieRenderer = new MovieRenderer(generalRenderer);
        HallRenderer hallRenderer = new HallRenderer(generalRenderer);
        ScreeningRenderer screeningRenderer = new ScreeningRenderer(generalRenderer);
        TicketRenderer ticketRenderer = new TicketRenderer(generalRenderer);

        MoviesHandler moviesHandler = new MoviesHandler(movieService, inputHandler, generalRenderer, movieRenderer);
        HallsHandler hallsHandler = new HallsHandler(hallService, inputHandler, generalRenderer, hallRenderer);
        ScreeningsHandler screeningsHandler = new ScreeningsHandler(
                screeningService, moviesHandler, hallsHandler, inputHandler, generalRenderer, screeningRenderer);
        TicketsHandler ticketsHandler = new TicketsHandler(
                ticketService, screeningService, screeningsHandler, inputHandler, generalRenderer, ticketRenderer);

        ConsoleApplication app = new ConsoleApplication(moviesHandler, hallsHandler, screeningsHandler,
            ticketsHandler, inputHandler, generalRenderer);

        app.start();
    }
}
