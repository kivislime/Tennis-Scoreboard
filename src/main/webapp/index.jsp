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
            background-color: #4e91f9;
            color: white;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #376fd1;
        }

        a button {
            background-color: #5cb85c;
        }

        a button:hover {
            background-color: #449d44;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Создание нового матча</h1>

    <form action="new-match" method="post">
        <input type="text" name="first_player_name" placeholder="Имя первого игрока" required>
        <input type="text" name="second_player_name" placeholder="Имя второго игрока" required>
        <br>
        <button type="submit">Создать матч</button>
    </form>

    <a href="matches.jsp">
        <button>Список матчей</button>
    </a>
</div>
</body>
</html>
