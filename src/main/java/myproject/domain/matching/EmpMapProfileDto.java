package myproject.domain.matching;

import lombok.Data;
import myproject.domain.member.Member;

@Data
public class EmpMapProfileDto {

    private Member member;
    private String location_api_lat;
    private String location_api_lng;

    public EmpMapProfileDto(Member member, String location_api_lat, String location_api_lng) {
        this.member = member;
        this.location_api_lat = location_api_lat;
        this.location_api_lng = location_api_lng;
    }
}
