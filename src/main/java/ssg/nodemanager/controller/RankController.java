package ssg.nodemanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ssg.nodemanager.service.RankService;

@Controller
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;


}
