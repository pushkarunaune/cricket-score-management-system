package com.prashik.cricket.service;

import com.prashik.cricket.model.Match;
import com.prashik.cricket.model.MatchSummary;
import com.prashik.cricket.model.PlayerPerformance;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LiveScoreService {

    private final Map<String, Match> matches = new ConcurrentHashMap<>();
    private final List<SseEmitter> subscribers = new CopyOnWriteArrayList<>();

    public LiveScoreService() {
        seedMatches();
    }

    private void seedMatches() {

        matches.put("ind-aus", new Match(
                "ind-aus",
                "India vs Australia",
                "India",
                "Australia",
                "Wankhede Stadium, Mumbai",
                "Live",
                "India",
                142,
                5,
                123,
                2,
                18,
                24,
                15,
                List.of(
                        "Rohit Sharma launched a six over mid-wicket.",
                        "Suryakumar Yadav is dealing in boundaries.",
                        "Australia tightened the bowling in the final over."
                ),
                List.of(
                        new PlayerPerformance(
                                "Rohit Sharma",
                                "India",
                                42,
                                22,
                                0,
                                190.9,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Suryakumar Yadav",
                                "India",
                                31,
                                16,
                                0,
                                193.75,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Pat Cummins",
                                "Australia",
                                12,
                                9,
                                2,
                                133.3,
                                "Bowler"
                        ),
                        new PlayerPerformance(
                                "Adam Zampa",
                                "Australia",
                                0,
                                0,
                                1,
                                0.0,
                                "Bowler"
                        )
                )
        ));

        matches.put("eng-sa", new Match(
                "eng-sa",
                "England vs South Africa",
                "England",
                "South Africa",
                "Lord's, London",
                "Live",
                "England",
                185,
                6,
                165,
                4,
                14,
                19,
                21,
                List.of(
                        "Jos Buttler started with a quick 24-run over.",
                        "South Africa were building pressure with yorkers.",
                        "The chase is still alive in the middle overs."
                ),
                List.of(
                        new PlayerPerformance(
                                "Jos Buttler",
                                "England",
                                58,
                                31,
                                0,
                                187.1,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Ben Stokes",
                                "England",
                                41,
                                24,
                                0,
                                170.8,
                                "All-rounder"
                        ),
                        new PlayerPerformance(
                                "Kagiso Rabada",
                                "South Africa",
                                11,
                                8,
                                2,
                                137.5,
                                "Bowler"
                        ),
                        new PlayerPerformance(
                                "Tabraiz Shamsi",
                                "South Africa",
                                0,
                                0,
                                1,
                                0.0,
                                "Bowler"
                        )
                )
        ));

        matches.put("nz-pak", new Match(
                "nz-pak",
                "New Zealand vs Pakistan",
                "New Zealand",
                "Pakistan",
                "MCG, Melbourne",
                "Upcoming",
                "New Zealand",
                0,
                0,
                0,
                0,
                0,
                20,
                0,
                List.of(
                        "Toss in a few minutes.",
                        "Ground conditions are ideal for brisk scoring.",
                        "Both teams are preparing for a high-intensity chase."
                ),
                List.of(
                        new PlayerPerformance(
                                "Daryl Mitchell",
                                "New Zealand",
                                0,
                                0,
                                0,
                                0.0,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Rachin Ravindra",
                                "New Zealand",
                                0,
                                0,
                                0,
                                0.0,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Babar Azam",
                                "Pakistan",
                                0,
                                0,
                                0,
                                0.0,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Shaheen Afridi",
                                "Pakistan",
                                0,
                                0,
                                0,
                                0.0,
                                "Bowler"
                        )
                )
        ));
    }

    public List<Match> getAllMatches() {
        return new ArrayList<>(matches.values());
    }

    public Match getMatch(String id) {
        return matches.get(id);
    }

    public MatchSummary getMatchSummary(String id) {

        Match match = matches.get(id);

        if (match == null) {
            return null;
        }

        String leader = match.getCurrentInnings();

        String requiredRate = match.getRequiredRuns() > 0
                ? (match.getRequiredRuns() /
                Math.max(1, match.getBalls() / 6.0)) + " rpo"
                : "-";

        String runRate = match.getBalls() > 0
                ? String.format(
                        "%.2f",
                        (double) match.getTeamBScore() /
                                (match.getBalls() / 6.0)
                )
                : "0.00";

        return new MatchSummary(
                match.getId(),
                match.getTitle(),
                leader,
                requiredRate,
                runRate,
                match.getStatus(),
                match.getLastUpdated().format(
                        DateTimeFormatter.ofPattern("HH:mm:ss")
                ),
                match.getRecentEvents()
        );
    }

    public SseEmitter subscribeToLiveUpdates() {

        SseEmitter emitter = new SseEmitter(60_000L);

        subscribers.add(emitter);

        emitter.onCompletion(() -> subscribers.remove(emitter));
        emitter.onTimeout(() -> subscribers.remove(emitter));
        emitter.onError((ex) -> subscribers.remove(emitter));

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connected")
                            .data("Connected to live updates")
            );
        } catch (IOException e) {
            subscribers.remove(emitter);
        }

        return emitter;
    }

    public void simulateMatchTick() {

        for (Match match : matches.values()) {

            if ("Upcoming".equalsIgnoreCase(match.getStatus())) {
                continue;
            }

            int run = ThreadLocalRandom.current().nextInt(0, 8);

            boolean wicketFell =
                    ThreadLocalRandom.current().nextInt(0, 10) > 7;

            match.setBalls(match.getBalls() + 1);
            match.setLastUpdated(LocalDateTime.now());

            if (match.getCurrentInnings().equals(match.getTeamA())) {

                match.setTeamAScore(
                        match.getTeamAScore() + run
                );

                if (wicketFell && match.getTeamAWickets() < 10) {
                    match.setTeamAWickets(
                            match.getTeamAWickets() + 1
                    );
                }

                match.setRequiredRuns(
                        Math.max(
                                0,
                                match.getTarget() -
                                        match.getTeamAScore()
                        )
                );

                match.addRecentEvent(
                        run + " run(s) from " +
                                match.getTeamA() +
                                " in the chase."
                );

            } else {

                match.setTeamBScore(
                        match.getTeamBScore() + run
                );

                if (wicketFell && match.getTeamBWickets() < 10) {
                    match.setTeamBWickets(
                            match.getTeamBWickets() + 1
                    );
                }

                match.setRequiredRuns(
                        Math.max(
                                0,
                                match.getTarget() -
                                        match.getTeamBScore()
                        )
                );

                match.addRecentEvent(
                        run + " run(s) from " +
                                match.getTeamB() +
                                " in the chase."
                );
            }

            if (match.getBalls() >= 20 &&
                    match.getStatus().equalsIgnoreCase("Live")) {

                match.setStatus("Completed");

                match.addRecentEvent(
                        "Match finished. Final score posted."
                );
            }

            if (match.getTrackRunningScore() > match.getTarget()) {
                match.setStatus("Completed");
            }
        }

        broadcastUpdates();
    }

    public void broadcastUpdates() {

        List<Match> payload = getAllMatches();

        for (SseEmitter emitter : subscribers) {

            try {

                emitter.send(
                        SseEmitter.event()
                                .name("match-update")
                                .data(payload)
                );

            } catch (IOException e) {
                subscribers.remove(emitter);
            }
        }
    }

    public void addSampleMatch() {

        String id = "new-match-" + System.nanoTime();

        Match match = new Match(
                id,
                "India vs Bangladesh",
                "India",
                "Bangladesh",
                "Eden Gardens, Kolkata",
                "Live",
                "India",
                118,
                4,
                96,
                3,
                9,
                18,
                21,
                List.of(
                        "Live match started with aggressive intent."
                ),
                List.of(
                        new PlayerPerformance(
                                "Shubman Gill",
                                "India",
                                48,
                                31,
                                0,
                                154.8,
                                "Batter"
                        ),
                        new PlayerPerformance(
                                "Taskin Ahmed",
                                "Bangladesh",
                                11,
                                8,
                                2,
                                137.5,
                                "Bowler"
                        )
                )
        );

        matches.put(id, match);

        broadcastUpdates();
    }
}