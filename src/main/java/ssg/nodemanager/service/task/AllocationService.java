package ssg.nodemanager.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.controller.task.allocation.AllocationForm;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.repository.TaskAllocationMapRepository;
import ssg.nodemanager.repository.TaskRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AllocationService {

    private final TaskRepository taskRepository;
    private final TaskAllocationMapRepository taskAllocationMapRepository;

    @Transactional
    public AllocationForm checkAndPrepareAllocation(Member currentMember) {
        Task task = findByMember(currentMember);
        allocationLogic(currentMember, task);
        return makeForm(currentMember);
    }

    @Transactional
    public void allocationLogic(Member currentMember, Task task) {
        if (task.isAllocation()) {
            levelUpCheck(currentMember);
            task.clear();
            task.setAllocation(false);
            task.setSubmission(false);
        }
    }

    private AllocationForm makeForm(Member currentMember) {
        AllocationForm allocationForm = new AllocationForm();
        allocationForm.setCurrectLevel(currentMember.getCurrentLevel());
        allocationForm.setAllocationUrl(taskAllocationMapRepository.findUrlByLevel(currentMember.getCurrentLevel()));
        return allocationForm;
    }

    @Transactional
    public void levelUpCheck(Member member) {
        member.levelUP(member.getCurrentLevel());
    }

    @Transactional
    public Task findByMember(Member currentMember) {
        Optional<Task> optionalTask = taskRepository.findByMember(currentMember);

        if (optionalTask.isEmpty() && currentMember.getCurrentLevel() == 1) {
            Task task = new Task();
            task.setMember(currentMember);
            taskRepository.save(task);
            return task;
        }
        else if (optionalTask.isEmpty()) {
            throw new IllegalStateException("task가 존재하지 않습니다");
        }

        Task task = optionalTask.get();
        return task;
    }
}
