package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * Service implementation for retrieving departure information.
 */
@Service
public class RetrieveDeparturesServiceImpl implements RetrieveDeparturesService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${FlightService.url}")
    String takeFlightsUrl;

    @Value("${spring.security.user.name}")
    String authUsername;

    @Value("${spring.security.user.password}")
    String authPassword;

    /**
     * Constructor for DepartureServiceImpl.
     *
     * @param restTemplate the RestTemplate to make HTTP requests
     * @param objectMapper the ObjectMapper to serialize and deserialize objects
     */
    @Autowired
    public RetrieveDeparturesServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Retrieves departure information from the TakeFlights microservice.
     *
     * @param airportCode the IATA code of the airport
     * @param date the date of departure
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return the list of DepartureInfo
     */
    @Override
    public List<DepartureInfo> retrieveDepartures(String airportCode, String date, int daysRange, boolean schengenOnly){
        String url = takeFlightsUrl + "origin=" + airportCode
                + "&departureAt=" + date
                + "&schengenOnly=" + schengenOnly
                + "&daysRange=" + daysRange;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        String auth = authUsername + ":" + authPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        headers.set("Authorization", authHeader);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        List<DepartureInfo> departureInfos = null;
        try{
            departureInfos = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return departureInfos;
    }
}
