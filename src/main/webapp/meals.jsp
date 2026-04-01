<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page import="java.util.List" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table { border-collapse: collapse; }
        th, td { border: 1px solid #888; padding: 6px 10px; }
        .ok { color: green; }
        .excess { color: red; }
    </style>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>

    <tbody>
        <%
            List<MealTo> meals = (List<MealTo>) request.getAttribute("meals");
            if (meals != null) {
                for (MealTo meal : meals) {
                    String css = meal.isExcess() ? "excess" : "ok";
        %>
            <tr class="<%= css %>">
                <td><%= TimeUtil.format(meal.getDateTime()) %></td>
                <td><%= meal.getDescription() %></td>
                <td><%= meal.getCalories() %></td>
            </tr>
        <%
                }
            }
        %>
    </tbody>
</table>

</body>
</html>