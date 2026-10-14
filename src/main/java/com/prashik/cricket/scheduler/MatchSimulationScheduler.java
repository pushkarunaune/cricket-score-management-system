package com.prashik.cricket.scheduler;

import com.prashik.cricket.service.LiveScoreService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchSimulationScheduler {

    private final LiveScoreService liveScoreService;

    public MatchSimulationScheduler(LiveScoreService liveScoreService) {
        this.liveScoreService = liveScoreService;
    }

    @Scheduled(fixedDelay = 5000)
    public void simulateLiveMatches() {
        liveScoreService.simulateMatchTick();
    }
}
