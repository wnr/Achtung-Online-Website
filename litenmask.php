<?php
session_start();

if(isset($_GET["logout"]))
{
    session_destroy();
    header("Location: index.php");
    die();
}

if($_GET["no"] != "holes"){
    header("Location: index.php");
    die();
}

require("head.php");

printHead();

require("upperbody.php");

if($_POST["password"] == "achtungonlineZsFI5G" or $_SESSION["adminonline"] == true)
{
    $_SESSION["adminonline"] = true;
    require("core/account.php");
    
    if(isset($_GET["approve"])){
        approveApplication($_GET["approve"]);
    }
    else if(isset($_GET["deny"])){
        denyApplication($_GET["deny"]);
    }
?>
    <h1>APPLICATIONS</h1>
    <p>Please keep in mind that the users are being mailed on apply/deny events. Don't spam them and don't abuse the power.</p>
    </p>DO NOT FORGET TO <a href="litenmask.php?logout">LOG OUT</a></p>
    <table>
        <thead>
            <tr>
                <th>Actions</th>
                <th class="textcenter">A. id</th>
                <th class="textcenter">U. id</th>
                <th>E-mail</th>
                <th>Motivation</th>
                <th>Date</th>
                <th class="textcenter">Status</th>
                <th class="textcenter">Verified</th>
            </tr>
        </thead>
        <tbody>
            <?php
                $res = getApplications();
                
                $count = 0;
                
                while($row = mysql_fetch_assoc($res))
                {
                    $count++;
                    
                    $date = substr($row['date'], 0, 10);
                    
                    $approve = $deny = "";
                    
                    if($row['id_application_status'] != 1){
                        $approve = "<a href='litenmask.php?no=holes&approve={$row['id_application']}'>Approve</a>";
                    }
                    if($row["id_application_status"] != 2){
                        $deny = "<a href='litenmask.php?no=holes&deny={$row['id_application']}'>Deny</a>";
                    }
                    
                    
                    echo "<tr>";
                    echo "<td>$approve / $deny</td>";
                    echo "<td class='textcenter'>{$row['id_application']}</td>";
                    echo "<td class='textcenter'>{$row['id_user']}</td>";
                    echo "<td>{$row['email']}</td>";
                    echo "<td>{$row['motivation']}</td>";
                    echo "<td>{$date}</td>";
                    echo "<td class='textcenter'>{$row['id_application_status']}</td>";
                    echo "<td class='textcenter'>{$row['verified']}</td>";
                    echo "</tr>";
                }
            ?>
        </tbody>
        <tfoot>
            <tr>
                <td>Total: <?php echo $count; ?></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </tfoot>
    </table>
    
    <br />
    
    <a href="litenmask.php?logout">LOG OUT</a>
<?php
}
else
{
?>
    <h1>AUTHORIZED STAFF ONLY</h1>
    <p>
        If you have reached this page by mistake, please <a href="index.php">click here</a> to return to the home page.
    </p>
    <p>
        Use the ultra strong password to access the admin site.
    </p>
<div class="line"></div>
<div id="apply">
    <h1>LOG IN</h1>
    <form action="#" method="POST">
    <input type="password" id="password" name="password" placeholder="PASSWORD" />
    </form>
</div>

<?php

}

require("lowerbody.php");
?>