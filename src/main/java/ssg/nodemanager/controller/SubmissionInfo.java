package ssg.nodemanager.controller;

import lombok.Getter;
import lombok.Setter;
import ssg.nodemanager.domain.ScoreStatus;

@Getter @Setter
public class SubmissionInfo {

    private int currentLevel;
    private ScoreStatus scoreStatus;
}
