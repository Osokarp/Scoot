<!DOCTYPE html>
<html lang="en"><head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="RMS" name="description">
        <meta content="JeanMin" name="author">
        <link href="favi.ico" rel="shortcut icon">

        <title>RMS (CSR start)</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="css/bootstrap.css">
        <link rel="stylesheet" href="css/font-awesome.css">
        <link rel="stylesheet" href="css/style.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="js/html5shiv.js"></script>
          <script src="js/respond.min.js"></script>
        <![endif]-->

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="css/carousel.css">
        <style id="holderjs-style" type="text/css">.holderjs-fluid {font-size:16px;font-weight:bold;text-align:center;font-family:sans-serif;margin:0}</style>
        <style>

        </style>
    </head>
    <!-- NAVBAR
    ================================================== -->
    <script>
        function validatePassword() {

            var username = document.getElementById("username").value;
            var password = document.getElementById("password").value;
            var newpassword = document.getElementById("newpassword").value;
            var newpasswordconfirm = document.getElementById("newpasswordconfirm").value;
            

            if (newpassword != newpasswordconfirm) {
                document.getElementById("msg").innerHTML = "Your new passwords do not match!";
                document.getElementById("msg").hidden = false;
                alert('ho');
            } else {
                
                $.post("ChangePasswordController",
                        {
                            username: username,
                            password: password,
                            newpassword: newpassword,
                            newpasswordconfirm: newpasswordconfirm
                        },
                        function (data, status) {
                            document.getElementById("msg").innerHTML = data;
                            document.getElementById("msg").hidden = false;
                        });
            }
            

        }
    </script>

    <body>
        <%@include file="validatelogin.jsp"%>
        <%@include file="navbar.jsp"%>
        

        <div class="featurette-divider"/>


        <!-- Marketing messaging and featurettes
        ================================================== -->
        <!-- Wrap the rest of the page in another container to center all the content. -->

        <div class="container marketing">

            <div class="jumbotron light">
                <div class="row">

                    <h3 style="text-align:center">Change Password</h3>
                    <div class="row">
                        <form class="form-signin" action="ChangePasswordController" method="POST">
                            <%out.print(session.getAttribute("user"));%>
                            <input type="password" id="password" placeholder="Current Password" class="form-control" name="password">
                            <input type="password" id="newpassword" placeholder="New Password" class="form-control" name="newpassword">
                            <input type="password" id="newpasswordconfirm" placeholder="Confirm New Password" class="form-control" name="newpasswordconfirm">
                            <input type="hidden" id="username" autofocus="" placeholder="User ID" class="form-control" name="username">
                            </br>
                            <input type="button" onclick="validatePassword()" value="Confirm" class="btn btn-lg btn-primary btn-block"/>
                        </form>
                        <br>
                        <p id="msg" hidden></p>
                        <div class="col-lg-4">

                            </br>

                        </div><!-- /.col-lg-4 -->

                    </div><!-- /.col-lg-4 -->
                </div><!-- /.row -->
                <br/><br/>
            </div>
        </div></div>


    <hr class="featurette-divider">

    <!-- FOOTER -->
    <footer>
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>&copy; 2012 Scoot&#8482; Pte Ltd. All Rights Reserved.  

        </p>
    </footer>

</div><!-- /.container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/jquery-2.1.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/holder.js"></script>
<script>

                                            function showRes() {
                                                $('#searchRes').show();
                                                scroll($('#searchRes'), 60);
                                            }
                                            function scroll(div, offset) {
                                                // Scroll to the popup
                                                var scrollTo = (div).offset().top;
                                                $("html:not(:animated),body:not(:animated)").animate({scrollTop: scrollTo - offset}, 500);

                                            }

</script>


</body></html>