package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.controllers.dto.AmenityDto.AmenityDtoBuilder;
import com.myhome.controllers.request.UpdateAmenityRequest;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-10-22T18:22:41+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.2 (Oracle Corporation)"
)
@Component
public class AmenityApiMapperImpl implements AmenityApiMapper {

    @Override
    public GetAmenityDetailsResponse amenityToAmenityDetailsResponse(Amenity amenity) {
        if ( amenity == null ) {
            return null;
        }

        GetAmenityDetailsResponse getAmenityDetailsResponse = new GetAmenityDetailsResponse();

        getAmenityDetailsResponse.setAmenityId( amenity.getAmenityId() );
        getAmenityDetailsResponse.setDescription( amenity.getDescription() );

        return getAmenityDetailsResponse;
    }

    @Override
    public Set<GetAmenityDetailsResponse> amenitiesSetToAmenityDetailsResponseSet(Set<Amenity> amenity) {
        if ( amenity == null ) {
            return null;
        }

        Set<GetAmenityDetailsResponse> set = new HashSet<GetAmenityDetailsResponse>( Math.max( (int) ( amenity.size() / .75f ) + 1, 16 ) );
        for ( Amenity amenity1 : amenity ) {
            set.add( amenityToAmenityDetailsResponse( amenity1 ) );
        }

        return set;
    }

    @Override
    public Amenity amenityDtoToAmenity(AmenityDto amenityDto) {
        if ( amenityDto == null ) {
            return null;
        }

        Amenity amenity = new Amenity();

        amenity.setId( amenityDto.getId() );
        amenity.setAmenityId( amenityDto.getAmenityId() );
        amenity.setName( amenityDto.getName() );
        amenity.setDescription( amenityDto.getDescription() );
        amenity.setPrice( amenityDto.getPrice() );

        return amenity;
    }

    @Override
    public AmenityDto amenityToAmenityDto(Amenity amenity) {
        if ( amenity == null ) {
            return null;
        }

        AmenityDtoBuilder amenityDto = AmenityDto.builder();

        amenityDto.id( amenity.getId() );
        amenityDto.amenityId( amenity.getAmenityId() );
        amenityDto.name( amenity.getName() );
        amenityDto.description( amenity.getDescription() );
        amenityDto.price( amenity.getPrice() );

        return amenityDto.build();
    }

    @Override
    public AmenityDto updateAmenityRequestToAmenityDto(UpdateAmenityRequest request) {
        if ( request == null ) {
            return null;
        }

        AmenityDtoBuilder amenityDto = AmenityDto.builder();

        amenityDto.name( request.getName() );
        amenityDto.description( request.getDescription() );
        amenityDto.price( BigDecimal.valueOf( request.getPrice() ) );
        amenityDto.communityId( request.getCommunityId() );

        return amenityDto.build();
    }
}
