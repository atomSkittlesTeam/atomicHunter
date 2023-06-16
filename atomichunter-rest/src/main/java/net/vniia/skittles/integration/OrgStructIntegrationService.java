package net.vniia.skittles.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.configs.OrgStructIntegrationHelper;
import net.vniia.skittles.dto.StaffUnitDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrgStructIntegrationService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final OrgStructIntegrationHelper helper;

    @Value("${remoteService.url}")
    private String remoteServiceUrl;

    public List<StaffUnitDto> getAllStaffUnits() {
        String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/staffUnits/List"
                )
                .build(false)
                .toUriString();

        HttpHeaders headers = helper.createHeaders();

        HttpEntity<StaffUnitDto[]> request = new HttpEntity<StaffUnitDto[]>(headers);
        ResponseEntity<StaffUnitDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, StaffUnitDto[].class);
        StaffUnitDto[] units = response.getBody();
        if (units == null) {
            log.info("staff units empty");
            return null;
        }
        return Arrays.asList(units);
    }
}
