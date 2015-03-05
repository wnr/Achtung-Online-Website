<?php
require("head.php");

printHead();
require("jumbotron_header.php");
?>
<h1>Achtung Online</h1> 
<p>Welcome to Achtung Online! This is the game for you that seeks a simple, addictive and yet extremely fun game to challange friends with. Gather around with your favourite snacks and explore the randomness of the newly created version of the well known game "achtung die kurve". Not only have we created crazy powerups and game modes - we have also added a lot of awesesomeness into the game! </p>
<a id="start_game_button" href="game/">
  <img src="img/btn_dark.png" />
</a>
<?php require("jumbotron_footer.php");?>


<div class="container" id="body_container">
  <!-- Example row of columns -->
  <div class="row" >
    <div class="col-md-4">
      <img data-src="holder.js/400*300" class="img-thumbnail"src="img/gameplay_square.png">
      <h2>Customizability</h2>
      <p>We want you to decide how you wanna play the game. You can experience the classical Achtung Die Kurve simply by removing the powerups and even the holes/jumps. If you don´t like the default feeling of the game you can change the speed, turning speed, jump frequency and more. You can also remove a few of the powerups if you don´t like them. If this is to much for you then just screw the settings and play the game!
        <br><br>
        We will continue to develop Achtung Online and would love to hear your thoughts on what features you would like us to implement and what bugs you have experienced (hopefully none). To do this just visit our <a href="https://www.facebook.com/achtungonline">community page</a>.</p>
      </div>
      <div class="col-md-4">
        <img data-src="holder.js/400*300" class="img-thumbnail"src="img/gameplay_circle.png">
        <h2>Different maps</h2>
        <p>If you get bored with the classical square map try out our circle map. But be aware that the wallhack powerup might be hard to master in this mind botteling mirror map.
          <br><br>
          In the future we will add more map types of different sizes and shapes. If you have any request you are most welcome to post it (or vote it up if it exist) in our <a href="https://www.facebook.com/achtungonline">community page</a>.</p>
        </div>
        <div class="col-md-4">

          <img data-src="holder.js/400*300" class="img-thumbnail" src="img/gameplay_options.png">
          <h2>Teamplay or FFA</h2>
          <p>Play FFA up to 12 players or team up against each other. You can play the clasical 2 vs 2 or you can play 4 vs 3 vs 2 vs 1. You decide! Don´t forget that you can change the "Target Score" in settings if the default don´t suite you and your friends.</p>
        </div>
      </div>

      <?php require("main_footer.php");?>

  </div> <!-- /container -->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
</body>

<script>
function changeJumbotronPaddingTop() {
  var headerHeight = $("#header_container").height();
  $("#jumbotron").css({'paddingTop':headerHeight+'px'});
}
window.addEventListener('resize', function(event){
  changeJumbotronPaddingTop();
});
$(this).ready(changeJumbotronPaddingTop());

$('#start_game_button img').hover(function() {
  $(this).attr('src', 'img/btn_light.png');
}, function() {
  $(this).attr('src', 'img/btn_dark.png');
}).mousedown(function() {
  $(this).attr('src', 'img/btn_pressed.png');
});
</script>