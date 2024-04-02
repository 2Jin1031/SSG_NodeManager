package ssg.nodemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Rank {

    @Id
    @GeneratedValue
    @Column(name = "rank_id")
    private Long id;

    private int highestLevel;
    private int lowestLevel;

    @OneToMany
    private List<Member> members = new ArrayList<>();
}
