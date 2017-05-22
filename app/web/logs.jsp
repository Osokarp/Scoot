<%@page import="Utility.Log"%>
<%@page import="java.io.File"%>
<!DOCTYPE html>
<html lang="en"><head>
        

        <title>RMS (CSR start)</title>

        <!-- Bootstrap core CSS -->
        <%@include file="include.jsp"%>
    </head>
    <!-- NAVBAR
    ================================================== -->
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


                    <div class="row">
                        <%
                            //String[] logs = Log.getAllLogs();
                            String logs = "ddjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoioiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwoidjaaoiwjdjawodjawodjaoidjawiodjaowajwidioawjdiawjdioawjoawjdoiawjdawjdoiawjiodawdajwdoiajwdioawjdoiawjidoawjdoiajiodawjdioawjiodawjodiawjoidjowaiwa";
                            int min = 0;
                            if (request.getParameter("search") != null && Integer.parseInt(request.getParameter("search")) != 0){
                                min = Integer.parseInt(request.getParameter("search"));
                            }
                            int max = min + 50;
                            if (max > logs.length()){
                                max = logs.length();
                            }
                            
                            int p = 1;
                            if (request.getParameter("p") != null){
                               p = Integer.parseInt(request.getParameter("p"));
                            }
                        %>
                        <h2><p style="text-align:center">System Logs</p></h2>
                        <table width="100%" border="1">
                            <tr>
                                <th width="70%">File</th>
                                <th width="30%">Date/Time</th>       
                            </tr>
                            
                            <%for (int i = min; i < max; i++){
                                %>
                            <tr>
                            <td>
                                
                            </td>
                            <td>
                            <%="date"%>
                            </td>
                            </tr>
                            <%}%>
                        </table>  
                        <br>
                        <p style="text-align:center">
                        <%
                            if (p - 1 > 0){
                                %>
                                <a href="logs.jsp?p=<%=p-1%>&search=<%=500*(p-1)-100%>" style="font-size: 20px;">Prev</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <%
                            }
                            int noPages = (int)Math.ceil(logs.length()/50.0/9);
                            
                            if (p <= 1){
                            for (int i = 1; i < 10; i++){
                                
                                if (i == max/50){
                                    %>
                                    <a href="logs.jsp?p=<%=p%>&search=<%=p*50*(i-1)%>" style="font-size: 20px;"><%=i%></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                       <%
                                }
                                else{%>
                                <a href="logs.jsp?p=<%=p%>&search=<%=p*50*(i-1)%>" style="font-size: 20px;"><%=i%></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <%        
                                }
                                
                                %>
                                
                                <%
                            }
                        }else{
                            for (int i = 10*p-10; i < 10*p; i++){
                                
                                if (i == max/50){
                                    %>
                                    <a href="logs.jsp?p=<%=p%>&search=<%=50*(i-1)%>" style="font-size: 20px;"><%=i%></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                       <%
                                }
                                else{%>
                                    <a href="logs.jsp?p=<%=p%>&search=<%=50*(i-1)%>" style="font-size: 20px;"><%=i%></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <%        
                                }   
                                
                                %>
                                
                                <%
                            }
                        }
                            
                        %>
                        <%
                            if (p+1 <= noPages){%>
                            <a href="logs.jsp?p=<%=p+1%>&search=<%=500*(p)-50%>" style="font-size: 20px;">Next</a>
                            <%}%>
                        </p>
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