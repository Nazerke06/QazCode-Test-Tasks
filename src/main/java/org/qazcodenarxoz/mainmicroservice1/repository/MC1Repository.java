package org.qazcodenarxoz.mainmicroservice1.repository;

import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MC1Repository extends JpaRepository<MC1Entity, Integer> {
}