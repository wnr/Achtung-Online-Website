<?php
/*
 File responsible for establishing connections to the server
*/

function OpenConnection()
{
    $con = mysql_connect("localhost", "achtungonline", "nuAMZR2WNzJbaWzJ");

    if($con)
    {
        mysql_select_db("achtungonline");
        mysql_set_charset("utf8");
    }
    
    return $con;
}

?>