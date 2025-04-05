<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Счет теннисного матча</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #ece9e6, #ffffff);
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .container {
            background-color: #fefefe;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            width: 90%;
            text-align: center;
        }

        h1, h2 {
            color: #333;
            margin-bottom: 20px;
        }

        input[type="text"] {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            margin: 5px;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #45a049;
        }

        #currentScore {
            font-size: 20px;
            color: #222;
            margin: 20px 0;
        }
    </style>
    <script>
        function updateScore(playerNumber) {
            const uuid = document.getElementById('matchId').value;
            console.log('updateScore вызван, UUID:', uuid, 'playerNumber:', playerNumber);
            fetch('/TennisScoreboard/match-score?uuid=' + uuid + '&player_number=' + playerNumber, {
                method: 'POST'
            })
                .then(response => {
                    console.log('POST-ответ, статус:', response.status);
                    return response.json();
                })
                .then(data => {
                    console.log('POST-данные:', data);
                    document.getElementById('currentScore').textContent =
                        `\${data.match.firstPlayer.name}: \${data.firstPlayerScore} | \${data.match.secondPlayer.name}: \${data.secondPlayerScore}`;
                })
                .catch(error => console.error('Ошибка в updateScore:', error));
        }

        function refreshScore() {
            const uuid = document.getElementById('matchId').value;
            console.log('refreshScore вызван, UUID:', uuid);
            fetch('/TennisScoreboard/match-score?uuid=' + uuid)
                .then(response => {
                    console.log('GET-ответ, статус:', response.status);
                    return response.json();
                })
                .then(data => {
                    console.log('GET-данные:', data);
                    document.getElementById('currentScore').textContent =
                        `\${data.match.firstPlayer.name}: \${data.firstPlayerScore} | \${data.match.secondPlayer.name}: \${data.secondPlayerScore}`;
                })
                .catch(error => console.error('Ошибка в refreshScore:', error));
        }

        document.addEventListener('DOMContentLoaded', function () {
            setInterval(refreshScore, 5000);
        });
    </script>
</head>
<body>
<div class="container">
    <h1>Текущий счет матча</h1>
    <input type="text" id="matchId" placeholder="Введите UUID матча">
    <button onclick="refreshScore()">Обновить счет</button>
    <div id="currentScore">Здесь будет отображаться счет</div>
    <h2>Увеличить счет:</h2>
    <button onclick="updateScore('player1')">Игрок 1</button>
    <button onclick="updateScore('player2')">Игрок 2</button>
</div>
</body>
</html>
