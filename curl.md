
# Получить всю еду текущего пользователя
curl -X GET http://localhost:8080/topjava/rest/profile/meals

# Получить еду по id
curl -X GET http://localhost:8080/topjava/rest/profile/meals/100003

# Создать новую еду
curl -X POST http://localhost:8080/topjava/rest/profile/meals -H "Content-Type: application/json" -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}'

# Обновить еду
curl -X PUT http://localhost:8080/topjava/rest/profile/meals/100003 -H "Content-Type: application/json" -d '{"id":100003,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}'

# Удалить еду
curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/100003

# Получить еду за период
curl -X GET "http://localhost:8080/topjava/rest/profile/meals/between?startDateTime=2020-01-30T00:00:00&endDateTime=2020-01-31T23:59:59"