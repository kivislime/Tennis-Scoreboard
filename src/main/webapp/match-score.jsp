<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Счёт матча</title>
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
            font-family: var(--font-family);
            background-color: var(--background-color);
            text-align: center;
            padding: 50px 0;
            margin: 0;
        }

        .score-container {
            background-color: var(--container-bg);
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            width: 90%;
            margin: 0 auto;
        }

        h2 {
            margin-bottom: 20px;
            font-size: 2em;
            color: var(--text-color);
        }

        .player {
            font-size: 24px;
            margin: 10px 0;
            color: var(--text-color);
        }

        button {
            margin-top: 10px;
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            border-radius: 8px;
            background-color: var(--primary-color);
            color: white;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: var(--hover-color);
        }

        @media (max-width: 600px) {
            .score-container {
                padding: 20px;
            }

            button {
                width: 100%;
            }
        }
    </style>
</head>
<body>

<div class="score-container">
    <h2>Счёт матча</h2>
    <div class="player" id="player1">Игрок 1: 0</div>
    <div class="player" id="player2">Игрок 2: 0</div>

    <button onclick="addScore(1)">+1 Игроку 1</button>
    <button onclick="addScore(2)">+1 Игроку 2</button>
</div>

<script>
    const params = new URLSearchParams(window.location.search);
    const uuid = params.get("uuid");

    function updateScoreboard(data) {
        const {firstPlayerScore, secondPlayerScore, match} = data;
        document.getElementById('player1').textContent = `\${match.firstPlayer.name}: \${firstPlayerScore}`;
        document.getElementById('player2').textContent = `\${match.secondPlayer.name}: \${secondPlayerScore}`;
    }

    function fetchScore() {
        fetch(`/TennisScoreboard/match-score?uuid=\${uuid}`)
            .then(res => {
                if (!res.ok) throw new Error('Ошибка получения данных');
                return res.json();
            })
            .then(data => updateScoreboard(data))
            .catch(err => alert('Ошибка получения счёта: ' + err.message));
    }

    function addScore(playerNumber) {
        fetch(`/TennisScoreboard/match-score?uuid=\${uuid}&player_number=\${playerNumber}`, {
            method: "POST",
        })
            .then(res => {
                if (!res.ok) throw new Error('Ошибка обновления счёта');
                return res.json();
            })
            .then(data => updateScoreboard(data))
            .catch(err => alert('Ошибка обновления счёта: ' + err.message));
    }

    if (uuid) {
        fetchScore();
    } else {
        alert("UUID матча не найден в URL");
    }
</script>

</body>
</html>
