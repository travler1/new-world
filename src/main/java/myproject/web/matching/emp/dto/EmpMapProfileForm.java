package myproject.web.matching.emp.dto;

import lombok.Data;
import myproject.domain.member.Member;

@Data
public class EmpMapProfileForm {

    private Member member;
    private String location_api_lat;
    private String location_api_lng;

    public EmpMapProfileForm(Member member, String location_api_lat, String location_api_lng) {
        this.member = member;
        this.location_api_lat = location_api_lat;
        this.location_api_lng = location_api_lng;
    }
}
