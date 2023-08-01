package project.house.builders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.house.builders.domain.Engineer;

import java.util.List;

public interface EngineerRepository extends JpaRepository<Engineer, Long> {
    List<Engineer> findByName(String name);
}