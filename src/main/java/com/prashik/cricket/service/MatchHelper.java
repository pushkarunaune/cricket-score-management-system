package com.prashik.cricket.service;

import com.prashik.cricket.model.Match;

public class MatchHelper {
    public static int getTrackRunningScore(Match match) {
        return match.getTeamAScore() + match.getTeamBScore();
    }
}
