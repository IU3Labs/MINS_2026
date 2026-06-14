package com.cinema.core.server;

import com.cinema.core.client.ReferenceServiceClient;
import com.cinema.core.entity.Ticket;
import com.cinema.core.util.TraceIdContext;
import com.cinema.proto.core.CoreServiceGrpc;
import com.cinema.proto.core.CoreServiceOuterClass.*;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoreServiceImpl extends CoreServiceGrpc.CoreServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);
    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();
    private final ReferenceServiceClient refClient;

    public CoreServiceImpl(String refHost, int refPort) {
        MDC.put("traceId", TraceIdContext.generateShortUuid());
        this.refClient = ReferenceServiceClient.create(refHost, refPort, 5);
        if (!refClient.isAvailable()) {
            log.warn("Reference Service unavailable. Working in degraded mode.");
        } else {
            log.info("Connected to Reference Service");
        }
    }

    // ==================== ListTickets ====================
    @Override
    public void listTickets(Empty request, StreamObserver<ListTicketsResponse> responseObserver) {
        String traceId = TraceIdContext.getCurrentTraceId();
        MDC.put("traceId", traceId);
        log.info("[{}] listTickets request", traceId);

        ListTicketsResponse.Builder responseBuilder = ListTicketsResponse.newBuilder();
        for (Ticket ticket : tickets.values()) {
            TicketInfo ticketInfo = TicketInfo.newBuilder()
                    .setTicketId(ticket.getId().toString())
                    .setScreeningId(ticket.getScreeningId().toString())
                    .setRow(ticket.getSeatRow())
                    .setSeatNumber(ticket.getSeatNumber())
                    .setPrice(ticket.getPrice())
                    .setStatus(ticket.getStatus())
                    .build();
            responseBuilder.addTickets(ticketInfo);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    // ==================== BuyTicket ====================
    @Override
    public void buyTicket(BuyTicketRequest request, StreamObserver<BuyTicketResponse> responseObserver) {
        String traceId = TraceIdContext.getCurrentTraceId();
        MDC.put("traceId", traceId);
        log.info("[{}] buyTicket request: screening_id={}, seat={}/{}",
                traceId, request.getScreeningId(), request.getRow(), request.getSeatNumber());

        if (!refClient.isAvailable()) {
            responseObserver.onError(Status.UNAVAILABLE
                    .withDescription("Reference Service unavailable")
                    .asRuntimeException());
            return;
        }

        try {
            Map<String, Object> screeningResp = refClient.getScreening(traceId, request.getScreeningId());

            if (screeningResp.containsKey("error")) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription(String.valueOf(screeningResp.get("error")))
                        .asRuntimeException());
                return;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> screening = (Map<String, Object>) screeningResp.get("screening");
            int rows = (int) screening.get("rows");
            int seatsPerRow = (int) screening.get("seats_per_row");

            if (request.getRow() <= 0 || request.getRow() > rows) {
                responseObserver.onError(Status.OUT_OF_RANGE
                        .withDescription("Неверный ряд. Доступны ряды 1-" + rows)
                        .asRuntimeException());
                return;
            }

            if (request.getSeatNumber() <= 0 || request.getSeatNumber() > seatsPerRow) {
                responseObserver.onError(Status.OUT_OF_RANGE
                        .withDescription("Неверное место. Доступны места 1-" + seatsPerRow)
                        .asRuntimeException());
                return;
            }

            double basePrice = (double) screening.get("ticket_price");
            double price = calculatePrice(basePrice, request.getCategoryIndex());

            boolean alreadyBought = tickets.values().stream()
                    .anyMatch(t -> t.getScreeningId().toString().equals(request.getScreeningId())
                            && t.getSeatRow() == request.getRow()
                            && t.getSeatNumber() == request.getSeatNumber()
                            && "PAID".equals(t.getStatus()));

            if (alreadyBought) {
                responseObserver.onError(Status.ALREADY_EXISTS
                        .withDescription("Место уже куплено")
                        .asRuntimeException());
                return;
            }

            Ticket ticket = new Ticket(
                    UUID.randomUUID(),
                    UUID.fromString(request.getScreeningId()),
                    request.getRow(),
                    request.getSeatNumber(),
                    price
            );
            ticket.setStatus("PAID");
            tickets.put(ticket.getId().toString(), ticket);

            log.info("[{}] Ticket bought: {}", traceId, ticket.getId());

            responseObserver.onNext(BuyTicketResponse.newBuilder()
                    .setTicketId(ticket.getId().toString())
                    .setFinalPrice(price)
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("[{}] Error buying ticket", traceId, e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    // ==================== UseTicket ====================
    @Override
    public void useTicket(UseTicketRequest request, StreamObserver<Empty> responseObserver) {
        String traceId = TraceIdContext.getCurrentTraceId();
        MDC.put("traceId", traceId);
        log.info("[{}] useTicket request: ticket_id={}", traceId, request.getTicketId());

        Ticket ticket = tickets.get(request.getTicketId());
        if (ticket == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Ticket not found")
                    .asRuntimeException());
            return;
        }

        if (!ticket.isPaid()) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Ticket is not paid")
                    .asRuntimeException());
            return;
        }

        if (ticket.isUsed()) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Ticket already used")
                    .asRuntimeException());
            return;
        }

        ticket.setStatus("USED");
        log.info("[{}] Ticket used: {}", traceId, request.getTicketId());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    // ==================== CancelTicket ====================
    @Override
    public void cancelTicket(CancelTicketRequest request, StreamObserver<Empty> responseObserver) {
        String traceId = TraceIdContext.getCurrentTraceId();
        MDC.put("traceId", traceId);
        log.info("[{}] cancelTicket request: ticket_id={}", traceId, request.getTicketId());

        Ticket ticket = tickets.get(request.getTicketId());
        if (ticket == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Ticket not found")
                    .asRuntimeException());
            return;
        }

        if (!ticket.canCancel()) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Ticket cannot be cancelled. Status: " + ticket.getStatus())
                    .asRuntimeException());
            return;
        }

        tickets.remove(request.getTicketId());
        log.info("[{}] Ticket cancelled: {}", traceId, request.getTicketId());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    // ==================== GetTicket ====================
    @Override
    public void getTicket(GetTicketRequest request, StreamObserver<GetTicketResponse> responseObserver) {
        String traceId = TraceIdContext.getCurrentTraceId();
        MDC.put("traceId", traceId);
        log.info("[{}] getTicket request: ticket_id={}", traceId, request.getTicketId());

        Ticket ticket = tickets.get(request.getTicketId());
        if (ticket == null) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Ticket not found")
                    .asRuntimeException());
            return;
        }

        GetTicketResponse response = GetTicketResponse.newBuilder()
                .setTicketId(ticket.getId().toString())
                .setScreeningId(ticket.getScreeningId().toString())
                .setRow(ticket.getSeatRow())
                .setSeatNumber(ticket.getSeatNumber())
                .setPrice(ticket.getPrice())
                .setStatus(ticket.getStatus())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private double calculatePrice(double basePrice, int categoryIndex) {
        return switch (categoryIndex) {
            case 1 -> basePrice * 0.5;  // DISCOUNTED
            case 2 -> basePrice * 2.0;  // CHILD
            default -> basePrice;         // STANDARD - полная цена
        };
    }
}