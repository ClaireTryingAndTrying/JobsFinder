<%-- 
    Document   : post
    Created on : Apr 10, 2015, 1:18:29 PM
    Author     : yueningli
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Post Page</title>
        <meta charset="UTF-8">
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
                        <li class="active"><a href="">Post</a></li>
                        <li><a href="search.html">Search</a></li>
                        <li><a href="profile.html">Profile</a></li>
                        <li><a href="login.html">Log out</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container-fluid">
            <div class="row-main">
                <div class="col-sm-6 col-sm-offset-3">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <span class="glyphicon glyphicon-user"></span>
                            Post a Job
                        </div>
                        <div class="panel-body">
                            <form method="post" action="insertJob.html" commandName="job"  class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Title:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="title" path="title" class="form-control" placeholder="title"  >
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Description: </label>
                                    <div class="col-sm-6">
                                        <textarea name="description" path="description" class="form-control" placeholder="description"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Requirement:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="requirement" path="requirement" class="form-control" placeholder="requirement">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Company Name:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="cname" path="cname" class="form-control" placeholder="company name">
                                    </div>
                                </div> 
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Company Type:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="ctype" path="ctype" class="form-control" placeholder="company type">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Company Industry:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="cindustry" path="cindustry" class="form-control" placeholder="company industry">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Website:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="website" path="website" class="form-control" placeholder="website">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Location:</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="location" path="location" class="form-control" placeholder="location">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Company Description:</label>
                                    <div class="col-sm-6">
                                        <textarea name="cdescription" path="cdescription" class="form-control" placeholder="company description"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-3 col-sm-offset-4">
                                        <button id="btn_1" class="btn btn-primary btn-block" type="submit" name="post" value="post">Post</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
