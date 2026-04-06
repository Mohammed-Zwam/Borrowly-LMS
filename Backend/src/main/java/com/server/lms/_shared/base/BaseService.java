package com.server.lms._shared.base;

import com.server.lms._shared.exception.EntityNotFoundException;
import com.server.lms.subscription.entity.Subscription;


public interface BaseService<ENTITY, ID> {
    public ENTITY findEntityById(ID id);
}
