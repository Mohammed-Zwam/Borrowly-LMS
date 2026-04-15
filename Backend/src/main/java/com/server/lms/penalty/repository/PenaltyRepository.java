package com.server.lms.penalty.repository;

import com.server.lms._shared.base.BaseRepository;
import com.server.lms.penalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PenaltyRepository extends BaseRepository<Penalty, String> , JpaSpecificationExecutor<Penalty> {

    List<Penalty> findByUserId(String userId);

}
