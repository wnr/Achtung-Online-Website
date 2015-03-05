<?php

$type = null;

if(isset($_GET["action"])){
    $type = "GET";
    $action = $_GET["action"];
}
else if(isset($_POST["action"])){
    $type = "POST";
    $action = $_POST["action"];
}

switch($type){
    case "POST":
        switch($action)
        {
            case "create_account":
                $username = $_POST["username"];
                $password = $_POST["password"];
                $password2 = $_POST["password2"];
                $email = $_POST["email"];
                $email_active = $_POST["email_active"];
                $id_nationality = $_POST["id_nationality"];
            
                require_once("account.php");
            
                $res = createAccount($username, $password, $password2, $email, $email_active, $id_nationality);
                if(is_numeric($res))
                    echo $res;
                else
                    echo json_encode($res);
                    
                die();
            case "create_account_from_application":
                $key = $_POST["key"];
                $username = $_POST["username"];
                $password = $_POST["password"];
                $password2 = $_POST["password2"];
                $email_active = $_POST["email_active"];
                $id_nationality = $_POST["id_nationality"];
                
                $email_active = "1";
                $id_nationality = "0";
                
                require_once("account.php");
                
                $res = createAccountFromBetaApplication($key, $username, $password, $password2, $email_active, $id_nationality);
                if(is_numeric($res))
                    echo $res;
                else
                    echo json_encode($res);
                
                die();
            case "login":
                $email = $_POST["email"];
                $password = $_POST["password"];
                
                require_once("account.php");
                
                $res = login($email, $password);
                echo $res;
                
                die();
            case "apply_beta":
                $email = $_POST["email"];
                $awesome = $_POST["awesome"];
                
                require_once("account.php");
                
                $res = applyForBeta($email, $awesome);
                if(is_numeric($res))
                    echo $res;
                else
                    echo json_encode($res);
                    
                die();
            default:
                echo -2;
                die();
        }
        die();
        
    case "GET":
        die();
        
    default:
        echo -1;
        die();
}

?>