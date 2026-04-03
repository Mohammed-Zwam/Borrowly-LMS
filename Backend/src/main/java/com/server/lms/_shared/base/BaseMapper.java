package com.server.lms._shared.base;

import org.mapstruct.MappingTarget;

public abstract class BaseMapper<REQ, RES, ENTITY> {
    public abstract RES toDTO(ENTITY entity);
    public abstract ENTITY toEntity(REQ dto);
    public abstract ENTITY toEntity(@MappingTarget ENTITY entity, REQ dto); // update
}
