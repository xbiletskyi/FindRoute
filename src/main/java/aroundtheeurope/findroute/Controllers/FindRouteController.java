package aroundtheeurope.findroute.Controllers;


import aroundtheeurope.findroute.Models.DepartureInfo;
import aroundtheeurope.findroute.Models.FoundTrip;
import aroundtheeurope.findroute.Services.FindRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FindRouteController {
    @Autowired
    private final FindRouteService findRouteService;
    @Value("${time.limit}")
    int timeLimit;

    @Autowired
    public FindRouteController(FindRouteService findRouteService) {
        this.findRouteService = findRouteService;
    }

    @GetMapping("/findroute")
    public ResponseEntity<List<FoundTrip>> findRoute(@RequestParam("origin") String origin,
                                                               @RequestParam("destination") String destination,
                                                               @RequestParam("departureAt") String departureAt,
                                                               @RequestParam("budget") double budget,
                                                               @RequestParam("maxStay") int maxStay) {
        List<FoundTrip> routes = findRouteService.findRoute(origin, destination, departureAt, maxStay,
                budget, timeLimit);
        return ResponseEntity.ok(routes);
    }
}
