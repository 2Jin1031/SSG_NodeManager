package ssg.nodemanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ssg.nodemanager.domain.Task;

import java.util.HashMap;
import java.util.Map;

//@Component
//public class ApplicationInitializer {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void onApplicationEvent() {
//        String sql = "SELECT level, url FROM task_allocation_map";
//        Map<Integer, String> allocationMap = new HashMap<>();
//
//        jdbcTemplate.query(sql, (rs) -> {
//            allocationMap.put(rs.getInt("level"), rs.getString("url"));
//        });
//
//        Task.initializeAllocationMap(allocationMap);
//    }
//}