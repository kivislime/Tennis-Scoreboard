<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список матчей</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f0f4f8;
            margin: 0;
            padding: 2rem;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .back-button-container {
            display: flex;
            justify-content: flex-end; /* Выравнивание по правому краю */
            margin-bottom: 20px; /* Отступ снизу */
        }

        .filter {
            text-align: center;
            margin-bottom: 20px;
        }

        input[type="text"] {
            padding: 10px;
            width: 300px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        button {
            padding: 10px 20px;
            background-color: #4e91f9;
            border: none;
            color: white;
            border-radius: 8px;
            cursor: pointer;
            margin-left: 10px;
        }

        button:hover {
            background-color: #376fd1;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.05);
        }

        th, td {
            padding: 16px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        th {
            background: #f9fafc;
            color: #555;
        }

        tr:hover {
            background: #f1f5f9;
        }
    </style>
</head>
<body>
<h1>Список матчей</h1>

<div class="back-button-container">
    <button onclick="history.back()">Назад</button>
</div>


<div class="filter">
    <input type="text" id="playerFilter" placeholder="Имя игрока">
    <button onclick="loadMatches()">Фильтровать</button>
</div>

<table id="matchesTable">
    <thead>
    <tr>
        <th>ID матча</th>
        <th>Игрок 1</th>
        <th>Игрок 2</th>
        <th>Победитель</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<script>
    function loadMatches() {
        const playerName = document.getElementById("playerFilter").value;
        let url = '/TennisScoreboard/matches';
        if (playerName) {
            url += '?filter_by_player_name=' + encodeURIComponent(playerName);
        }

        fetch(url)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.querySelector("#matchesTable tbody");
                tableBody.innerHTML = ''; // Очистим таблицу

                data.forEach(match => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                            <td>\${match.id}</td>
                            <td>\${match.firstPlayer.name}</td>
                            <td>\${match.secondPlayer.name}</td>
                            <td>\${match.winnerPlayer ? match.winnerPlayer.name : '-'}</td>
                        `;
                    tableBody.appendChild(row);
                });
            })
            .catch(error => console.error("Ошибка при загрузке матчей:", error));
    }

    document.addEventListener("DOMContentLoaded", loadMatches);
</script>
</body>
</html>
