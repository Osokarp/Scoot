<%-- 
    Document   : dbtables
    Created on : May 8, 2017, 5:14:59 PM
    Author     : PrakosoNB
--%>

<%@page import="java.util.List"%>
<%@page import="Utility.Database"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <input type="button" value="CREATE"/>
        <input type="button" value="READ"/>
        <input type="button" value="UPDATE"/>
        <input type="button" value="DELETE"/>
        <br>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
        <script>
            function disableSize()
            {
                document.getElementById("size").disabled=true;

            }
            function enableSize()
            {
                document.getElementById("size").disabled=false;

            }
            function disableDecimal()
            {
                document.getElementById("decimal").disabled=true;

            }
            function enableDecimal()
            {
                document.getElementById("decimal").disabled=false;

            }
            function change(){
                var selected = $("#datatype option:selected").attr('id');
                $('#'+selected).click();
            }
            
            function hide(){
                $('#create').hide();
            }
        </script>
        <%
            String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE=\'BASE TABLE\'";
            List<String> dbtables = Database.getAllDBTables();
        %>
        <select name="tablename">
            <%
                for (String columnname:dbtables){
                    out.print("<option value=" + columnname + ">" + columnname + "</option>");
                }
                %>
        </select>
        <div id="create">
            Field Name<input type="text" name="fieldname"/>
            Not Null<input type="checkbox" name="nullnotnull" value="Not Null">
            <select id="datatype" name="datatype" onchange="change()">
                <option id= "CHAR" value="CHAR" onclick="enableSize(); disableDecimal()">CHAR</option>
                <option id="VARCHAR" value="VARCHAR" onclick="enableSize(); disableDecimal();">VARCHAR</option>
                <option id="TINYTEXT" value="TINYTEXT" onclick="disableSize(); disableDecimal();">TINYTEXT</option>
                <option id="TEXT" value="TEXT" onclick="disableSize(); disableDecimal();">TEXT</option>
                <option id="BLOB" value="BLOB" onclick="disableSize(); disableDecimal();">BLOB</option>
                <option id="MEDIUMTEXT" value="MEDIUMTEXT" onclick="disableSize(); disableDecimal();">MEDIUMTEXT</option>
                <option id="MEDIUMBLOB" value="MEDIUMBLOB" onclick="disableSize(); disableDecimal();">MEDIUMBLOB</option>
                <option id="LONGTEXT" value="LONGTEXT" onclick="disableSize(); disableDecimal();">LONGTEXT</option>
                <option id="LONGBLOB" value="LONGBLOB" onclick="disableSize(); disableDecimal();">LONGBLOB</option>
                <option id="SET" value="SET" onclick="disableSize(); disableDecimal();">SET</option>
                <option id="TINYINT" value="TINYINT" onclick="enableSize(); disableDecimal();">TINYINT</option>
                <option id="SMALLINT" value="SMALLINT" onclick="enableSize(); disableDecimal();">SMALLINT</option>
                <option id="MEDIUMINT" value="MEDIUMINT" onclick="enableSize(); disableDecimal();">MEDIUMINT</option>
                <option id="INT" value="INT" onclick="enableSize(); disableDecimal();">INT</option>
                <option id="BIGINT" value="BIGINT" onclick="enableSize(); disableDecimal();">BIGINT</option>
                <option id="FLOAT" value="FLOAT" onclick="enableSize(); enableDecimal();">FLOAT</option>
                <option id="DOUBLE" value="DOUBLE" onclick="enableSize(); enableDecimal();">DOUBLE</option>
                <option id="DECIMAL" value="DECIMAL" onclick="enableSize(); enableDecimal();">DECIMAL</option>
                <option id="DATE" value="DATE" onclick="disableSize(); disableDecimal();">DATE</option>
                <option id="DATETIME" value="DATETIME" onclick="disableSize(); disableDecimal();">DATETIME</option>
                <option id="TIMESTAMP" value="TIMESTAMP" onclick="disableSize(); disableDecimal();">TIMESTAMP</option>
                <option id="TIME" value="TIME" onclick="disableSize(); disableDecimal();">TIME</option>
                <option id="YEAR" value="YEAR" onclick="disableSize(); disableDecimal();">YEAR</option>
            </select>
            <input type="text" id="size" name="size"/>Size
            <input type="number" id="decimal" name="decimal"/>Decimal Point
            <input type="hidden" name="accesstype" value="create"/>
            <input type="button" onclick="hide()"/>
        </div>
    </body>
</html>
