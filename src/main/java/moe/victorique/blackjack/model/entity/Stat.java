package moe.victorique.blackjack.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stat {

    @Id
    @Column(columnDefinition = "TEXT")
    @NonNull
    public String device;

    @Column(columnDefinition = "INTEGER")
    public int wins;

    @Column(columnDefinition = "INTEGER")
    public int loses;

    @Column(columnDefinition = "INTEGER")
    public int draws;
}
