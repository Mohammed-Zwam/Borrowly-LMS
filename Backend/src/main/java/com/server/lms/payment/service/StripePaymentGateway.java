package com.server.lms.payment.service;

import com.server.lms.payment.dto.response.PaymentLinkResponse;
import com.server.lms.payment.entity.Payment;
import com.server.lms.payment.enums.PaymentType;
import com.server.lms.payment.exception.PaymentFailureException;
import com.server.lms.subscription.entity.SubscriptionPlan;
import com.server.lms.subscription.service.SubscriptionPlanService;
import com.server.lms.user.entity.User;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("STRIPE")
@RequiredArgsConstructor
public class StripePaymentGateway implements PaymentGateway {

    private final SubscriptionPlanService subscriptionPlanService;

    @Value("${client.base-url}")
    private String baseUrl;

    @Override
    public PaymentLinkResponse createPaymentLink(User user, Payment payment) {
        try {
            Map<String, String> metadata = buildMetadata(user, payment, baseUrl);

            long amountInCents;
            String productName;

            if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
                SubscriptionPlan plan = subscriptionPlanService.getByPlanCode(metadata.get("subscription_plan_code"));
                amountInCents = plan.getPrice() * 100; // convert dollar to cent
                productName = plan.getName();
            } else if (payment.getPaymentType() == PaymentType.FINE) {
                // TODO

                amountInCents = payment.getAmount() * 100;
                productName = "Library Fine";

            } else {
                throw new PaymentFailureException("Unsupported payment type");
            }


            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount(amountInCents)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(productName)
                                                    .build()
                                    )
                                    .build()
                    )
                    .setQuantity(1L)
                    .build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .addLineItem(lineItem)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(baseUrl + "/payment/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(baseUrl + "/payment/cancel")
                    .putAllMetadata(metadata)
                    .build();

            Session session = Session.create(params);

            return PaymentLinkResponse.builder()
                    .id(session.getId())
                    .url(session.getUrl())
                    .build();

        } catch (StripeException e) {
            throw new PaymentFailureException("Failed to create payment link: " + e.getMessage());
        }
    }


    @Override
    public Map<String, String> validatePayment(String gatewayPaymentId) {
        try {
            PaymentIntent paymentIntent = fetchPaymentDetails(gatewayPaymentId);
            Map<String, String> metadata = paymentIntent.getMetadata();
            metadata.put("isValidPayment", "false"); // initially


            String status = paymentIntent.getStatus();
            if (!status.equalsIgnoreCase("captured")) return metadata;

            long amount = paymentIntent.getAmount();
            long amountInCurrency = amount / 100;
            String paymentType = metadata.get("payment_type");

            if (paymentType.equals(PaymentType.MEMBERSHIP.toString())) {
                SubscriptionPlan subscriptionPlan = subscriptionPlanService.getByPlanCode(metadata.get("subscription_plan_code"));
                metadata.put("isValidPayment", String.valueOf(subscriptionPlan.getPrice() == amountInCurrency));
                return metadata;
            } else if (paymentType.equals(PaymentType.FINE.toString())) {
            /*
                TODO: FINE LOGIC
                String fineId = notes.getLong( key: "fine_id");
                Fine fine = fineRepository.findById(fineId).orElseThrow(
                        () -> new PaymentFailureException("Fine not found with given id .... ")

                return fine.getAmount() == amountInCurrency;
            */
                return metadata;
            } else {
                return metadata;
            }
        } catch (Exception ex) {
            throw new PaymentFailureException("Payment Validation Failed: " + ex.getMessage());
        }
    }

    private PaymentIntent fetchPaymentDetails(String paymentIntentId) {
        try {
            return PaymentIntent.retrieve(paymentIntentId);
        } catch (Exception e) {
            throw new PaymentFailureException("Failed to fetch payment details: " + e.getMessage());
        }
    }

}
