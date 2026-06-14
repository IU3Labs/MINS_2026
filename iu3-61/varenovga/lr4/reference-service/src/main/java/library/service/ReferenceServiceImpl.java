package library.service;
import library.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Map;

public class ReferenceServiceImpl extends ReferenceServiceGrpc.ReferenceServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    //  Справочные данные (каталоги, валидационные правила)
    private static final Map<String, String> DOMAIN_TO_CATEGORY = Map.of(
            "@vip.library.ru", "VIP", "@msu.ru", "TEACHER", "@mgu.ru", "TEACHER", "@student.msu.ru", "STUDENT"
    );
    private static final Map<String, Integer> CATEGORY_TO_MAX_DAYS = Map.of(
            "VIP", 90, "TEACHER", 60, "STUDENT", 30, "REGULAR", 14
    );
    private static final int STUDENT_SESSION_DAYS = 21;
    private static final LocalDate SESSION_START = LocalDate.of(2026, 6, 1);
    private static final LocalDate SESSION_END = LocalDate.of(2026, 6, 30);

    @Override
    public void validateBorrow(ValidateBorrowRequest req, StreamObserver<ValidateBorrowResponse> resp) {
        log.info("[TRACE:{}] Валидация выдачи: email={}, days={}", req.getTraceId(), req.getUserEmail(), req.getRequestedDays());
        try {
            String category = resolveCategory(req.getUserEmail());
            int maxDays = CATEGORY_TO_MAX_DAYS.getOrDefault(category, 14);
            if ("STUDENT".equals(category) && isSessionPeriod()) maxDays = STUDENT_SESSION_DAYS;

            boolean isValid = req.getRequestedDays() <= maxDays;
            String error = isValid ? "" : "Лимит превышен. Максимум: " + maxDays + " дн.";

            resp.onNext(ValidateBorrowResponse.newBuilder()
                    .setIsValid(isValid).setMaxAllowedDays(maxDays).setUserCategory(category)
                    .setErrorMessage(error).setTraceId(req.getTraceId()).build());
        } catch (Exception e) {
            log.error("[TRACE:{}] Ошибка", req.getTraceId(), e);
            resp.onNext(ValidateBorrowResponse.newBuilder().setIsValid(false).setErrorMessage("Internal Error").setTraceId(req.getTraceId()).build());
        }
        resp.onCompleted();
    }

    @Override
    public void validateUser(ValidateUserRequest req, StreamObserver<ValidateUserResponse> resp) {
        log.info("[TRACE:{}] Валидация юзера: {}", req.getTraceId(), req.getEmail());
        try {
            if (req.getEmail() == null || !req.getEmail().contains("@")) {
                resp.onNext(ValidateUserResponse.newBuilder().setIsValid(false).setErrorMessage("Некорректный email").setTraceId(req.getTraceId()).build());
                resp.onCompleted(); return;
            }
            String category = resolveCategory(req.getEmail());
            resp.onNext(category.equals("REGULAR")
                    ? ValidateUserResponse.newBuilder().setIsValid(false).setErrorMessage("Домен не зарегистрирован").setTraceId(req.getTraceId()).build()
                    : ValidateUserResponse.newBuilder().setIsValid(true).setUserCategory(category).setTraceId(req.getTraceId()).build());
        } catch (Exception e) { resp.onNext(ValidateUserResponse.newBuilder().setIsValid(false).setErrorMessage("Internal Error").setTraceId(req.getTraceId()).build()); }
        resp.onCompleted();
    }

    @Override
    public void validatePublication(ValidatePublicationRequest req, StreamObserver<ValidatePublicationResponse> resp) {
        log.info("[TRACE:{}] Валидация издания: {} ({})", req.getTraceId(), req.getTitle(), req.getYear());
        try {
            int cur = LocalDate.now().getYear();
            if (req.getYear() < 1000 || req.getYear() > cur + 1) {
                resp.onNext(ValidatePublicationResponse.newBuilder().setIsValid(false).setErrorMessage("Некорректный год").setTraceId(req.getTraceId()).build());
            } else if (req.getIsbn() == null || req.getIsbn().trim().isEmpty()) {
                resp.onNext(ValidatePublicationResponse.newBuilder().setIsValid(false).setErrorMessage("ISBN/номер выпуска пуст").setTraceId(req.getTraceId()).build());
            } else {
                resp.onNext(ValidatePublicationResponse.newBuilder().setIsValid(true).setTraceId(req.getTraceId()).build());
            }
        } catch (Exception e) { resp.onNext(ValidatePublicationResponse.newBuilder().setIsValid(false).setErrorMessage("Internal Error").setTraceId(req.getTraceId()).build()); }
        resp.onCompleted();
    }

    @Override
    public void healthCheck(Empty req, StreamObserver<HealthResponse> resp) {
        resp.onNext(HealthResponse.newBuilder().setAlive(true).build()); resp.onCompleted();
    }

    private String resolveCategory(String email) {
        if (email == null) return "REGULAR";
        String l = email.toLowerCase();
        return DOMAIN_TO_CATEGORY.entrySet().stream().filter(e -> l.endsWith(e.getKey())).map(Map.Entry::getValue).findFirst().orElse("REGULAR");
    }
    private boolean isSessionPeriod() {
        LocalDate n = LocalDate.now();
        return !n.isBefore(SESSION_START) && !n.isAfter(SESSION_END);
    }

    @Override
    public void getRulesInfo(GetRulesInfoRequest req, StreamObserver<GetRulesInfoResponse> resp) {
        log.info("[TRACE:{}] Запрос правил выдачи", req.getTraceId());

        String rules = "📋 ПРАВИЛА СРОКА ВЫДАЧИ:\n" +
                "• Обычные читатели: 14 дней\n" +
                "• Студенты: 30 дней (21 день в период сессии — июнь)\n" +
                "• Преподаватели: 60 дней\n" +
                "• VIP (исследователи): 90 дней для обычных книг, 180 для редких\n" +
                "⚠️  Правила управляются централизованно в Reference Service";

        resp.onNext(GetRulesInfoResponse.newBuilder()
                .setRulesText(rules)
                .setTraceId(req.getTraceId())
                .build());
        resp.onCompleted();
    }
}