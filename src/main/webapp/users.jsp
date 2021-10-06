<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
<%@include file="includes/header.jsp" %>
<title>Users</title>
</head>
<body>
<div class="container">

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2><img src="${contextPath}/resources/img/logo.png" alt="HandsOnSpring" /> Users - ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a></h2>

    </c:if>


<nav class="navbar" style="border-bottom: 1px solid #e3f2fd;">
<a href="/users" class="btn btn-primary btn-lg active" role="button" >Users</a>
</nav>
      <div class="controls">
        <button name="save" id="save" class="intext-btn btn btn-primary">Save</button>
        <button name="addRow" id="addRow" class="intext-btn btn btn-primary" >Add row</button>
      </div>
      <div id="responseConsole" class="console alert alert-dark" role="alert""></div>
      <div id="example" class="hot"></div>

<script>
$(function () {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });
});

<%@include file="includes/validators.jsp" %>

var
    $$ = function(id) {
        return document.getElementById(id);
    },
    container = $$('example'),
    save = $$('save'),
    rowChanged = [],
    hot,
    userData = [
    <c:forEach items="${usersData}" var="userData">
        [<c:forEach items="${userData}" var="column">"<c:out value="${column}"/>", </c:forEach>],
    </c:forEach>
    ];

hot = new Handsontable(container, {
    data: userData,
    columns: [
        {
            editor: false,
        },
        {
            validator: function validateId(value, callback) {
                var data = this.instance.getDataAtCol(this.col);
                var index = data.indexOf(value);
                var valid = true;
                if (value == "" || (index > -1 && this.row !== index)) {
                  valid = false;
                }
                return callback(valid);
              }
         }, {   validator: emailValidator  }, {
         type: 'password'
        },
        {
            editor: 'select',
            selectOptions: [<c:forEach items="${roles}" var="role">
            "<c:out value="${role.name}"/>",
            </c:forEach>],
            validator: emptyValidator
        }
    ],
    rowHeaders: true,
    colHeaders: ['id' <c:forEach items="${colHeaders}" var="colHeader">, "<c:out value="${colHeader}"/>"</c:forEach>],
    colWidths: [1, 200, 250, 150, 200],
    contextMenu: [
        'row_above',
        'row_below',
        '---------',
        'undo',
        'redo',
        '---------',
        'copy',
        'cut'
    ],
    afterChange: function (change, source) {
        if (source === 'loadData') {
            return; //don't save this change
        }
        row = change[0][0];
        if(-1 == rowChanged.indexOf(row)) {
            rowChanged.push(row);
        }
    }
});

$("#addRow").click(function() {
    hot.alter("insert_row");
});

Handsontable.dom.addEvent(save, 'click', function() {
  hot.validateCells(function(result, obj) {
    if(result == true) {
        allData = hot.getData();
        changedData = [];

        var index;
        for (index = 0; index < rowChanged.length; ++index) {
            row = rowChanged[index];
            changedData[row] = (allData[row]);
        }
        $.ajax({
            method: "POST",
            url: '${contextPath}/saveUser',
            data: JSON.stringify({data: changedData }),
            dataType: 'json',
            contentType: 'application/json'
        })
        .done(function( msg ) {
            responseConsole.innerText = 'Data saved';
        })
        .fail(function() {
            responseConsole.innerText = 'Data NOT saved';
        });
    }
    else {
        responseConsole.innerText = 'Bad form data';
    }
  });
});
</script>

</div>
<!-- /container -->
</body>
</html>
