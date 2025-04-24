This web application allows you to create, conduct and save the results of tennis matches. It is built according to
architecture
MVCS (Model-View-Controller-Service) and uses:

- **Java 11** / Jakarta EE Servlets
- **Hibernate ORM** + **H2** (in-memory)
- **JSP** + Vanilla **HTML/CSS/JS**
- **JUnit 5** + Mockito (unit/integration)
- **Docker** + **Tomcat 10**

The application includes validation of user requests and error handling.

## Quick Start

### Running the script deploy

`./deploy.sh`

### Manually launching the app

1. **Assemble and test**

`./gradlew clean build`

2. **Launch the container**

```docker build -t tennis-scoreboard:latest .
docker run -d \
--name tennis-scoreboard \
-p 8080:8080 \
--restart unless-stopped \
tennis-scoreboard:latest
```

3. **Open in the browser**

`http://localhost:8080/`

## Functionality

- Create a new match
- Real-time match score management
- Save completed match
- View completed matches
- Search by player name

## Presentation level

- index.jsp is the start page, with the option to start a new match or view completed matches.
- match-score.jsp is a page for managing the match in real time. When the match ends, add points buttons
  they are blocked, and a message about the end of the match is displayed.
- matches.jsp is the Completed matches page.

## Servlets

#### NewMatchServlet ("/new-match")

- GET returns the page. new-match.html
- Does the POST take the first_player_name and second_player_name parameters, create a new match, redirect to
  /match-table?uuid=" + uuid

#### MatchScoreServlet ("/match-score")

- GET Accepts the uuid parameter of the match, returns the state model of the current match
- The POST accepts the uuid and player_number parameters, updates the match score, and returns the current status model.
  The match

#### MatchesListServlet ("/matches")

- GET Returns a list of matches depending on the page by the filter parameter filter_by_player_name and by the page
  number parameter pages

## Services

- PlayerService - Returns an existing player and/or creates a new player
- LiveMatchService: Manages the current matches, tracks the results and executes logic to determine the winners
- MatchQueryService - Returns a list of matches, a list of matches by name, total number of pages, number of pages by
  name
- FinishedMatchService - Saves the completed match

## Tests

JUnit5 was used to implement the tests. The tests cover interactions with the database at the repository level.