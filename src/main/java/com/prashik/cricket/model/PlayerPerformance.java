package com.prashik.cricket.model;

public class PlayerPerformance {
    private String name;
    private String team;
    private int runs;
    private int balls;
    private int wickets;
    private double strikeRate;
    private String role;

    public PlayerPerformance() {
    }

    public PlayerPerformance(String name, String team, int runs, int balls, int wickets, double strikeRate, String role) {
        this.name = name;
        this.team = team;
        this.runs = runs;
        this.balls = balls;
        this.wickets = wickets;
        this.strikeRate = strikeRate;
        this.role = role;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public int getRuns() { return runs; }
    public void setRuns(int runs) { this.runs = runs; }

    public int getBalls() { return balls; }
    public void setBalls(int balls) { this.balls = balls; }

    public int getWickets() { return wickets; }
    public void setWickets(int wickets) { this.wickets = wickets; }

    public double getStrikeRate() { return strikeRate; }
    public void setStrikeRate(double strikeRate) { this.strikeRate = strikeRate; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
