# How to run application
### you need to have docker and docker-compose installed
### To run the application locally, copy the repository,<br> go to the "startup" directory in Explorer and run "run.sh".<br> To stop, run "stop .sh"

# Endpoints
### All endpoints except /aurh require an access token!

## 1. Authentication/Registration
### /api/v1/auth/register
#### Page for register user
- POST
- Body Example:
  -     {"username": "username", "login": "login", "password": "Password123)" }
- Response: UserDto

### /api/v1/auth/login
#### Page for login user
- POST
- Body Example:
  -     {"login": "login", "password": "Password123)" }
- Response Example:
  -     { "accessToken": "some access JWT tocken", "refreshToken": "some refresh JWT tocken" }

### /api/v1/auth/refresh
#### Page for refresh JWT token
- POST
- Body Example:
  -     {"refreshToken": "some refresh JWT tocken" }
- Response Example:
  -     { "accessToken": "some access JWT tocken" }

## 2. Game sessions management
### /api/v1/sessions/{sessionId}/start
#### Endpoint for start game session
- POST
- Response: GameSessionDto

### /api/v1/sessions/{sessionId}
#### Endpoint for join game session
- POST
- Response: GameSessionDto

### /api/v1/sessions/{sessionId}
#### Endpoint for get game session by id
- GET
- Response: GameSessionDto

### /api/v1/sessions
#### Endpoint gor create game session
- POST
- Response: GameSessionDto

## Turns
### /api/v1/sessions/{sessionId}/turn
#### endpoint for making a move in a game session
- Post
- Params: targetId: Long
- Response: TurnDto

## DTO
### GameSessionStatus
- enum class GameSessionStatus {
  -     CREATED
  -     IN_PROGRESS
  -     FINISHED
  - }

### gameSessionDto
- var id: Long
- players: List<UserDto>
- status: GameSessionStatus
- deckSize: Int
- turnHistory: List<TurnDto>
- createdBy: UserDto
- scoreBoard: List<PlayerScoreDto>
#### json example:

```json
{
  "id": "someId",
  "players": [
    {
      "id": "someId",
      "username": "someUsername",
      "login": "someLogin"
    }
  ],
  "status": "someStatus",
  "deckSize": "someDeckSize",
  "turnHistory": [
    {
      "player": {
        "id": "someId",
        "username": "someUsername",
        "login": "someLogin"
      },
      "target": {
        "id": "someId",
        "username": "someUsername",
        "login": "someLogin"
      },
      "card": {
        "name": "someName",
        "value": "someValue",
        "cardType": "someCardType"
      }
    } 
  ],
  "createdBy": {
    "id": "someId",
    "username": "someUsername",
    "login": "someLogin"
  },
  "scoreBoard": [
    {
      "user": {
        "id": "someId",
        "username": "someUsername",
        "login": "someLogin"
      },
      "score": "someScore"
    }
  ]
}
```

### userDto
- var id: Long
- username: String
- login: String
#### json example:

```json
{
  "id": "someId",
  "username": "someUsername",
  "login": "someLogin"
}
```

### turnDto
- player: UserDto
- target: UserDto
- card: CardDto
#### json example:

```json
{
  "player": {
    "id": "someId",
    "username": "someUsername",
    "login": "someLogin"
  },
  "target": {
    "id": "someId",
    "username": "someUsername",
    "login": "someLogin"
  },
  "card": {
    "name": "someName",
    "value": "someValue",
    "cardType": "someCardType"
  }
}
```

### cardDto
- name: String
- value: String
- cardType: String
#### json example:

```json
{
  "name": "someName",
  "value": "someValue",
  "cardType": "someCardType"
}
```

### playerScoreDto
- player: UserDto
- score: Int
#### json example:

```json
{
    "player": {
        "id": 1,
        "username": "someUsername",
        "login": "someLogin"
    },
    "score": 10
}
```
