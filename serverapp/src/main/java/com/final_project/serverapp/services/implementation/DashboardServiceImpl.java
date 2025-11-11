package com.final_project.serverapp.services.implementation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.final_project.serverapp.dto.response.DashboardSummaryResponse;
import com.final_project.serverapp.repositories.ETicketRepository;
import com.final_project.serverapp.repositories.EventRepository;
import com.final_project.serverapp.repositories.OrderRepository;
import com.final_project.serverapp.repositories.UserRepository;
import com.final_project.serverapp.services.contracts.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ETicketRepository eTicketRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardSummaryResponse getDashboardSummary() {
        // Get counts
        Long totalUsers = userRepository.count();
        Long totalEvents = eventRepository.count();
        Long totalETickets = eTicketRepository.count();
        Long totalOrders = orderRepository.count();

        // Get event statistics (top 5 events by ticket sales)
        List<Map<String, Object>> eventStats = getEventStatistics();

        // Get monthly order statistics (last 6 months)
        List<Map<String, Object>> monthlyOrderStats = getMonthlyOrderStatistics();

        return new DashboardSummaryResponse(
                totalUsers,
                totalEvents,
                totalETickets,
                totalOrders,
                eventStats,
                monthlyOrderStats);
    }

    private List<Map<String, Object>> getEventStatistics() {
        List<Map<String, Object>> stats = new ArrayList<>();
        
        // Get all events with their ticket count
        var events = eventRepository.findAll();
        
        events.stream()
                .sorted((e1, e2) -> {
                    long count1 = eTicketRepository.countByOrder_Event_EventId(e1.getEventId());
                    long count2 = eTicketRepository.countByOrder_Event_EventId(e2.getEventId());
                    return Long.compare(count2, count1);
                })
                .limit(5)
                .forEach(event -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("eventName", event.getJudul());
                    stat.put("ticketCount", eTicketRepository.countByOrder_Event_EventId(event.getEventId()));
                    stats.add(stat);
                });

        return stats;
    }

    private List<Map<String, Object>> getMonthlyOrderStatistics() {
        List<Map<String, Object>> stats = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 5; i >= 0; i--) {
            LocalDateTime monthStart = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);

            long orderCount = orderRepository.countByCreatedAtBetween(monthStart, monthEnd);

            Map<String, Object> stat = new HashMap<>();
            stat.put("month", monthStart.format(formatter));
            stat.put("orderCount", orderCount);
            stats.add(stat);
        }

        return stats;
    }
}
