package ssg.nodemanager.controller.task;

import lombok.Getter;
import lombok.Setter;
import ssg.nodemanager.domain.ScoreStatus;

@Getter @Setter
public class SubmissionInfo {

    private int currentLevel;
    private int nextLevel;
    private ScoreStatus scoreStatus;
    private boolean isSuccess = false;

}
