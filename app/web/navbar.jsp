<%
    if (request.getRequestURL().toString().contains("navbar.jsp")){
        response.sendRedirect("index.html");
    }
    %>
<div class="navbar-wrapper">

            <div class="container">

                <div class="navbar navbar-inverse navbar-fixed-top">
                    <div class="container">
                        <div class="navbar-header">
                            <button data-target=".navbar-collapse" data-toggle="collapse" class="navbar-toggle" type="button">
                                <span class="fa fa-bars"></span>

                            </button>
                            <a href="#" class="navbar-brand">RMS</a>
                        </div>
                        <div class="navbar-collapse collapse">
                            <ul class="nav navbar-nav navbar-left">
                                <li class=""><a href="mainmenu.jsp"><i class="fa fa-home"></i> Home</a></li>
                                <li class="dropdown">
                                    <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="fa fa-pencil-square-o"></i> Menu<b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="logs.jsp"><i class="fa fa-file-text-o"></i> &nbsp;View Logs</a></li>
                                        <li><a href="startprocess.jsp"><i class="fa fa-file-text-o"></i> &nbsp;Start System</a></li>
                                        <li><a href="changepswd.jsp"><i class="fa fa-file-text-o"></i> &nbsp;Change Password</a></li>
                                        <li class="divider"></li>
                                        
                                        
                                </li>
                                </li>
                                
                            </ul>
                                <li class=""><a href="logout.jsp" class="btn btn-default" style="margin-left:700px;;color:white;">Log Out</a></li>
                        </ul>
                        
                        <span class="logoSQ"><img alt="SIA" src="images/Scoot_Logo.png" class="logoImg"></span>

                    </div>

                </div>

            </div>

        </div>