

<?php

function printHead($root = "./", $js = true){
    
    echo '<?xml version="1.0" encoding="UTF-8"?>
    
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Achtung Online</title>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="'.$root.'bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="'.$root.'jumbotron.css" rel="stylesheet">
    <link href="'.$root.'css/stylesheet.css" rel="stylesheet">
</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a style="margin-left: -5px;" class="navbar-brand" href="/">Home</a>
          <a class="navbar-brand" href="game/">Play</a>
          <a class="navbar-brand" href="https://www.facebook.com/achtungonline">Community</a>
          <iframe id = "facebook_frame"src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Ffacebook.com/achtungonline&amp;layout=button_count&amp;show_faces=true&amp;action=like&amp;colorscheme=light" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:150px; height:30px; margin-top: 15px;" allowTransparency="true" ></iframe>
      </div>
    </div>';
} 
?>
