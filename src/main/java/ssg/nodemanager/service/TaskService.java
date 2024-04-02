package ssg.nodemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.ScoreStatus;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.repository.TaskRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Task submit(Member member, String submissionUrl) {
        Task task = new Task();
        task.setMember(member);
        task.setSubmissionUrl(submissionUrl);
        task.setSubmission(true);
        task.setScoreStatus(ScoreStatus.Checking);
        taskRepository.save(task);
        return task;
    }
}
