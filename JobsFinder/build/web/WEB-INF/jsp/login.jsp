<%-- 
    Document   : login
    Created on : Apr 6, 2015, 12:41:18 AM
    Author     : yueningli
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Welcome to Job Hunter!</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="<c:url value="/assets/css/customize.css" />" >
        <link rel="stylesheet" href="<c:url value="/assets/css/bootstrap.min.css"/>">
        <link rel="stylesheet" href="<c:url value="/assets/css/login.css"/>">      
        <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>       
        <script src="<c:url value="/assets/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/assets/js/validateChecker.js"/>"></script>
        <script src="<c:url value="/assets/js/jquery.js"/>"></script>
    </head>
    <body>
        <div class="navbar">
            <div class="container">
                <div class="navbar-header">

                    <div class="header-logo-container navbar-brand">
                        <h1><b>Job Hunter</b></h1>
                    </div>

                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <div class="navbar-collapse collapse pull-right">
                        <ul class="nav navbar-nav pull-right">

                        </ul>
                    </div>

                </div>
            </div>
        </div>
    <section class="section-first" id="section-first">
        <div class="container">
            <div class="head_text col-md-12">
                <h1>
                    Get Hired. Love Your Job Forever!!!!
                </h1>

                <h2 class="col-sm-10 col-sm-push-1">
                    Job Hunter offers Pitt students a simple way to find a good job.
                </h2>
            </div>

            <div id="forms_container" class="form_cont col-lg-4 col-sm-4 col-xs-12 pull-right">
                <form class="dark_form" method="POST" action="login.html" commandName="user" novalidate>
                    <legend class="clearfix">
                        <div class="pull-left">
                            <span class="glyphicon glyphicon-lock"></span> Enter Job Hunter
                        </div>
                    </legend>
                    <fieldset class="register-form-fields">
                        <div class="input">
                            <input path="name" type="text" id="name" name="name" class="form-control" placeholder="Username" autocapitalize="off">
                        </div>
                        <div class="input" data-showpassword="">
                            <input path="password" type="password" name="password" class="form-control" placeholder="Password">
                        </div>
                    </fieldset>
                    <fieldset>
                        <button id="submit" type="submit" value="Login" class="fbtn blue">
                            <span class="txt">LOG IN</span>
                        </button>
                    </fieldset>
                </form>
                <form class="dark_form">
                    <legend class="clearfix">
                        <div class="pull-left">
                            <span class="glyphicon glyphicon-user"></span> Don't have an account?
                        </div>
                    </legend>
                    <fieldset>
                        <button type="button" class="fbtn blue" data-toggle="modal" data-target="#sigup">
                            <span class="txt">SIGN UP</span>
                        </button>
                    </fieldset>
                </form>
            </div>
            <div id="video" class="col-sm-7">
                <div id="placeholder">
                    <div id="play_btn" class="play_btn"></div>
                </div>
                <div id="video_placeholder_mobile"></div>
            </div>
        </div>
    </section>
    <div class="modal fade" id="sigup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Sign Up</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal ng-pristine ng-valid" role="form" method="POST" commandName="user" action="register.html">
                        <div class="form-group">
                            <label for="email" class="col-sm-4 control-label">Name</label>
                            <div class="col-sm-6">
                                <input type="name" id="name" name="name" path="name" class="form-control" placeholder="Name" autocapitalize="off">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-sm-4 control-label">Email</label>
                            <div class="col-sm-6">
                                <input type="email" id="email" name="email" path="email" class="form-control" placeholder="Email" autocapitalize="off">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="col-sm-4 control-label">Password</label>
                            <div class="col-sm-6">
                                <input type="password" name="password" path="password" class="form-control" placeholder="Password">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="re_password" class="col-sm-4 control-label">Confirm Password</label>
                            <div class="col-sm-6">
                                <input type="re_password" id="re_password" name="re_password" class="form-control" placeholder="Password" autocapitalize="off">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="remember"> I agree to the Terms of Service.
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-5 col-sm-3">
                                <button type="submit" value="register" class="btn btn-primary btn-block"><i class="fa fa-sign-in"></i>Sign Up</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
