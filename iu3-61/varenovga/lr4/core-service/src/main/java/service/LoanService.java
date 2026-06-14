package service;

import exceptions.LibrarySystemException;
import exceptions.UserNotFoundException;
import interfaces.DateProvider;
import interfaces.LoanServiceInterface;
import models.LoanRecord;
import models.Publication;
import models.User;
import repos.LoanRepository;
import repos.PublicationRepository;
import repos.UserRepository;
import service.dto.OverdueInfo;
import service.dto.ActiveLoanInfo;
import service.dto.UserLoanHistory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanService implements LoanServiceInterface {
    private final LoanRepository loanRepository;
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;
    private final DateProvider dateProvider;

    public LoanService(LoanRepository loanRepository,
                       PublicationRepository publicationRepository,
                       UserRepository userRepository,
                       DateProvider dateProvider) {
        this.loanRepository = loanRepository;
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
        this.dateProvider = dateProvider;
    }

    @Override
    public void borrowPublication(int pubId, int userId, LocalDate dueDate)
            throws LibrarySystemException {
        Publication pub = publicationRepository.findById(pubId)
                .orElseThrow(() -> new exceptions.PublicationNotFoundException(pubId));

        if (!pub.isAvailable()) {
            throw new exceptions.PublicationUnavailableException(pubId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new exceptions.UserNotFoundException(userId));

        if (dueDate == null) {
            dueDate = dateProvider.now().plusDays(14);
        }

        LoanRecord loan = new LoanRecord(pub, userId, dueDate, dateProvider);
        loanRepository.save(loan);

        pub.setAvailable(false);
        publicationRepository.save(pub);
        user.addBorrowedPublicationId(pubId);
        userRepository.save(user);
    }

    @Override
    public void borrowPublicationForTest(int pubId, int userId, LocalDate dueDate)
            throws LibrarySystemException {
        Publication pub = publicationRepository.findById(pubId)
                .orElseThrow(() -> new exceptions.PublicationNotFoundException(pubId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new exceptions.UserNotFoundException(userId));

        LoanRecord loan = new LoanRecord(pub, userId, dueDate, dateProvider);
        loanRepository.save(loan);

        pub.setAvailable(false);
        publicationRepository.save(pub);
        user.addBorrowedPublicationId(pubId);
        userRepository.save(user);
    }

    @Override
    public void returnPublication(int pubId) throws LibrarySystemException {
        LoanRecord loan = loanRepository.findByPublicationId(pubId)
                .orElseThrow(() -> new LibrarySystemException("Выдача не найдена для издания ID=" + pubId));

        if (loan.isReturned()) {
            throw new LibrarySystemException("Издание уже возвращено");
        }

        Publication pub = loan.getPublication();
        pub.setAvailable(true);
        publicationRepository.save(pub);

        User user = userRepository.findById(loan.getUserId())
                .orElseThrow(() -> new exceptions.UserNotFoundException(loan.getUserId()));
        user.removeBorrowedPublicationId(pubId);
        userRepository.save(user);

        loan.markReturned();
        loanRepository.save(loan);
    }

    @Override
    public boolean hasActiveLoans(int userId) {
        return loanRepository.findAllActive().stream()
                .anyMatch(loan -> loan.getUserId() == userId);
    }

    @Override
    public List<OverdueInfo> getOverdueReport() {
        List<OverdueInfo> report = new ArrayList<>();
        LocalDate today = dateProvider.now();

        for (LoanRecord loan : loanRepository.findAllActive()) {
            if (loan.isOverdue(today)) {
                Publication pub = loan.getPublication();
                User user = userRepository.findById(loan.getUserId())
                        .orElse(null);

                if (user != null) {
                    report.add(new OverdueInfo(
                            pub.getTitle(),
                            user.getName(),
                            user.getEmail(),
                            loan.getDueDate(),
                            loan.getDaysOverdue(today)
                    ));
                }
            }
        }
        return report;
    }

    @Override
    public List<ActiveLoanInfo> getActiveLoans() {
        List<ActiveLoanInfo> loans = new ArrayList<>();
        LocalDate today = dateProvider.now();

        for (LoanRecord loan : loanRepository.findAllActive()) {
            Publication pub = loan.getPublication();
            User user = userRepository.findById(loan.getUserId())
                    .orElse(null);

            if (user != null) {
                loans.add(new ActiveLoanInfo(
                        pub.getTitle(),
                        user.getName(),
                        loan.getBorrowDate(),
                        loan.getDueDate(),
                        loan.isOverdue(today)
                ));
            }
        }
        return loans;
    }

    @Override
    public List<UserLoanHistory> getUserHistory(int userId) throws UserNotFoundException {
        List<UserLoanHistory> history = new ArrayList<>();
        LocalDate today = dateProvider.now();

        for (LoanRecord loan : loanRepository.findAllActive()) {
            if (loan.getUserId() == userId) {
                Publication pub = loan.getPublication();
                history.add(new UserLoanHistory(
                        pub.getTitle(),
                        loan.getBorrowDate(),
                        loan.getDueDate(),
                        loan.isOverdue(today)
                ));
            }
        }
        return history;
    }
}