const matchList = document.getElementById('matchList');
const matchDetail = document.getElementById('matchDetail');
const activeMatchesEl = document.getElementById('activeMatches');
const completedMatchesEl = document.getElementById('completedMatches');
const upcomingMatchesEl = document.getElementById('upcomingMatches');
const feedStatusEl = document.getElementById('feedStatus');
const refreshButton = document.getElementById('refreshButton');
const demoMatchButton = document.getElementById('demoMatchButton');

let matches = [];
let selectedMatchId = null;
let eventSource = null;

async function fetchMatches() {
  const response = await fetch('/api/matches');
  matches = await response.json();
  renderMatches();
  updateSummaryCards();
  if (!selectedMatchId && matches.length) {
    selectedMatchId = matches[0].id;
  }
  renderMatchDetail();
}

function renderMatches() {
  if (!matches.length) {
    matchList.innerHTML = '<div class="empty-state">No live matches are available.</div>';
    return;
  }

  matchList.innerHTML = matches
    .map((match) => {
      const badgeClass = match.status.toLowerCase() === 'live' ? 'live' : match.status.toLowerCase() === 'upcoming' ? 'upcoming' : 'completed';
      const selectedClass = match.id === selectedMatchId ? 'selected' : '';
      return `
        <button class="match-card ${selectedClass}" data-id="${match.id}">
          <div class="match-header">
            <div>
              <h3>${match.title}</h3>
              <div class="match-meta">${match.venue}</div>
            </div>
            <span class="badge ${badgeClass}">${match.status}</span>
          </div>
          <div class="score-row">
            <div class="score-line">${match.teamA} <small>${match.teamAScore}/${match.teamAWickets}</small></div>
            <div class="score-line">${match.teamB} <small>${match.teamBScore}/${match.teamBWickets}</small></div>
          </div>
          <div class="match-meta">Overs: ${match.oversDisplay || match.balls}</div>
        </button>
      `;
    })
    .join('');

  document.querySelectorAll('.match-card').forEach((card) => {
    card.addEventListener('click', () => {
      selectedMatchId = card.dataset.id;
      renderMatches();
      renderMatchDetail();
    });
  });
}

function updateSummaryCards() {
  const active = matches.filter((match) => match.status.toLowerCase() === 'live').length;
  const completed = matches.filter((match) => match.status.toLowerCase() === 'completed').length;
  const upcoming = matches.filter((match) => match.status.toLowerCase() === 'upcoming').length;

  activeMatchesEl.textContent = active;
  completedMatchesEl.textContent = completed;
  upcomingMatchesEl.textContent = upcoming;
}

async function renderMatchDetail() {
  const match = matches.find((item) => item.id === selectedMatchId);
  if (!match) {
    matchDetail.className = 'match-detail empty-state';
    matchDetail.textContent = 'Select a match to view live details.';
    return;
  }

  const summaryResponse = await fetch(`/api/matches/${match.id}/summary`);
  const summary = await summaryResponse.json();

  matchDetail.className = 'match-detail';
  matchDetail.innerHTML = `
    <div class="score-summary">
      <div class="summary-block">
        <small>${match.teamA}</small>
        <strong>${match.teamAScore}/${match.teamAWickets}</strong>
      </div>
      <div class="summary-block">
        <small>${match.teamB}</small>
        <strong>${match.teamBScore}/${match.teamBWickets}</strong>
      </div>
    </div>

    <div class="match-meta">Venue: ${match.venue}</div>
    <div class="match-meta">Status: ${match.status}</div>
    <div class="match-meta">Overs: ${match.oversDisplay || match.balls}</div>
    <div class="match-meta">Current innings: ${match.currentInnings}</div>
    <div class="match-meta">Target: ${match.target}</div>
    <div class="match-meta">Required runs: ${match.requiredRuns}</div>

    <h3>Highlights</h3>
    <ul class="highlight-list">
      ${(summary.highlights || match.recentEvents || []).map((event) => `<li>${event}</li>`).join('')}
    </ul>
  `;
}

async function refreshFeed() {
  await fetchMatches();
  feedStatusEl.textContent = 'Updated';
  setTimeout(() => {
    feedStatusEl.textContent = 'Connected';
  }, 1200);
}

async function addDemoMatch() {
  await fetch('/api/matches/add-demo', { method: 'POST' });
  await fetchMatches();
}

function connectStream() {
  if (eventSource) {
    eventSource.close();
  }

  eventSource = new EventSource('/api/live/stream');
  eventSource.addEventListener('match-update', async () => {
    await fetchMatches();
  });

  eventSource.onopen = () => {
    feedStatusEl.textContent = 'Connected';
  };

  eventSource.onerror = () => {
    feedStatusEl.textContent = 'Reconnecting';
  };
}

refreshButton.addEventListener('click', refreshFeed);
demoMatchButton.addEventListener('click', addDemoMatch);

fetchMatches();
connectStream();
