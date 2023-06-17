package net.vniia.skittles.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Deprecated
public class RequestIntegrationService {

    private final RestTemplate restTemplate = new RestTemplate();

//    @Value("${api.url}")
//    private String url;

//    public void changeRequestStatusInProductionCrm(Long requestId) {
//        String url = UriComponentsBuilder.fromHttpUrl(this.url + "/crm/requests/" + requestId
//                        + "/set-state/in-production"
//                )
//                .build(false)
//                .toUriString();
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<?> entity = new HttpEntity<>(null, headers);
//
//            ResponseEntity<?> responseEntity = restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    entity,
//                    new ParameterizedTypeReference<>() {
//                    });
//        } catch (Exception e) {
//            System.out.println("impossible");
//        }
//    }
//
//    public void sendToCrmCountForBatchItem(Long requestId, Long requestItemId, Long quantity) {
//        String url = UriComponentsBuilder.fromHttpUrl(this.url + "/crm/requests/" +
//                        requestId +
//                        "/items/" +
//                        requestItemId +
//                        "/add-execution-qty" +
//                        "/" +
//                        quantity
//                )
//                .build(false)
//                .toUriString();
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<?> entity = new HttpEntity<>(null, headers);
//
//            ResponseEntity<Object> responseEntity = restTemplate.exchange(
//                    url,
//                    HttpMethod.PUT,
//                    entity,
//                    new ParameterizedTypeReference<>() {
//                    });
//        } catch (Exception e) {
//            System.out.println("impossible");
//        }
//    }
//
//    private List<RequestDto> getRequestsFromApi() {
//        String url = UriComponentsBuilder.fromHttpUrl(this.url + "/crm/requests")
//                .build(false)
//                .toUriString();
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<?> entity = new HttpEntity<>(null, headers);
//
//            ResponseEntity<List<RequestDto>> responseEntity = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    entity,
//                    new ParameterizedTypeReference<List<RequestDto>>() {
//                    });
//
//            List<RequestDto> result = responseEntity.getBody();
//            if (result == null)
//                System.out.println("Null");
//            return result;
//        } catch (Exception e) {
//            System.out.println("impossible");
//        }
//        return null;
//    }
//
//    public RequestDto getRequestById(Long id) {
//        String url = UriComponentsBuilder.fromHttpUrl(this.url + "/crm/requests/" + id.toString())
//                .build(false)
//                .toUriString();
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<?> entity = new HttpEntity<>(null, headers);
//
//            ResponseEntity<RequestDto> responseEntity = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    entity,
//                    new ParameterizedTypeReference<RequestDto>() {
//                    });
//
//            RequestDto result = responseEntity.getBody();
//            if (result == null)
//                System.out.println("Null");
//            return result;
//        } catch (Exception e) {
//            System.out.println("impossible");
//        }
//        return null;
//    }

}
