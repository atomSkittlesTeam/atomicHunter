package net.vniia.skittles.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.configs.OrgStructIntegrationHelper;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Vacancy;
import net.vniia.skittles.entities.VacancyRespond;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
public class OrgStructIntegrationService implements IOrgStructIntegrationService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final OrgStructIntegrationHelper helper;

    @Value("${remoteService.url}")
    private String remoteServiceUrl;

    //-------------------------staff units - штатные единицы

    public List<StaffUnitDto> getAllStaffUnits() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/staffUnits/List"
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<StaffUnitDto[]> request = new HttpEntity<StaffUnitDto[]>(headers);
            ResponseEntity<StaffUnitDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, StaffUnitDto[].class);
            StaffUnitDto[] units = response.getBody();
            if (units == null) {
                log.info("staff units empty [rest]");
                return null;
            }
            return Arrays.asList(units);
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске всех штатных единиц");
        }
    }

    public List<StaffUnitDto> getAllStaffUnitsByStatus(String status) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/staffUnits/List/" + status
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<StaffUnitDto[]> request = new HttpEntity<StaffUnitDto[]>(headers);
            ResponseEntity<StaffUnitDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, StaffUnitDto[].class);
            StaffUnitDto[] units = response.getBody();
            if (units == null) {
                log.info("staff units by status empty [rest]");
                return null;
            }
            return Arrays.asList(units);
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске штатных единиц по статусу");
        }
    }

    public StaffUnitDto getStaffUnitById(String staffUnitId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/staffUnits/" + staffUnitId
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<StaffUnitDto> request = new HttpEntity<StaffUnitDto>(headers);
            ResponseEntity<StaffUnitDto> response = restTemplate.exchange(url, HttpMethod.GET, request, StaffUnitDto.class);
            StaffUnitDto unit = response.getBody();
            if (unit == null) {
                log.info("staff unit by id is empty [rest]");
                return null;
            }
            return unit;
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске штатных единиц по id");
        }
    }

    //------------------ employees - сотрудники
    public List<EmployeeDto> getAllEmployees() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/employees/List"
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<EmployeeDto[]> request = new HttpEntity<EmployeeDto[]>(headers);
            ResponseEntity<EmployeeDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, EmployeeDto[].class);
            EmployeeDto[] employees = response.getBody();
            if (employees == null) {
                log.info("employees empty [rest]");
                return null;
            }
            return Arrays.asList(employees);
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске всех сотрудников");
        }
    }

    public List<EmployeeDto> getAllEmployeesByPositionId(String positionId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/employees/List/" + positionId
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<EmployeeDto[]> request = new HttpEntity<EmployeeDto[]>(headers);
            ResponseEntity<EmployeeDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, EmployeeDto[].class);
            EmployeeDto[] employees = response.getBody();
            if (employees == null) {
                log.info("employees by position id empty [rest]");
                return null;
            }
            return Arrays.asList(employees);
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске всех сотрудников по должности");
        }
    }

    public EmployeeDto getAllEmployeeById(String employeeId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/employees/" + employeeId
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<EmployeeDto> request = new HttpEntity<EmployeeDto>(headers);
            ResponseEntity<EmployeeDto> response = restTemplate.exchange(url, HttpMethod.GET, request, EmployeeDto.class);
            EmployeeDto employee = response.getBody();
            if (employee == null) {
                log.info("employee by id empty [rest]");
                return null;
            }
            return employee;
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске сотрудника по id");
        }
    }

    public void registerEmployee(Vacancy vacancy, VacancyRespond vacancyRespond) {
        EmployeeRequest requestDto = new EmployeeRequest(vacancy, vacancyRespond);
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/employees/Register"
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<EmployeeRequest> request = new HttpEntity<EmployeeRequest>(requestDto, headers);
            ResponseEntity<EmployeeResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                    request, EmployeeResponse.class);
            EmployeeResponse employee = response.getBody();
            if (employee == null) {
                log.info("employee by id empty [rest]");
            }
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в регистрации сотрудника");
        }
    }

    //----------------- positions - должности
    public List<PositionDto> getAllPositions() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/positions/List"
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<PositionDto[]> request = new HttpEntity<PositionDto[]>(headers);
            ResponseEntity<PositionDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, PositionDto[].class);
            PositionDto[] positions = response.getBody();
            if (positions == null) {
                log.info("positions empty [rest]");
                return null;
            }
            return Arrays.asList(positions);
        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске всех должностей");
        }
    }

    public PositionDto getPositionById(String positionId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(this.remoteServiceUrl + "/positions/" + positionId
                    )
                    .build(false)
                    .toUriString();

            HttpHeaders headers = helper.createHeaders();

            HttpEntity<PositionDto> request = new HttpEntity<PositionDto>(headers);
            ResponseEntity<PositionDto> response = restTemplate.exchange(url, HttpMethod.GET, request, PositionDto.class);
            PositionDto position = response.getBody();
            if (position == null) {
                log.info("position by id is empty [rest]");
                return null;
            }
            return position;

        } catch (Exception exception) {
            throw new RuntimeException("Ошибка в поиске должности по id");
        }
    }
}

class EmployeeRequest {
     public UUID staffUnitId;
    public String firstName;
    public String lastName;
    public String email;

    public EmployeeRequest(Vacancy vacancy, VacancyRespond vacancyRespond) {
        this.staffUnitId = vacancy.getStaffUnitId();
        this.firstName = vacancyRespond.getFirstName();
        this.lastName = vacancyRespond.getLastName();
        this.email = vacancyRespond.getEmail();
    }
}

class EmployeeResponse {
    public String result;
}
