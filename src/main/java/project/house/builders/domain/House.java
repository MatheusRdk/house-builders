package project.house.builders.domain;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The house project must have a name or a nickname")
    private String projectName;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Engineer engineer;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Architect architect;

}
