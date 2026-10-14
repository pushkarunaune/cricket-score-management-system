package com.prashik.cricket.controller;

import com.prashik.cricket.model.Match;
import com.prashik.cricket.model.MatchSummary;
import com.prashik.cricket.service.LiveScoreService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final LiveScoreService liveScoreService;

    public MatchController(LiveScoreService liveScoreService) {
        this.liveScoreService = liveScoreService;
    }

    @GetMapping("/matches")
    public List<Match> getAllMatches() {
        return liveScoreService.getAllMatches();
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<Match> getMatch(@PathVariable String id) {
        Match match = liveScoreService.getMatch(id);
        return match == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(match);
    }

    @GetMapping("/matches/{id}/summary")
    public ResponseEntity<MatchSummary> getSummary(@PathVariable String id) {
        MatchSummary summary = liveScoreService.getMatchSummary(id);
        return summary == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(summary);
    }

    @GetMapping(value = "/live/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLiveUpdates() {
        return liveScoreService.subscribeToLiveUpdates();
    }

    @PostMapping("/matches/{id}/trigger")
    public ResponseEntity<String> triggerSimulation(@PathVariable String id) {
        Match match = liveScoreService.getMatch(id);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }

        match.setStatus("Live");
        match.setLastUpdated(java.time.LocalDateTime.now());
        match.addRecentEvent("Manual update triggered by admin dashboard.");
        liveScoreService.broadcastUpdates();
        return ResponseEntity.ok("Updated live match scores");
    }

    @PostMapping("/matches/add-demo")
    public ResponseEntity<String> addDemoMatch() {
        liveScoreService.addSampleMatch();
        return ResponseEntity.ok("Demo match added");
    }
}
