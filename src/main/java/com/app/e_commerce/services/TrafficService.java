package com.app.e_commerce.services;

import com.app.e_commerce.entity.Traffic;
import com.app.e_commerce.repository.TrafficRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrafficService {

    @Autowired
    private TrafficRepo trafficRepo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;  // Inject messaging template

    public void trackVisit() {
        LocalDate today = LocalDate.now();
        Optional<Traffic> optionalVisit = trafficRepo.findByDate(today);
        Traffic traffic;

        if (optionalVisit.isPresent()) {
            traffic = optionalVisit.get();
            traffic.setCount(traffic.getCount() + 1);
        } else {
            traffic = new Traffic(today, 1L);
        }

        trafficRepo.save(traffic);

        // Send the updated traffic data to WebSocket clients
        messagingTemplate.convertAndSend("/topic/trafficUpdates", getAllTraffic());
    }

    public List<Traffic> getAllTraffic() {
        return trafficRepo.findAll();
    }

    public Optional<Traffic> getVisitByDay(LocalDate date) {
        return trafficRepo.findByDate(date);
    }
}

