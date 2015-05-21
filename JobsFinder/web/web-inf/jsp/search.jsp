<%-- 
    Document   : search
    Created on : Apr 10, 2015, 1:48:54 PM
    Author     : yueningli
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="model.User"%>
<%@page import="model.Job"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<c:url value="/assets/css/customize.css"/>">
        <link rel="stylesheet" href="<c:url value="/assets/css/bootstrap.min.css"/>">
        <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
        <script src="<c:url value="/assets/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/assets/js/validateChecker.js"/>"></script>
        <script src="<c:url value="/assets/js/jquery.js"/>"></script>
    </head>
    <body>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <div class="navbar-brand">
                    Job Hunter
                </div>
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
                    <span class="sr-only">Toggle Navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#"></a>
            </div>
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <ul class="nav navbar-nav">
                    <!-- Server Dropdown -->
                </ul>
                <!-- User Dropdown -->
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="post.html">Post</a></li>
                    <li class="active"><a href="">Search</a></li>
                    <li><a href="profile.html">Profile</a></li>
                    <li><a href="login.html">Log out</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container-fluid">
        <div class="row-main" style=" padding-top:1%">
            <form method="POST" action="search.html" commandName="job">
                <div class="col-md-offset-1 col-lg-11">
                    <label for="input1" class="col-md-1 ">Keyword:</label>
                    <input path="keyword" class="col-md-2" type="text" id="keyword" name="keyword" class="form-control" placeholder=" ">
                    <label for="input2" class="col-md-1 ">Location:</label>
                    <input path="location" class="col-md-2" type="text" id="location" name="location" class="form-control" placeholder=" ">
                    <label for="input3" class="col-md-1 ">Company:</label>
                    <input path="cname" class="col-md-2" type="text" id="cname" name="cname" class="form-control" placeholder=" ">
                    <button class="col-md-1 btn-primary" value="search" type="submit" style="margin-left: 5%">Search</button>
                </div>
            </form>
        </div>
        <div class="row-main" >
            <c:if test="${not empty jobList}">
                <c:forEach items="${jobList}" var="job" varStatus="status">
                    <div class="panel panel-primary col-md-offset-2 col-md-8">
                        <div class="panel-body" style="margin-top: -6%;">
                            <div class="row">
                                Company Name:&nbsp ${job.cname}
                            </div>
                            <div class="row">
                                Title:&nbsp${job.title}
                            </div>
                            <div class="row">
                                Location:&nbsp${job.location}
                            </div>
                            <div class="pull-right">
                                <button type="button" class="btn-primary" data-toggle="modal" data-target="#${job.id}">
                                    <span class="txt">detail</span>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="${job.id}" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                    <h4 class="modal-title" id="myModalLabel">Job Detail Information:</h4>
                                </div>
                                <div class="modal-body">
                                    ${job.description}
                                </div>
                            </div>
                        </div>
                    </div>    
                </c:forEach>
            </c:if>
        </div>
    </div>
</body>
</html>
