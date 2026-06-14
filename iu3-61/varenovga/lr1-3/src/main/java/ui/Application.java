package main.java.ui;

import main.java.interfaces.DateProvider;
import main.java.interfaces.PublicationServiceInterface;
import main.java.interfaces.UserServiceInterface;
import main.java.interfaces.LoanServiceInterface;
import main.java.repos.*;
import main.java.service.*;
import main.java.service.factories.*;

public class Application {
    public static ServiceBundle createDefaultServices() {
        PublicationRepository pubRepo = new InMemoryPublicationRepository();
        UserRepository userRepo = new InMemoryUserRepository();
        LoanRepository loanRepo = new InMemoryLoanRepository();
        IdGenerator pubIdGen = new IdGenerator(1);
        IdGenerator userIdGen = new IdGenerator(1);
        DateProvider dateProvider = new SystemDateProvider();

        PublicationFactoryRegistry factoryRegistry = new PublicationFactoryRegistry();
        factoryRegistry.register(main.java.service.LibraryService.PublicationType.BOOK, new BookFactory());
        factoryRegistry.register(main.java.service.LibraryService.PublicationType.JOURNAL, new JournalFactory());

        PublicationServiceInterface pubService = new PublicationService(pubRepo, pubIdGen, factoryRegistry);
        UserServiceInterface userService = new UserService(userRepo, userIdGen);
        LoanServiceInterface loanService = new LoanService(loanRepo, pubRepo, userRepo, dateProvider);

        // ✅ Передаём 6 аргументов, как требует конструктор LibraryService
        LibraryService libraryFacade = new LibraryService(
                pubService, userService, loanService, pubRepo, userRepo, dateProvider);

        return new ServiceBundle(libraryFacade, pubIdGen, userIdGen);
    }

    public static class ServiceBundle {
        public final LibraryService libraryService;
        public final IdGenerator publicationIdGenerator;
        public final IdGenerator userIdGenerator;

        public ServiceBundle(LibraryService libraryService,
                             IdGenerator publicationIdGenerator,
                             IdGenerator userIdGenerator) {
            this.libraryService = libraryService;
            this.publicationIdGenerator = publicationIdGenerator;
            this.userIdGenerator = userIdGenerator;
        }
    }
}