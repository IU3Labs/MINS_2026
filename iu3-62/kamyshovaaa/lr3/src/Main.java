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
import service.TicketQueryService;
import service.TicketCommandService;
import service.impls.CrudServiceImpl;
import service.impls.HallServiceImpl;
import service.impls.MovieServiceImpl;
import service.impls.ScreeningServiceImpl;
import service.impls.TicketQueryServiceImpl;
import service.impls.TicketCommandServiceImpl;
import service.pricing.*;
import service.uow.IUnitOfWork;
import service.uow.IUnitOfWorkFactory;
import service.uow.UnitOfWorkFactory;
import ui.ConsoleApplication;
import ui.InputHandler;
import ui.handlers.HallsHandler;
import ui.handlers.MoviesHandler;
import ui.handlers.RevenuesHandler;
import ui.handlers.ScreeningsHandler;
import ui.handlers.TicketsHandler;
import ui.renderer.GeneralRenderer;
import ui.renderer.HallRenderer;
import ui.renderer.MovieRenderer;
import ui.renderer.ScreeningRenderer;
import ui.renderer.TicketRenderer;

import java.util.List;
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

        List<PricingStrategy> pricingStrategies = List.of(
            new StandardPricing(),
            new DiscountedPricing(),
            new ChildPricing()
        );
        IPricingService pricingService = new PricingService(pricingStrategies);
        
        IUnitOfWorkFactory uowFactory = new UnitOfWorkFactory();
        
        IUnitOfWork<entity.Ticket> ticketUow = uowFactory.create(ticketCrud);
        IUnitOfWork<entity.Screening> screeningUow = uowFactory.create(screeningCrud);

        TicketQueryService ticketQueryService = new TicketQueryServiceImpl(ticketCrud, pricingService);
        TicketCommandService ticketCommandService = new TicketCommandServiceImpl(ticketCrud, screeningCrud, pricingService, ticketUow);

        MovieService movieService = new MovieServiceImpl(movieCrud, null);
        HallService hallService = new HallServiceImpl(hallCrud, null);
        ScreeningService screeningService = new ScreeningServiceImpl(
                screeningCrud, movieService, hallService, ticketCommandService, ticketQueryService, screeningUow, ticketUow);

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
                ticketQueryService, ticketCommandService, screeningService, screeningsHandler, inputHandler, generalRenderer, ticketRenderer);
        
        RevenuesHandler revenuesHandler = new RevenuesHandler(inputHandler, generalRenderer);
        revenuesHandler.setTicketRepository(ticketRepo);
        revenuesHandler.setScreeningRepository(screeningRepo);

        ConsoleApplication app = new ConsoleApplication(moviesHandler, hallsHandler, screeningsHandler,
            ticketsHandler, revenuesHandler, inputHandler, generalRenderer);

        app.start();
    }
}
