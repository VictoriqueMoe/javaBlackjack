package moe.victorique.blackjack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stat {

    @Id
    @Column(columnDefinition = "TEXT")
    public String device;

    @Column(columnDefinition = "INTEGER")
    public int wins;

    @Column(columnDefinition = "INTEGER")
    public int loses;

    @Column(columnDefinition = "INTEGER")
    public int draws;
}
