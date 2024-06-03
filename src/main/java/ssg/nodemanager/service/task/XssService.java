package ssg.nodemanager.service.task;

import org.springframework.stereotype.Service;
import ssg.nodemanager.config.ConfigConstants;

import java.util.ArrayList;
import java.util.List;

@Service
public class XssService {

    private List<String> submittedContents = new ArrayList<>();

    public boolean isLevel(int level) {
        return level == ConfigConstants.XSS_LEVEL;
    }

    public List<String> getSubmittedContents() {
        return submittedContents;
    }

    public void addContent(String content) {
        submittedContents.add(0, content); // 최근 항목이 맨 위로 오도록 추가
    }
}