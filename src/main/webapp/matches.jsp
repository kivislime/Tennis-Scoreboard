<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Match List</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f0f4f8;
            margin: 0;
            padding: 2rem;
            max-width: 1400px;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .back-button-container {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 20px;
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
            max-width: 1200px;
            margin: 0 auto;
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

        .pagination {
            margin-top: 20px;
            text-align: center;
        }

        .pagination button {
            margin: 0 4px;
            padding: 6px 12px;
            border-radius: 6px;
            border: none;
            background-color: #e0e7ff;
            color: #333;
            cursor: pointer;
        }

        .pagination button.active {
            background-color: #4e91f9;
            color: white;
            font-weight: bold;
        }

        .pagination button:hover {
            background-color: #c7d2fe;
        }

        #error {
            text-align: center;
            margin-bottom: 20px;
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
    </style>
</head>
<body>
<h1>Match List</h1>

<div class="back-button-container">
    <a href="index.jsp">
        <button>Back to Home</button>
    </a>
</div>

<div class="filter">
    <input type="text" id="playerFilter" placeholder="Player Name">
    <button onclick="loadMatches(1)">Filter</button>
</div>

<div id="error"></div>

<table id="matchesTable">
    <thead>
    <tr>
        <th>Match ID</th>
        <th>Player 1</th>
        <th>Player 2</th>
        <th>Winner</th>
    </tr>
    </thead>
    <tbody></tbody>
</table>

<div class="pagination" id="pagination"></div>

<script>
    function loadMatches(pageNumber = 1) {
        const playerName = document.getElementById("playerFilter").value.trim();
        let url = `/tennis-scoreboard/matches?page=\${pageNumber}`;
        if (playerName) {
            url += `&filter_by_player_name=\${encodeURIComponent(playerName)}`;
        }

        const errorDiv = document.getElementById('error');
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.message || 'Error loading matches');
                    });
                }
                return response.json();
            })
            .then(data => {
                renderMatches(data.matches);
                renderPagination(data.totalPages, data.currentPage);
            })
            .catch(error => {
                showError(error.message);
            });
    }

    function renderMatches(matches) {
        const tableBody = document.querySelector("#matchesTable tbody");
        tableBody.innerHTML = '';

        if (matches.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="4" style="text-align:center;">No matches available</td></tr>';
        }

        matches.forEach(match => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>\${match.id}</td>
                <td>\${match.firstPlayer.name}</td>
                <td>\${match.secondPlayer.name}</td>
                <td>\${match.winnerPlayer ? match.winnerPlayer.name : '-'}</td>
            `;
            tableBody.appendChild(row);
        });

        document.querySelector("#matchesTable").style.display = 'table';
        document.querySelector("#pagination").style.display = 'block';
    }

    function renderPagination(totalPages, currentPage) {
        const paginationContainer = document.getElementById("pagination");
        paginationContainer.innerHTML = '';

        const maxVisible = 5;
        let start = Math.max(2, currentPage - 2);
        let end = Math.min(totalPages - 1, currentPage + 2);

        if (currentPage <= 3) {
            start = 2;
            end = Math.min(totalPages - 1, maxVisible);
        }

        if (currentPage >= totalPages - 2) {
            start = Math.max(2, totalPages - maxVisible + 1);
            end = totalPages - 1;
        }

        if (totalPages > 0) {
            paginationContainer.appendChild(createPageButton(1, currentPage));
        }

        if (start > 2) {
            const dots = document.createElement("span");
            dots.textContent = "...";
            paginationContainer.appendChild(dots);
        }

        for (let i = start; i <= end; i++) {
            paginationContainer.appendChild(createPageButton(i, currentPage));
        }

        if (end < totalPages - 1) {
            const dots = document.createElement("span");
            dots.textContent = "...";
            paginationContainer.appendChild(dots);
        }

        if (totalPages > 1) {
            paginationContainer.appendChild(createPageButton(totalPages, currentPage));
        }
    }

    function createPageButton(page, currentPage) {
        const button = document.createElement("button");
        button.textContent = page;
        if (page === currentPage) {
            button.classList.add("active");
        }
        button.onclick = () => loadMatches(page);
        return button;
    }

    function showError(message) {
        const errorDiv = document.getElementById('error');
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';

        document.querySelector("#matchesTable").style.display = 'none';
        document.querySelector("#pagination").style.display = 'none';
    }

    document.addEventListener("DOMContentLoaded", () => loadMatches(1));
</script>
</body>
</html>
