<%@page import="Utility.Log"%>
<%@page import="java.util.List"%>
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
        <script>
            function startTime(hi) {

            }
        </script>
    </head>
    <!-- NAVBAR
    ================================================== -->
    <body onload="startTime(1)">

        <%@include file="navbar.jsp"%>

        <div class="featurette-divider"/>


        <!-- Marketing messaging and featurettes
        ================================================== -->
        <!-- Wrap the rest of the page in another container to center all the content. -->

        <div class="container marketing">

            <div class="jumbotron light">
                <div class="row">


                    <div class="row">
                        <h2><p style="text-align:center">System Log</p></h2>
                        <% String fpath = request.getParameter("f");
                            List<String> lines = Log.getLogLines(fpath);
                        %>
                        <table width="100%" border="1">
                            <tr>
                                <th width="10%">Line</th>
                                <th width="90%">Description</th>       
                            </tr>

                            <%for (int i = 0; i < lines.size(); i++) {
                            %>
                            <tr>
                                <td>
                                    <%=i + 1%>
                                </td>
                                <td>
                                    <%=lines.get(i)%>
                                </td>
                            </tr>
                            <%}%>
                        </table>

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