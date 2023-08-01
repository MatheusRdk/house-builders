package project.house.builders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.house.builders.domain.Architect;

import java.util.List;

public interface ArchitectRepository extends JpaRepository<Architect, Long> {
    List<Architect> findByName(String name);
}