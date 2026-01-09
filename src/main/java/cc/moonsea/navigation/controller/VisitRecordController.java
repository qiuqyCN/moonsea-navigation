package cc.moonsea.navigation.controller;

import cc.moonsea.navigation.service.VisitRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class VisitRecordController {

    private final VisitRecordService visitRecordService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getStatsOverview() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("todayPv", visitRecordService.getTodayTotalVisits());
        stats.put("todayUv", visitRecordService.getTodayUniqueVisitors());
        stats.put("totalPv", visitRecordService.getTotalVisits());
        stats.put("totalUv", visitRecordService.getTotalUniqueVisitors());
        return ResponseEntity.ok(stats);
    }
}

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
class StatsViewController {

    private final VisitRecordService visitRecordService;

    @GetMapping("/stats")
    public String getStatsPage(Model model, @RequestParam(required = false) String showAuth) {
        model.addAttribute("todayPv", visitRecordService.getTodayTotalVisits());
        model.addAttribute("todayUv", visitRecordService.getTodayUniqueVisitors());
        model.addAttribute("totalPv", visitRecordService.getTotalVisits());
        model.addAttribute("totalUv", visitRecordService.getTotalUniqueVisitors());
        model.addAttribute("showAuth", "true".equals(showAuth)); // 只有当参数值为"true"时才显示认证按钮
        return "admin/stats";
    }
}
