package project.house.builders.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
