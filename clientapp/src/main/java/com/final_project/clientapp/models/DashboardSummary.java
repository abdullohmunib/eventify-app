package com.final_project.clientapp.models;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardSummary {
    private Long totalUsers;
    private Long totalEvents;
    private Long totalETickets;
    private Long totalOrders;
    private List<Map<String, Object>> eventStats;
    private List<Map<String, Object>> monthlyOrderStats;
}
