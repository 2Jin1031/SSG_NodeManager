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
    public void submit(Member member, String submissionUrl) {
        Task task = new Task();
        task.setMember(member);
        task.setSubmissionUrl(submissionUrl);
        task.setSubmission(true);
        task.setScoreStatus(ScoreStatus.Checking);
        taskRepository.save(task);
    }

    // 과제 존재 유무
    public boolean checkIfTaskExists(Member member) {
        Task existingTask = member.getTask();
        return existingTask != null || existingTask.isSubmission();
    }

    // 과제 제출 후 결과 Info 생성
    public SubmissionInfo makeInfo(Member member, String submissionUrl) {
        Optional<Task> optionalTask = taskRepository.findByMember(member);
        if (optionalTask.isEmpty()) {
            throw new IllegalStateException("Task가 비어있음");
        }
        Task task = optionalTask.get();

        SubmissionInfo info = new SubmissionInfo();
        info.setCurrentLevel(member.getCurrentLevel());
        info.setScoreStatus(task.getScoreStatus());
        info.setNextLevel(member.getNextLevel());

        if (ScoreStatus.Success == task.getScoreStatus()) {
            info.setSuccess(true);
        }

        return info;
    }

    // 과제 제출 할 Form 생성
    public SubmissionForm makeForm(Member member, String url) {
        SubmissionForm form = new SubmissionForm();
        form.setCurrentLevel(member.getCurrentLevel());
        form.setSubmissionUrl(url);
        return form;
    }

    // 멤버로 현재 과제 url 찾기
    public String findUrlByMember(Member member) {
        Optional<Task> optionalTask = taskRepository.findByMember(member);
        if (optionalTask.isEmpty()) {
            throw new IllegalStateException("Task가 비어있음");
        }
        Task task = optionalTask.get();
        return task.getSubmissionUrl();
    }

}
