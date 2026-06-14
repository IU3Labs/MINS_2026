package ui;

import interfaces.DateProvider;
import interfaces.PublicationServiceInterface;
import interfaces.UserServiceInterface;
import interfaces.LoanServiceInterface;
import repos.*;
import service.*;
import service.factories.*;
import library.client.ReferenceGrpcClient;  // ✅ Добавь импорт

public class Application {
    public static ServiceBundle createDefaultServices() {
        // 1. Создаём gRPC-клиент (связь с Reference Service)
        ReferenceGrpcClient grpcClient = new ReferenceGrpcClient();

        // 2. Репозитории и генераторы
        PublicationRepository pubRepo = new InMemoryPublicationRepository();
        UserRepository userRepo = new InMemoryUserRepository();
        LoanRepository loanRepo = new InMemoryLoanRepository();
        IdGenerator pubIdGen = new IdGenerator(1);
        IdGenerator userIdGen = new IdGenerator(1);
        DateProvider dateProvider = new SystemDateProvider();

        // 3. Фабрики
        PublicationFactoryRegistry factoryRegistry = new PublicationFactoryRegistry();
        factoryRegistry.register(LibraryService.PublicationType.BOOK, new BookFactory());
        factoryRegistry.register(LibraryService.PublicationType.JOURNAL, new JournalFactory());

        // 4. Сервисы
        PublicationServiceInterface pubService = new PublicationService(pubRepo, pubIdGen, factoryRegistry);
        UserServiceInterface userService = new UserService(userRepo, userIdGen);
        LoanServiceInterface loanService = new LoanService(loanRepo, pubRepo, userRepo, dateProvider);

        // 5. ✅ Передаём grpcClient в LibraryService (7 параметров!)
        LibraryService libraryFacade = new LibraryService(
                pubService, userService, loanService, pubRepo, userRepo, dateProvider, grpcClient);

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