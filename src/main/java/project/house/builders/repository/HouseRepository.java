package project.house.builders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.house.builders.domain.House;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByProjectName(String projectName);
}
