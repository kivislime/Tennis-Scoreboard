<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Теннисный счетчик</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #eef2f7;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background-color: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 400px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        input[type="text"] {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 16px;
        }

        button {
            font-size: 16px;
            padding: 12px 24px;
            margin: 10px 5px;
            border: none;
            border-radius: 10px;
            background-color: #5cb85c;
            color: white;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #449d44;
        }

        a button {
            background-color: #4e91f9;
        }

        a button:hover {
            background-color: #376fd1;
        }

        #error {
            display: none;
            padding: 10px;
            border-radius: 8px;
            margin-top: 10px;
            margin-bottom: 10px;

            text-align: center;
            font-size: 18px;
            color: #c62828;
            background-color: #ffcdd2;
            max-width: 500px;
            margin-left: auto;
            margin-right: auto;
            box-shadow: 0 0 10px rgba(198, 40, 40, 0.3);
            animation: fadeIn 0.4s ease-in-out;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Creating a new match</h1>

    <form id="matchForm">
        <input type="text" name="first_player_name" placeholder="Name of the first player" required>
        <input type="text" name="second_player_name" placeholder="Name of the second player" required>

        <div id="error"></div>

        <button type="submit">Create match</button>
    </form>

    <a href="matches.jsp">
        <button>List of matches</button>
    </a>
</div>

<script>
    document.getElementById('matchForm').addEventListener('submit', function (e) {
        e.preventDefault();

        const form = e.target;
        const firstPlayer = form.first_player_name.value;
        const secondPlayer = form.second_player_name.value;
        const errorDiv = document.getElementById('error');

        fetch('new-match', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                first_player_name: firstPlayer,
                second_player_name: secondPlayer
            })
        }).then(async response => {
            if (!response.ok) {
                const error = await response.json();
                errorDiv.style.display = 'block';
                errorDiv.textContent = error.message;
            } else {
                window.location.href = response.url || 'matches.jsp';
            }
        }).catch(err => {
            errorDiv.style.display = 'block';
            errorDiv.textContent = 'Error when connecting to the server.';
        });
    });
</script>
</body>
</html>
