<?php
session_start();
/*
if(!isset($_SESSION["online"]) or $_SESSION["online"] != true){
  header("Location: ../index.php");
  die();
}*/

require_once("../head.php");

printHead("../", false)
?>
  <body class="bg_black">
    <applet code="org.lwjgl.util.applet.AppletLoader" archive="lwjgl_util_applet.jar" codebase="." width="800" height="600">
        <param name="al_title" value="achtungonline">
        <param name="al_main" value="org.newdawn.slick.AppletGameContainer">
        <param name="al_debug" value="false">
        <param name="game" value="core.Engine">
        <param name="al_logo" value="appletlogo.png">
        <param name="al_progressbar" value="appletprogress.gif">
        <param name="al_jars" value="game.jar, lwjgl.jar, jinput.jar, lwjgl_util.jar, slick.jar, ibxm.jar, jogg-0.0.7.jar, jorbis-0.0.15.jar">
        <param name="al_windows" value="windows_natives.jar">
        <param name="al_linux" value="linux_natives.jar">
        <param name="al_mac" value="macosx_natives.jar">
        <param name="al_solaris" value="solaris_natives.jar">
        <param name="al_version" value="0.2">
        <param name="al_cache" value="true">
        <param name="al_prepend_host" value="true">
        <param name="java_arguments" value="-Dsun.java2d.noddraw=true -Dsun.awt.noerasebackground=true -Dsun.java2d.d3d=false -Dsun.java2d.opengl=false -Dsun.java2d.pmoffscreen=false">
        <param name="separate_jvm" value="true">
        <param name="codebase_lookup" value="false">
    </applet>
    
  </body>
</html>
