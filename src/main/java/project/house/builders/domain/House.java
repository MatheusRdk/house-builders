package project.house.builders.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The house project must have a name or a nickname")
    private String projectName;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Engineer engineer;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Architect architect;
}
