<?php
/*
 File responsible for handling user accounts.
*/

function logout()
{
    unset($_SESSION["id_user"]);
    unset($_SESSION["online"]);
}

function login($email, $password)
{
    require_once("validation.php");
    
    if(validEmail($email) == false)
        return 3;
    
    if(validPassword($password) == false)
        return 2;
    
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $password = sha1($password);
    
    $email = mysql_real_escape_string($email, $con);
    $password = mysql_real_escape_string($password, $con);
    
    $res = mysql_query("SELECT id_user FROM users WHERE email='$email' AND password='$password' LIMIT 1");
    
    if(!$res)
        return 8;
    
    if(mysql_num_rows($res) == 0)
        return 9;
    
    $row = mysql_fetch_assoc($res);
    
    $_SESSION["id_user"] = $row["id_user"];
    $_SESSION["online"] = true;
    
    return 0;
}

function createAccount($username, $password, $password2, $email, $email_active, $id_nationality)
{
    //Validation
    
    require_once("validation.php");
    
    if(validUsername($username) == false)
        return 1;
    if(validPassword($password) == false)
        return 2;
    
    if($password != $password2)
        return 12;
    
    if(validEmail($email) == false)
        return 3;
    if(validNationality($id_nationality) == false)
        return 4;
    
    if(validEmailActive($email_active) == false)
        $email_active = "1"; 
    
    //Existence
    
    if(userExists($username) == true)
        return 5;
    if(userEmailExists($email) == true)
        return 6;

    //Create user    

    require_once("connection.php");

    $con = OpenConnection();
    
    if(!$con)
        return 7;

    $password = sha1($password);

    $username = mysql_real_escape_string($username, $con);
    $password = mysql_real_escape_string($password, $con);
    $email = mysql_real_escape_string($email, $con);
    $email_active = mysql_real_escape_string($email_active, $con);
    $id_nationality = mysql_real_escape_string($id_nationality, $con);
    
    $res = mysql_query("INSERT INTO users (username, password, email, email_active, id_nationality) VALUES('$username', '$password', '$email', '$email_active', '$id_nationality')");
    
    if($res == FALSE)
        return 8;
    
    //Success
    return 0;
}

function createAccountFromBetaApplication($key, $username, $password, $password2, $email_active, $id_nationality)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $key = mysql_real_escape_string($key, $con);
    
    $res = mysql_query("SELECT id_user, email FROM applications WHERE unique_activation='$key' AND id_application_status='1' AND verified='1' LIMIT 1");
    
    if(!$res)
        return 8;
    
    if(mysql_num_rows($res) == 0)
        return 9;
    
    $row = mysql_fetch_assoc($res);
    
    $email = $row['email'];
    $id_user = $row['id_user'];
    
    return createAccount($username, $password, $password2, $email, $email_active, $id_nationality);
}

function applyForBeta($email, $awesome)
{
    require_once("validation.php");
    
    if(validEmail($email) == false)
        return 3;
    if(validAwesome($awesome) == false)
        return 10;
    
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $email = mysql_real_escape_string($email);
    $awesome = mysql_real_escape_string($awesome);
    $activation = mysql_real_escape_string($activation);
    
    /*
    
    TODO: Fix so that you only can have one application.
    
    $res = mysql_query("SELECT id_user FROM applications WHERE email='$email' AND id_application_type='1' LIMIT 1");
    
    if(!$res)
        return 8;
    
    if(mysql_num_rows($res) > 0)
        return 11;
    
    */
    
    $id_user = getIdByEmail($email);
    
    if(!$id_user)
        $res = mysql_query("INSERT INTO applications (email, id_application_type, motivation) VALUES('$email', '1', '$awesome')");
    else
        $res = mysql_query("INSERT INTO applications (email, id_application_type, id_user, motivation) VALUES('$email', '1', '$id_user', '$awesome')");
        
    if(!$res)
        return 8;
    
    return 0;
}

