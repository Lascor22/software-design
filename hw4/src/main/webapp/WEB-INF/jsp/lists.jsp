<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html lang="ru">
<c:set var="listsPresent" value="${listsOfTasks != null && not empty listsOfTasks}"/>

<head>
    <meta charset="UTF-8"/>
    <title>Страница задач</title>
    <link rel="stylesheet" href="/resources/css/lists.css"/>
    <link rel="shortcut icon" href="/resources/gfx/tasks.png" type="image/x-icon">
</head>

<body>
<header id="main-header" onclick="location.href='/'">
    <span>TODO</span><img src="/resources/gfx/tasks.png"/>
</header>

<div id="control-container">
    <button id="add-list-button">
        <img src="/resources/gfx/add.png"/> <span>Список задач</span>
    </button>
</div>

<c:if test="${listsPresent}">
    <div id="lists-container">
        <c:forEach var="list" items="${listsOfTasks}">
            <div>
                <header>${list.getHeader()}</header>

                <div class="list-control-container">
                    <button list="${list.getId()}"
                            title="${list.getHeader()}"
                            class="add-task-button">
                        <img src="/resources/gfx/add.png"/>
                    </button>
                    <button list="${list.getId()}"
                            class="delete-list-button">
                        <img src="/resources/gfx/bin.png"/>
                    </button>
                </div>

                <c:set var="tasks" value="${list.getTasks()}"/>

                <c:choose>
                    <c:when test="${tasks != null && not empty tasks}">
                        <ul>
                            <c:forEach var="task" items="${tasks}">
                                <c:set var="styleClass" value="${task.getStatus().getStyle()}"/>
                                <c:set var="status" value="${task.getStatus().toString()}"/>

                                <li task="${task.getId()}"
                                    class="task ${styleClass}">
                                    <span>${task.getDescription()}</span>
                                    <c:if test="${task.hasExpireDate()}">
                                        <span><b>До:</b> ${task.getExpireDate()}</span>
                                    </c:if>
                                    <span><b>Статус:</b>${status}</span>
                                    <button class="delete-task-button">
                                        <img src="/resources/gfx/bin.png"/>
                                    </button>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>

                    <c:otherwise>
                        <div class="no-tasks-message">Скоро тут что-то будет...</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </div>
</c:if>
<c:if test="${!listsPresent}">
    <p id="no-lists-message">
        Нет списков задач... вы можете <b>добавить</b> список
    </p>
</c:if>

<div id="add-list-shadow" class="shadow ma">
    <div>
        <header>Создать новый список</header>
        <div>Название:
            <input id="add-list-title" type="text"/>
        </div>
        <div>
            <span id="add-list-error"></span>
            <button id="add-list-button-final">создать список</button>
        </div>
    </div>
</div>

<div id="add-task-shadow" class="shadow">
    <div>
        <header>Создать новую задачу</header>
        <div>Описание:
            <input id="add-task-desc" type="text"/>
        </div>
        <div>Дедлайн:
            <div>
                <input id="add-task-date" type="date" lang="ru"/>
                <input id="add-task-time" type="time" lang="ru"/>
            </div>
        </div>
        <div>Список:
            <div id="add-task-label"></div>
            <input id="add-task-list" type="hidden" value=""/>
        </div>
        <div>
            <span id="add-task-error"></span>
            <button id="add-task-button">создать задачу</button>
        </div>
    </div>
</div>

<script type="text/javascript" src="/resources/js/index.js"></script>
</body>
</html>