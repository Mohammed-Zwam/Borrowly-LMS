package com.server.lms.payment.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembershipPaymentSucceededEvent {
    private String paymentId;
    private String userId;
    private String userEmail;
    private String userName;
    private String subscriptionPlanCode;
    private String subscriptionId;

}
