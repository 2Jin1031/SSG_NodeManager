package ssg.nodemanager.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.controller.task.submission.SubmissionForm;
import ssg.nodemanager.controller.task.submission.SubmissionInfo;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.ScoreStatus;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.repository.TaskRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubmissionService {

    private final TaskRepository taskRepository;

    // 과제 제출 로직 -> task update
    @Transactional
    public void submit(Member member, String url) {
        Optional<Task> existingTask = taskRepository.findByMember(member);
        Task task;
        if (existingTask.isPresent()) {
            task = existingTask.get();
            // 기존 Task 업데이트 로직
        } else {
            task = new Task();
            task.setMember(member);
            // Task 초기화 로직
        }
        task.submission(url);
        task.setSubmission(true);
        task.setScoreStatus(ScoreStatus.Checking);
        taskRepository.save(task);
    }

    // 과제 존재 유무
    public boolean checkIfTaskExists(Member member) {
        Task existingTask = member.getTask();
        if (existingTask == null) return false;
        return existingTask.isSubmission();
    }

    // 과제 제출 후 결과 Info 생성
    @Transactional
    public SubmissionInfo makeInfo(Member member) {
        Task task = findByMember(member);

        SubmissionInfo info = new SubmissionInfo();
        info.setCurrentLevel(member.getCurrentLevel());
        info.setScoreStatus(task.getScoreStatus());
        info.setNextLevel(member.getNextLevel());

        scoreCheck(task, info);

        return info;
    }

    @Transactional
    public void scoreCheck(Task task, SubmissionInfo info) {
        System.out.println("task.isAllocation() = " + task.isAllocation());
        if (ScoreStatus.Success == task.getScoreStatus()) {
            info.setSuccess(true);
            task.setAllocation(true);
        }
        System.out.println("task.isAllocation() = " + task.isAllocation());
    }

    // 멤버로 과제 찾기
    private Task findByMember(Member member) {
        Optional<Task> optionalTask = taskRepository.findByMember(member);
        if (optionalTask.isEmpty()) {
            throw new IllegalStateException("Task가 비어있음");
        }
        return optionalTask.get();
    }
}
