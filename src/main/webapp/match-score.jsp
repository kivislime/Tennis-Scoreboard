<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Match Score</title>
    <style>
        :root {
            --primary-color: #4CAF50;
            --hover-color: #388e3c;
            --background-color: #f2f2f2;
            --container-bg: #fff;
            --text-color: #333;
            --font-family: 'Segoe UI', sans-serif;
        }

        body {
            font-family: var(--font-family), serif;
            background-color: #eef2f7;
            text-align: center;
            padding: 50px 0;
            margin: 0;
        }

        .score-container {
            background-color: var(--container-bg);
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            max-width: 700px;
            width: 90%;
            margin: 0 auto;
        }

        h2 {
            margin-bottom: 20px;
            font-size: 2em;
            color: var(--text-color);
        }

        .score-table {
            width: 100%;
            border-collapse: collapse;
        }

        .score-table th, .score-table td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }

        .score-table th {
            background: #f9fafc;
        }

        button {
            padding: 8px 12px;
            font-size: 14px;
            border: none;
            border-radius: 8px;
            background-color: var(--primary-color);
            color: white;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
            opacity: 0.6;
        }

        button:hover:not(:disabled) {
            background-color: var(--hover-color);
        }

        #matchStatus {
            margin-top: 20px;
            font-size: 18px;
            color: var(--text-color);
        }

        #errorMessage {
            margin-top: 20px;
            font-size: 18px;
            color: #c62828;
            background-color: #ffcdd2;
            padding: 12px 20px;
            border-radius: 10px;
            display: none;
            max-width: 500px;
            margin-left: auto;
            margin-right: auto;
            box-shadow: 0 0 10px rgba(198, 40, 40, 0.3);
            animation: fadeIn 0.4s ease-in-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @media (max-width: 600px) {
            .score-container {
                padding: 20px;
            }

            .score-table th, .score-table td {
                font-size: 14px;
                padding: 8px;
            }

            button {
                width: 100%;
                margin-top: 4px;
            }
        }
    </style>
</head>
<body>
<div class="score-container">
    <h2>Match Score</h2>
    <table class="score-table">
        <thead>
        <tr>
            <th>Player Name</th>
            <th>Sets</th>
            <th>Games</th>
            <th>Points</th>
            <th>+1 Point</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td id="player1Name">Player 1</td>
            <td id="player1Sets">0</td>
            <td id="player1Games">0</td>
            <td id="player1Points">0</td>
            <td>
                <button onclick="addScore(1)">+1</button>
            </td>
        </tr>
        <tr>
            <td id="player2Name">Player 2</td>
            <td id="player2Sets">0</td>
            <td id="player2Games">0</td>
            <td id="player2Points">0</td>
            <td>
                <button onclick="addScore(2)">+1</button>
            </td>
        </tr>
        </tbody>
    </table>
    <div id="matchStatus"></div>
    <div id="errorMessage"></div>
</div>

<script>
    const params = new URLSearchParams(window.location.search);
    const uuid = params.get("uuid");

    function updateScoreboard(data) {
        const {firstPlayerScore, secondPlayerScore, matchDto} = data;

        document.getElementById("player1Name").textContent = `\${matchDto.firstPlayer.name}`;
        document.getElementById("player1Sets").textContent = `\${firstPlayerScore.sets}`;
        document.getElementById("player1Games").textContent = `\${firstPlayerScore.games}`;
        document.getElementById("player1Points").textContent =
            firstPlayerScore.points === -1 ? "AD" : firstPlayerScore.points;

        document.getElementById("player2Name").textContent = `\${matchDto.secondPlayer.name}`;
        document.getElementById("player2Sets").textContent = `\${secondPlayerScore.sets}`;
        document.getElementById("player2Games").textContent = `\${secondPlayerScore.games}`;
        document.getElementById("player2Points").textContent =
            secondPlayerScore.points === -1 ? "AD" : secondPlayerScore.points;

        if (matchDto.winnerPlayer) {
            document.querySelectorAll(".score-table button").forEach(btn => btn.disabled = true);
            document.getElementById("matchStatus").innerHTML = `
                <p>Match completed. Winner: \${matchDto.winnerPlayer.name}</p>
                <a href="index.jsp"><button>Go to Home</button></a>
            `;
        }
    }

    function fetchScore() {
        fetch(`/tennis-scoreboard/match-score?uuid=\${uuid}`)
            .then(res => {
                if (!res.ok) return res.json().then(data => {
                    throw new Error(data.message || 'Error fetching data')
                });
                return res.json();
            })
            .then(data => updateScoreboard(data))
            .catch(err => {
                displayErrorMessage(err.message);
            });
    }

    function addScore(playerNumber) {
        fetch(`/tennis-scoreboard/match-score?uuid=\${uuid}&player_number=\${playerNumber}`, {
            method: "POST"
        })
            .then(res => {
                if (!res.ok) {
                    return res.json().then(data => {
                        const errorCode = data.errorCode;
                        if (errorCode === "MAX_GAMES_EXCEEDED") {
                            displayErrorMessage(data.message);
                            document.querySelectorAll(".score-table button").forEach(btn => btn.disabled = true);
                            return;
                        }
                        throw new Error(data.message || 'Error updating score');
                    });
                }
                return res.json();
            })
            .then(data => {
                if (data) updateScoreboard(data);
            })
            .catch(err => {
                displayErrorMessage(err.message);
            });
    }

    function displayErrorMessage(message) {
        const errorMessageDiv = document.getElementById('errorMessage');
        errorMessageDiv.textContent = message;
        errorMessageDiv.style.display = 'block';
    }

    if (uuid) {
        fetchScore();
    } else {
        alert("Match UUID not found in URL");
    }
</script>
</body>
</html>
