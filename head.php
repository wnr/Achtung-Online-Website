<?php

function printHead($root = "./", $js = true){
    
    echo '<?xml version="1.0" encoding="UTF-8"?>
    
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Achtung Online</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    
    <link rel="stylesheet" href="'.$root.'css/style.css" type="text/css">';
    
    
    if($js){
        echo '<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
    <script type="text/javascript">
    if (typeof jQuery == "undefined") {
        document.write(unescape("%3Cscript src=' . $root . "'/js/jquery-1.6.2.min.js'" . $root . "type='text/javascript'" . '%3E%3C/script%3E"));
    }
    </script>
    
    <script type="text/javascript" src="'.$root.'js/jquery.placeholder.js"></script>
    <script type="text/javascript" src="'.$root.'js/jquery.bgstretcher.js"></script>
    
    <script type="text/javascript" src="'.$root.'js/app.js"></script>
    ';
    }
    
    echo '<script type="text/javascript">
        var _gaq = _gaq || [];
     _gaq.push(["_setAccount", "UA-24270170-1"]);
     _gaq.push(["_setDomainName", "none"]);
     _gaq.push(["_setAllowLinker", true]);
     _gaq.push(["_trackPageview"]);
   
     (function() {
       var ga = document.createElement("script"); ga.type = "text/javascript"; ga.async = true;
       ga.src = ("https:" == document.location.protocol ? "https://ssl" : "http://www") + ".google-analytics.com/ga.js";
       var s = document.getElementsByTagName("script")[0]; s.parentNode.insertBefore(ga, s);
     })();
    </script>
</head>';

}

?>
