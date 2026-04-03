package com.server.lms.subscription.service;

import com.server.lms._shared.exception.DuplicateFieldException;
import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.subscription.dto.request.SubscriptionPlanRequest;
import com.server.lms.subscription.dto.response.SubscriptionPlanResponse;
import com.server.lms.subscription.entity.SubscriptionPlan;
import com.server.lms.subscription.mapper.SubscriptionPlanMapper;
import com.server.lms.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;

    @Override
    public SubscriptionPlanResponse create(SubscriptionPlanRequest dto) {
        existsByPlanCode(dto.getPlanCode()); // check if plan exist or no
        SubscriptionPlan subscriptionPlan = subscriptionPlanMapper.toEntity(dto);
        return subscriptionPlanMapper.toDTO(subscriptionPlanRepository.save(subscriptionPlan));
    }

    @Override
    public SubscriptionPlanResponse update(String id, SubscriptionPlanRequest dto) {
        SubscriptionPlan subscriptionPlan = findEntityById(id);
        if (!Objects.equals(dto.getPlanCode(), subscriptionPlan.getPlanCode())) existsByPlanCode(dto.getPlanCode()); // to check if the new plan code used with another plan
        subscriptionPlanMapper.toEntity(subscriptionPlan, dto);

        return subscriptionPlanMapper.toDTO(subscriptionPlanRepository.save(subscriptionPlan));
    }

    @Override
    public void delete(String id) {
        var subscriptionPlan = findEntityById(id);
        subscriptionPlanRepository.delete(subscriptionPlan);
    }

    @Override
    public SubscriptionPlanResponse getById(String id) {
        return subscriptionPlanMapper.toDTO(findEntityById(id));
    }

    @Override
    public List<SubscriptionPlanResponse> getAll() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .map(subscriptionPlanMapper::toDTO)
                .collect(Collectors.toList());
    }


    // ====== HELPERS ====== //

    @Override
    public SubscriptionPlan findEntityById(String id) {
        return subscriptionPlanRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Subscription Plan Not Found")
        );
    }

    @Override
    public void existsByPlanCode(String planCode) {
        if (subscriptionPlanRepository.existsByPlanCode(planCode))
            throw new DuplicateFieldException("Subscription Plan Code Already Exists");
    }
}
