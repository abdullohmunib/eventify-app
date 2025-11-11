package com.final_project.serverapp.dto.response;

import java.util.List;
import java.util.Map;

public record DashboardSummaryResponse(
        Long totalUsers,
        Long totalEvents,
        Long totalETickets,
        Long totalOrders,
        List<Map<String, Object>> eventStats,
        List<Map<String, Object>> monthlyOrderStats) {
}
