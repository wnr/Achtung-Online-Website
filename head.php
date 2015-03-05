

<?php

function printHead($root = "./", $js = true){
    
    echo '<?xml version="1.0" encoding="UTF-8"?>
    
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Achtung Online</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="'.$root.'css/bootstrap.css" rel="stylesheet">
    <link href="'.$root.'css/stylesheet.css" rel="stylesheet">

</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top" id="header_container">
      <div class="container">
          <a class="navbar-brand" href="/">Home</a>
          <a class="navbar-brand" href="game/Achtung_Online.jnlp">Play</a>
          <a class="navbar-brand" href="https://www.facebook.com/achtungonline">Community</a>
		  <iframe src="//www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.facebook.com%2Fachtungonline&amp;width&amp;layout=standard&amp;action=like&amp;show_faces=false&amp;share=true&amp;colorscheme=dark&amp;height=35&amp;appId=534043489970951" scrolling="no" frameborder="0" style="border:none; overflow:hidden; height:30px; margin-top: 15px; width:400px" allowTransparency="true"></iframe>
          </div>
    </div>';
} 
?>
