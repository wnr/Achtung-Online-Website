<?php
/*
 File responsible for validating information.
*/

function validUsername($username)
{
    if(ereg("^[A-Za-z0-9]{3,15}$", $username))
        return true;
    
    return false;
}

function validPassword($password)
{
    if(strlen($password) > 6)
        return true;
    
    return false;
}

function validEmail($email)
{
    if(filter_var($email, FILTER_VALIDATE_EMAIL))
        return true;
    
    return false;
}

function validNationality($id_nationality)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    $id_nationality = mysql_real_escape_string($id_nationality, $con);
    
    $res = mysql_query("SELECT id_nationality FROM nationalities WHERE id_nationality='$id_nationality' LIMIT 1");
    
    if(mysql_num_rows($res) > 0)
        return true;
    
    return false;
}

function validApplicationType($id_application_type)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    $id_application_type = mysql_real_escape_string($id_application_type);
    
    $res = mysql_query("SELECT id_application_type FROM id_application_types WHERE id_application_type='$id_application_type' LIMIT 1");
    
    if(mysql_num_rows($res) > 0)
        return false;
    
    return false;
}

function validEmailActive($email_active)
{
    if($email_active == '1' or $email_active == '0')
        return true;
    
    return false;
}

function validAwesome($awesome)
{
    if(strlen($awesome) > 6)
        return true;
    
    return false;
}

?>