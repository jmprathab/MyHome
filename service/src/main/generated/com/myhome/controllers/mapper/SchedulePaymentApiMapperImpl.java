package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.PaymentDto.PaymentDtoBuilder;
import com.myhome.controllers.request.EnrichedSchedulePaymentRequest;
import com.myhome.controllers.request.SchedulePaymentRequest;
import com.myhome.controllers.response.ListAdminPaymentsResponse.AdminPayment;
import com.myhome.controllers.response.ListMemberPaymentsResponse.MemberPayment;
import com.myhome.controllers.response.SchedulePaymentResponse;
import com.myhome.domain.Payment;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-22T18:22:40+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class SchedulePaymentApiMapperImpl implements SchedulePaymentApiMapper {

    @Override
    public PaymentDto schedulePaymentRequestToPaymentDto(SchedulePaymentRequest schedulePaymentRequest) {
        if ( schedulePaymentRequest == null ) {
            return null;
        }

        PaymentDtoBuilder paymentDto = PaymentDto.builder();

        paymentDto.member( SchedulePaymentApiMapper.memberIdToMemberDto( schedulePaymentRequest.getMemberId() ) );
        paymentDto.admin( SchedulePaymentApiMapper.adminIdToAdminDto( schedulePaymentRequest.getAdminId() ) );
        paymentDto.charge( schedulePaymentRequest.getCharge() );
        paymentDto.type( schedulePaymentRequest.getType() );
        paymentDto.description( schedulePaymentRequest.getDescription() );
        paymentDto.recurring( schedulePaymentRequest.isRecurring() );
        paymentDto.dueDate( schedulePaymentRequest.getDueDate() );

        return paymentDto.build();
    }

    @Override
    public PaymentDto enrichedSchedulePaymentRequestToPaymentDto(EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
        if ( enrichedSchedulePaymentRequest == null ) {
            return null;
        }

        PaymentDtoBuilder paymentDto = PaymentDto.builder();

        paymentDto.charge( enrichedSchedulePaymentRequest.getCharge() );
        paymentDto.type( enrichedSchedulePaymentRequest.getType() );
        paymentDto.description( enrichedSchedulePaymentRequest.getDescription() );
        paymentDto.recurring( enrichedSchedulePaymentRequest.isRecurring() );
        paymentDto.dueDate( enrichedSchedulePaymentRequest.getDueDate() );

        return paymentDto.build();
    }

    @Override
    public Set<MemberPayment> memberPaymentSetToRestApiResponseMemberPaymentSet(Set<Payment> memberPaymentSet) {
        if ( memberPaymentSet == null ) {
            return null;
        }

        Set<MemberPayment> set = new HashSet<MemberPayment>( Math.max( (int) ( memberPaymentSet.size() / .75f ) + 1, 16 ) );
        for ( Payment payment : memberPaymentSet ) {
            set.add( paymentToMemberPayment( payment ) );
        }

        return set;
    }

    @Override
    public MemberPayment paymentToMemberPayment(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        MemberPayment memberPayment = new MemberPayment();

        memberPayment.setPaymentId( payment.getPaymentId() );
        memberPayment.setCharge( payment.getCharge() );
        if ( payment.getDueDate() != null ) {
            memberPayment.setDueDate( DateTimeFormatter.ISO_LOCAL_DATE.format( payment.getDueDate() ) );
        }

        memberPayment.setMemberId( payment.getMember().getMemberId() );

        return memberPayment;
    }

    @Override
    public Set<AdminPayment> adminPaymentSetToRestApiResponseAdminPaymentSet(Set<Payment> memberPaymentSet) {
        if ( memberPaymentSet == null ) {
            return null;
        }

        Set<AdminPayment> set = new HashSet<AdminPayment>( Math.max( (int) ( memberPaymentSet.size() / .75f ) + 1, 16 ) );
        for ( Payment payment : memberPaymentSet ) {
            set.add( paymentToAdminPayment( payment ) );
        }

        return set;
    }

    @Override
    public AdminPayment paymentToAdminPayment(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        AdminPayment adminPayment = new AdminPayment();

        adminPayment.setPaymentId( payment.getPaymentId() );
        adminPayment.setCharge( payment.getCharge() );
        if ( payment.getDueDate() != null ) {
            adminPayment.setDueDate( DateTimeFormatter.ISO_LOCAL_DATE.format( payment.getDueDate() ) );
        }

        adminPayment.setAdminId( payment.getAdmin().getUserId() );

        return adminPayment;
    }

    @Override
    public SchedulePaymentResponse paymentToSchedulePaymentResponse(PaymentDto payment) {
        if ( payment == null ) {
            return null;
        }

        SchedulePaymentResponse schedulePaymentResponse = new SchedulePaymentResponse();

        schedulePaymentResponse.setAdminId( SchedulePaymentApiMapper.adminToAdminId( payment.getAdmin() ) );
        schedulePaymentResponse.setMemberId( SchedulePaymentApiMapper.memberToMemberId( payment.getMember() ) );
        schedulePaymentResponse.setPaymentId( payment.getPaymentId() );
        schedulePaymentResponse.setCharge( payment.getCharge() );
        schedulePaymentResponse.setType( payment.getType() );
        schedulePaymentResponse.setDescription( payment.getDescription() );
        schedulePaymentResponse.setRecurring( payment.isRecurring() );
        schedulePaymentResponse.setDueDate( payment.getDueDate() );

        return schedulePaymentResponse;
    }
}
