package com.myhome.controllers.dto.mapper;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.PaymentDto.PaymentDtoBuilder;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.UserDto.UserDtoBuilder;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.domain.User.UserBuilder;
import com.myhome.model.HouseMemberDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-22T18:22:41+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment paymentDtoToPayment(PaymentDto paymentDto) {
        if ( paymentDto == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setPaymentId( paymentDto.getPaymentId() );
        payment.setCharge( paymentDto.getCharge() );
        payment.setType( paymentDto.getType() );
        payment.setDescription( paymentDto.getDescription() );
        payment.setRecurring( paymentDto.isRecurring() );
        if ( paymentDto.getDueDate() != null ) {
            payment.setDueDate( LocalDate.parse( paymentDto.getDueDate() ) );
        }
        payment.setAdmin( userDtoToUser( paymentDto.getAdmin() ) );
        payment.setMember( houseMemberDtoToHouseMember( paymentDto.getMember() ) );

        return payment;
    }

    @Override
    public PaymentDto paymentToPaymentDto(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentDtoBuilder paymentDto = PaymentDto.builder();

        if ( payment.getDueDate() != null ) {
            paymentDto.dueDate( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( payment.getDueDate() ) );
        }
        paymentDto.paymentId( payment.getPaymentId() );
        paymentDto.charge( payment.getCharge() );
        paymentDto.type( payment.getType() );
        paymentDto.description( payment.getDescription() );
        paymentDto.recurring( payment.isRecurring() );
        paymentDto.admin( userToUserDto( payment.getAdmin() ) );
        paymentDto.member( houseMemberToHouseMemberDto( payment.getMember() ) );

        return paymentDto.build();
    }

    protected User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.name( userDto.getName() );
        user.userId( userDto.getUserId() );
        user.email( userDto.getEmail() );
        user.encryptedPassword( userDto.getEncryptedPassword() );

        return user.build();
    }

    protected HouseMember houseMemberDtoToHouseMember(HouseMemberDto houseMemberDto) {
        if ( houseMemberDto == null ) {
            return null;
        }

        HouseMember houseMember = new HouseMember();

        houseMember.setId( houseMemberDto.getId() );
        houseMember.setMemberId( houseMemberDto.getMemberId() );
        houseMember.setName( houseMemberDto.getName() );

        return houseMember;
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.userId( user.getUserId() );
        userDto.name( user.getName() );
        userDto.email( user.getEmail() );
        userDto.encryptedPassword( user.getEncryptedPassword() );

        return userDto.build();
    }

    protected HouseMemberDto houseMemberToHouseMemberDto(HouseMember houseMember) {
        if ( houseMember == null ) {
            return null;
        }

        HouseMemberDto houseMemberDto = new HouseMemberDto();

        houseMemberDto.setId( houseMember.getId() );
        houseMemberDto.setMemberId( houseMember.getMemberId() );
        houseMemberDto.setName( houseMember.getName() );

        return houseMemberDto;
    }
}
