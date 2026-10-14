package com.prashik.cricket.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Match {

    private String id;
    private String title;
    private String teamA;
    private String teamB;
    private String venue;
    private String status;
    private String currentInnings;

    private int teamAScore;
    private int teamAWickets;
    private int teamBScore;
    private int teamBWickets;
    private int balls;
    private int target;
    private int requiredRuns;

    private LocalDateTime lastUpdated;
    private List<String> recentEvents;
    private List<PlayerPerformance> playerStats;

    public Match() {
        this.recentEvents = new ArrayList<>();
        this.playerStats = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    public Match(
            String id,
            String title,
            String teamA,
            String teamB,
            String venue,
            String status,
            String currentInnings,
            int teamAScore,
            int teamAWickets,
            int teamBScore,
            int teamBWickets,
            int balls,
            int target,
            int requiredRuns,
            List<String> recentEvents,
            List<PlayerPerformance> playerStats
    ) {
        this.id = id;
        this.title = title;
        this.teamA = teamA;
        this.teamB = teamB;
        this.venue = venue;
        this.status = status;
        this.currentInnings = currentInnings;

        this.teamAScore = teamAScore;
        this.teamAWickets = teamAWickets;
        this.teamBScore = teamBScore;
        this.teamBWickets = teamBWickets;
        this.balls = balls;
        this.target = target;
        this.requiredRuns = requiredRuns;

        this.lastUpdated = LocalDateTime.now();

        this.recentEvents = recentEvents == null
                ? new ArrayList<>()
                : new ArrayList<>(recentEvents);

        this.playerStats = playerStats == null
                ? new ArrayList<>()
                : new ArrayList<>(playerStats);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentInnings() {
        return currentInnings;
    }

    public void setCurrentInnings(String currentInnings) {
        this.currentInnings = currentInnings;
    }

    public int getTeamAScore() {
        return teamAScore;
    }

    public void setTeamAScore(int teamAScore) {
        this.teamAScore = teamAScore;
    }

    public int getTeamAWickets() {
        return teamAWickets;
    }

    public void setTeamAWickets(int teamAWickets) {
        this.teamAWickets = teamAWickets;
    }

    public int getTeamBScore() {
        return teamBScore;
    }

    public void setTeamBScore(int teamBScore) {
        this.teamBScore = teamBScore;
    }

    public int getTeamBWickets() {
        return teamBWickets;
    }

    public void setTeamBWickets(int teamBWickets) {
        this.teamBWickets = teamBWickets;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getRequiredRuns() {
        return requiredRuns;
    }

    public void setRequiredRuns(int requiredRuns) {
        this.requiredRuns = requiredRuns;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getRecentEvents() {
        return recentEvents;
    }

    public void setRecentEvents(List<String> recentEvents) {
        this.recentEvents = recentEvents;
    }

    public List<PlayerPerformance> getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(List<PlayerPerformance> playerStats) {
        this.playerStats = playerStats;
    }

    public String getOversDisplay() {
        int overs = balls / 6;
        int remainingBalls = balls % 6;

        return overs + "." + remainingBalls;
    }

    public void addRecentEvent(String event) {
        if (recentEvents.size() >= 6) {
            recentEvents.remove(0);
        }

        recentEvents.add(event);
    }

    public int getTrackRunningScore() {
        if (teamA.equals(currentInnings)) {
            return teamAScore;
        }

        return teamBScore;
    }
}