function userExists($username)
{
    require_once("connection.php");
    
    $con = OpenConnection();

    $username = mysql_real_escape_string($username);
    
    $res = mysql_query("SELECT id_user FROM users WHERE username='$username' LIMIT 1");
    
    if(mysql_num_rows($res) > 0)
        return true;
    
    return false;
}

function userEmailExists($email)
{
    require_once("connection.php");

    $con = OpenConnection();

    $email = mysql_real_escape_string($email);
    
    $res = mysql_query("SELECT id_user FROM users WHERE email='$email' LIMIT 1");
    
    if(mysql_num_rows($res) > 0)
        return true;
    
    return false;
}

function betaApplicationExists($email, $id_application_type)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    $email = mysql_real_escape_string($email);
    $id_application_type = mysql_real_escape_string($id_application_type);
    
    $res = mysql_query("SELCT id_user FROM applications WHERE email='$email' AND id_application_type='$id_application_type' LIMIT 1");
    
    if(mysql_num_rows($res) > 0)
        return true;
    
    return false;
}

function getIdByEmail($email)
{
    require_once("connection.php");

    $con = OpenConnection();

    $email = mysql_real_escape_string($email);
    
    $res = mysql_query("SELECT id_user FROM users WHERE email='$email' LIMIT 1");
    
    if(mysql_num_rows($res) == 0)
        return false;
    
    $row = mysql_fetch_array($res);
    
    return $row[0];
}

function verifyBetaApplication($key)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $key = mysql_real_escape_string($key);
    
    $res = mysql_query("UPDATE applications SET verified='1' WHERE unique_activation='$key'");
    
    if(!$res)
        return 8;
    
    $res = mysql_query("SELECT id_user FROM applications WHERE unique_activation='$key' LIMIT 1");
    
    if(!$res)
        return 8;
    
    if(mysql_num_rows($res) == 0)
        return 9;
    
    $row = mysql_fetch_assoc($res);
    
    return $row;
}

function getApplications()
{
    if(isset($_SESSION["adminonline"]) == false or $_SESSION["adminonline"] != true)
        return null;
    
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $res = mysql_query("SELECT * FROM applications");
    
    if(!$res)
        return 8;
    
    return $res;
}

function approveApplication($id_application)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $id_application = mysql_real_escape_string($id_application, $con);
    
    $res = mysql_query("SELECT email FROM applications WHERE id_application='$id_application' LIMIT 1");
    
    if(!$res)
        return 8;
    
    if(mysql_num_rows($res) == 0)
        return 9;
    
    $row = mysql_fetch_assoc($res);
    $email = $row['email'];
    
    //Create verification key
    $key = sha1(time() . $email . $id_application);
    $key = mysql_real_escape_string($key);

    $res = mysql_query("UPDATE applications SET id_application_status='1', unique_activation='$key' WHERE id_application='$id_application' LIMIT 1");

    if(!$res)
        return 8;

    //Send email with activation code.
    $subject = "Achtung Online beta application";
    $message = "
    Welcome to Achtung Online!\r\r
    
    We are happy to inform you that your beta application has been approved. Please complete the application process
    by clicking the following link:\r
    http://www.achtungonline.com/verify.php?type=beta&key=$key\r\r
    
    Your Achtung Team
    ";
    
    $headers = "From: noreply@achtungonline.com \r\n
    
                Reply-To: noreply@achtungonline.com \r\n
                
                X-Mailer: PHP/" . phpversion();
                
    mail($email, $subject, $message, $headers);
    
    return 0;
}

function denyApplication($id_application)
{
    require_once("connection.php");
    
    $con = OpenConnection();
    
    if(!$con)
        return 7;
    
    $id_application = mysql_real_escape_string($id_application);
    
    $res = mysql_query("UPDATE applications SET id_application_status='2' WHERE id_application='$id_application' LIMIT 1");
    
    if(!$res)
        return 8;
    
    //TODO end mail about the denied application.
    
    return 0;
}

?>