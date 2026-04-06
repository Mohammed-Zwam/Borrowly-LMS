package com.server.lms.payment.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentLinkResponse {
    private String url;
    private String id;
}
