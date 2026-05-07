| URL                  | Metodo HTTP | Chi può accedere     |
|----------------------|-------------|----------------------|
| /api/students        | GET         | Tutti                |
| /api/students/{id}   | GET         | USER, ADMIN          |
| /api/students        | POST        | ADMIN                |
| /api/students/{id}   | PUT         | ADMIN                |
| /api/students/{id}   | DELETE      | ADMIN                |


Test paginazione: http://localhost:8080/api/students?page=0&size=5

{
    "content": [
        {
            "id": 1,
            "name": "Mario",
            "surname": "Rossi",
            "email": "mario.rossi@example.com",
            "dob": "1990-01-01",
            "degree": "Ingegneria Informatica"
        },
        {
            "id": 2,
            "name": "Luigi",
            "surname": "Verdi",
            "email": "luigi.verdi@example.com",
            "dob": "1992-05-15",
            "degree": "Matematica"
        },
        {
            "id": 3,
            "name": "Giulia",
            "surname": "Bianchi",
            "email": "giulia.bianchi@example.com",
            "dob": "1991-08-20",
            "degree": "Fisica"
        }
    ],
    "empty": false,
    "first": true,
    "last": true,
    "number": 0,
    "numberOfElements": 3,
    "pageable": {
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 5,
        "paged": true,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "unpaged": false
    },
    "size": 5,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "totalElements": 3,
    "totalPages": 1
}


Test ordinamento: http://localhost:8080/api/students?sort=surname,asc

{
    "content": [
        {
            "id": 3,
            "name": "Giulia",
            "surname": "Bianchi",
            "email": "giulia.bianchi@example.com",
            "dob": "1991-08-20",
            "degree": "Fisica"
        },
        {
            "id": 1,
            "name": "Mario",
            "surname": "Rossi",
            "email": "mario.rossi@example.com",
            "dob": "1990-01-01",
            "degree": "Ingegneria Informatica"
        },
        {
            "id": 2,
            "name": "Luigi",
            "surname": "Verdi",
            "email": "luigi.verdi@example.com",
            "dob": "1992-05-15",
            "degree": "Matematica"
        }
    ],
    "empty": false,
    "first": true,
    "last": true,
    "number": 0,
    "numberOfElements": 3,
    "pageable": {
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 20,
        "paged": true,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "unpaged": false
    },
    "size": 20,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "totalElements": 3,
    "totalPages": 1
}

