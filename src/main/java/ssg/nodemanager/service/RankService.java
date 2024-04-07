package ssg.nodemanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ssg.nodemanager.repository.RankRepository;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;
}
