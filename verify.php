<?php

if(isset($_GET["type"]))
{
    switch($_GET["type"])
    {
        case "beta":
            $key = $_GET["key"];
            
            require_once("core/account.php");
            
            $result = verifyBetaApplication($key);
            
            break;
    }
}

if(!isset($result) || $result == 9)
    header("Location: index.php");

require("head.php");

printHead();

require("upperbody.php");

if($result["id_user"] != "")
{
?>
    <h1>BETA APPLICATION</h1>
    <p>
        You have now successfully been approved to our closed beta. Please have lots of fun and don't forget to report any bugs you stumble upon!
    </p>
    <p>
        In order to play the beta, please go back to the <a href="index.php">HOME PAGE</a> and login.
    </p>

<?php
}
else
{
?>
    <h1>BETA APPLICATION</h1>
    <p>
        You have now successfully been approved to our closed beta. Please have lots of fun and don't forget to report any bugs you stumble upon!
    </p>
    <p>
        In order to play the beta, please create an account and then login at the homepage.
    </p>
    <div class="line"></div>
    <div id="createacc">
        <input type="hidden" value="<?php echo $key; ?>" id="createacc_key" />
        <input type="text" id="createacc_username" placeholder="USERNAME" />
        <input type="password" id="createacc_password" placeholder="PASSWORD" />
        <input type="password" id="createacc_password2" placeholder="CONFIRM PASSWORD" />
        <div id="createacc_button" class="button">
            DO IT
        </div>
        <div id="createacc_error">
        </div>
    </div>
    
<?php
}

require("lowerbody.php");
?>