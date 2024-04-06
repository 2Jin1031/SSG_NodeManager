package ssg.nodemanager.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssg.nodemanager.controller.task.allocation.AllocationForm;
import ssg.nodemanager.domain.Member;
import ssg.nodemanager.domain.Task;
import ssg.nodemanager.repository.TaskRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AllocationService {

    private final TaskRepository taskRepository;

    @Transactional
    public AllocationForm checkAndPrepareAllocation(Member currentMember) {
        Task task = findByMember(currentMember);

        allocationLogic(currentMember, task);

        return makeForm(currentMember, task);
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

    private static AllocationForm makeForm(Member currentMember, Task task) {
        AllocationForm allocationForm = new AllocationForm();
        Map<Integer, String> allocationMapByLevel = task.getAllocationMapByLevel();
        allocationForm.setCurrectLevel(currentMember.getCurrentLevel());
        allocationForm.setAllocationUrl(allocationMapByLevel.get(currentMember.getCurrentLevel()));
        return allocationForm;
    }

    @Transactional
    public void levelUpCheck(Member member) {
        member.levelUP(member.getCurrentLevel());
    }

    private Task findByMember(Member currentMember) {
        Optional<Task> optionalTask = taskRepository.findByMember(currentMember);
        if (optionalTask.isEmpty()) {
            throw new IllegalStateException("task 존재하지 않음");
        }

        Task task = optionalTask.get();
        return task;
    }
}
