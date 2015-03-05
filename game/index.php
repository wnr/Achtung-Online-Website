<?php
session_start();
/*
if(!isset($_SESSION["online"]) or $_SESSION["online"] != true){
  header("Location: ../index.php");
  die();
}*/

require_once("../head.php");

printHead("../")
?> 
        <div class="container" id="game_container">
            <applet code="org.lwjgl.util.applet.AppletLoader" archive="lwjgl_util_applet.jar" codebase="." width="800" height="600">
                <param name="al_title" value="achtungonline">
                <param name="al_main" value="org.newdawn.slick.AppletGameContainer">
                <param name="al_debug" value="false">
                <param name="game" value="client.core.Engine">
                <param name="al_logo" value="appletlogo.gif">
                <param name="al_progressbar" value="appletprogress.gif">
                <param name="al_jars" value="game.jar, lwjgl.jar, slick.jar, jogg-0.0.7.jar, jorbis-0.0.15.jar, kryonet-1.04-all.jar, jinput.jar, asm-debug-all.jar, ibxm.jar, jnlp.jar, lwjgl_util.jar, AppleJavaExtensions.jar, tinylinepp.jar, lzma.jar">
                <param name="al_windows" value="windows_natives.jar">
                <param name="al_linux" value="linux_natives.jar">
                <param name="al_mac" value="macosx_natives.jar">
                <param name="al_solaris" value="solaris_natives.jar">
                <param name="al_version" value="0.7">
                <!-- <param name="al_cache" value="true"> -->
                <param name="al_prepend_host" value="true">
                <!-- <param name="java_arguments" value="-Djava.library.path=/nativestest"> -->

                <param name="separate_jvm" value="true">
                <!-- <param name="codebase_lookup" value="false"> -->
            </applet>
        </div>
        <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
</body>

<script>
function changeGamePaddingTop() {
  var headerHeight = $("#header_container").height();
  $("#game_container").css({'paddingTop':headerHeight+'px'});
}
window.addEventListener('resize', function(event){
  changeGamePaddingTop();
});
$(this).ready(changeGamePaddingTop());
</script>
