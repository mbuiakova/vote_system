# Vote system
[![CI](https://github.com/mbuiakova/vote_system/actions/workflows/ci.yml/badge.svg)](https://github.com/mbuiakova/vote_system/actions/workflows/ci.yml)
[![MIT licensed](https://img.shields.io/badge/license-no--license-lightgrey)](./LICENSE)

A simple vote system for restaurants' menu.

# Examples

All the examples here assume building and running on `localhost:8080/vote_system`.

## Get all restaurants without menus (as a simple user)

Request:
```
curl -u "user@domain.com:123456" -X GET "http://localhost:8080/vote_system/restaurants" -H "accept: application/json"
```
Response:
```
[{"id":3,"name":"Dominos","menus":null},{"id":4,"name":"TrackFood","menus":null},{"id":5,"name":"PapaPizza","menus":null}]
```

## Get all votes for an existing restaurant (as a simple user)

Request:
```
curl -u "user@domain.com:123456" -X GET "http://localhost:8080/vote_system/restaurants/4/votes" -H "accept: application/json"
```

Response:
```
[{"restaurantId":4,"date":"2021-04-20","userId":2}]
```


## Create new menu for today for a restaurant (as an admin)

Request:
```
curl -u "admin@domain.com:654321" -X POST "http://localhost:8080/vote_system/restaurants/3/menu" -H "accept: application/json" -H "Content-Type: application/json" -d "\"our new menu\""
```

Response:
```
{"date":"2021-05-07","menu":"our new menu"}
```


## Vote for a restaurant (as a simple user)

Request:
```
curl -u "user@domain.com:123456" -X POST "http://localhost:8080/vote_system/restaurants/3/vote" -H "accept: application/json"
```

Response:
```
{"restaurantId":3,"date":"2021-05-07","userId":1}
```

More documentation is generated in openapi format by swagger and available at: http://localhost:8080/vote_system/swagger-ui.html

## License
No-license (all rights belong to the owner).