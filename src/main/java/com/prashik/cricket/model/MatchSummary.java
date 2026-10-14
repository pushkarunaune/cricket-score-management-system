package com.prashik.cricket.model;

import java.util.List;

public class MatchSummary {
    private String matchId;
    private String title;
    private String leader;
    private String requiredRate;
    private String runRate;
    private String status;
    private String lastUpdated;
    private List<String> highlights;

    public MatchSummary() {
    }

    public MatchSummary(String matchId, String title, String leader, String requiredRate, String runRate,
                       String status, String lastUpdated, List<String> highlights) {
        this.matchId = matchId;
        this.title = title;
        this.leader = leader;
        this.requiredRate = requiredRate;
        this.runRate = runRate;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.highlights = highlights;
    }

    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLeader() { return leader; }
    public void setLeader(String leader) { this.leader = leader; }

    public String getRequiredRate() { return requiredRate; }
    public void setRequiredRate(String requiredRate) { this.requiredRate = requiredRate; }

    public String getRunRate() { return runRate; }
    public void setRunRate(String runRate) { this.runRate = runRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    public List<String> getHighlights() { return highlights; }
    public void setHighlights(List<String> highlights) { this.highlights = highlights; }
}
