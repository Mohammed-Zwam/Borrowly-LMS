package com.server.lms.penalty.specification;


import com.server.lms.penalty.entity.Penalty;
import com.server.lms.penalty.enums.PenaltyState;
import com.server.lms.penalty.enums.PenaltyType;
import org.springframework.data.jpa.domain.Specification;


public class PenaltySpecification {

    public static Specification<Penalty> hasUserId(String userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Penalty> hasPenaltyState(PenaltyState penaltyState) {
        return (root, query, cb) ->
                penaltyState == null ? null : cb.equal(root.get("penaltyState"), penaltyState);
    }

    public static Specification<Penalty> hasPenaltyType(PenaltyType penaltyType) {
        return (root, query, cb) ->
                penaltyType == null ? null : cb.equal(root.get("penaltyType"), penaltyType);
    }


}
