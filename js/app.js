var app = {
    version: '0.1'
}

app.urls = {
    image_background: 'http://www.achtungonline.com/img/background_dirty.jpg' 
}

app.ids = {
    apply_email: 'apply_email',
    apply_awesome: 'apply_awesome',
    
    login_email: 'login_email',
    login_password: 'login_password',
    
    createacc_key: 'createacc_key',
    createacc_username: 'createacc_username',
    createacc_password: 'createacc_password',
    createacc_password2: 'createacc_password2',
    
    error_apply: 'apply_error',
    error_login: 'login_error',
    error_createacc: 'createacc_error',
    
    button_apply: 'apply_button',
    button_login: 'login_button',
    button_createacc: 'createacc_button'
}

app.texts = {
    invalid_email: 'Please enter a valid e-mail address.',
    invalid_awesome: 'Please write something about yourself.',
    invalid_password: 'Please enter a valid password.',
    invalid_username: 'Please enter a valid username.',
    error_ajax: 'An internal server error has occured. Please contanct site administrator.',
    login_failed: 'Wrong e-mail or password.',
    username_exists: 'This username is already in use.',
    passwords_not_matching: "The passwords doesn't match."
}

app.responses = {
    success: 0,
    invalid_username: 1,
    invalid_password: 2,
    invalid_email: 3,
    invalid_nationality: 4,
    already_exists_username: 5,
    already_exists_email: 6,
    database_connection_error: 7,
    database_result_error: 8,
    database_empty_result: 9,
    application_exists: 11,
    invalid_awesome: 10,
    passwords_not_matching: 12
}

app.presentation = {
    init: function(){
        $(document).bgStretcher({
           images: [app.urls.image_background], imageWidth: 1403, imageHeight: 1052
        });
        
        $('input[placeholder],textarea[placeholder]').placeholder();

        $("#" + app.ids.login_password).keyup(function(e){
            if (e.keyCode == 13) {
                app.presentation.login();
                return false;
            }
        
            return true;
        });
        
        $("#" + app.ids.button_apply).bind("click", app.presentation.apply);
        $("#" + app.ids.button_login).bind("click", app.presentation.login);
        $("#" + app.ids.button_createacc).bind("click", app.presentation.createAccFromBetaApplication);
        
        $(":input").focus(app.presentation.inputFocus);
    },
    login: function(){
        var email = $("#" + app.ids.login_email).val();
        var password = $("#" + app.ids.login_password).val();
        
        app.domain.login(email, password);
    },
    loginSuccess: function(){
        $("#" + app.ids.button_login).html("DONE");
        $("#" + app.ids.button_login).addClass("success");
        $("#" + app.ids.error_login).html("Redirecting...");
        $("#" + app.ids.login_email).val("");
        $("#" + app.ids.login_awesome).val("");
    },
    loginFailed: function(){
        $("#" + app.ids.error_login).html(app.texts.login_failed);
    },
    apply: function(){
        var email = $("#" + app.ids.apply_email).val();
        var awesome = $("#" + app.ids.apply_awesome).val();
        
        app.domain.apply(email, awesome);
    },
    applySuccess: function(){
        $("#" + app.ids.button_apply).html("DONE");
        $("#" + app.ids.button_apply).addClass("success");
        $("#" + app.ids.error_apply).html("Thanks for your application.");
        $("#" + app.ids.apply_email).val("");
        $("#" + app.ids.apply_awesome).val("");
    },
    createAccFromBetaApplication: function(){
        var key = $("#" + app.ids.createacc_key).val();
        var username = $("#" + app.ids.createacc_username).val();
        var password = $("#" + app.ids.createacc_password).val();
        var password2 = $("#" + app.ids.createacc_password2).val();
        
        app.domain.createAccountFromBetaApplication(key, username, password, password2);
    },
    createAccFromBetaApplicationSuccess: function(){
        $("#" + app.ids.button_createacc).html("DONE").addClass("success");
        $("#" + app.ids.error_createacc).html("Account created! <a href='index.php'>Please login</a> to play the game.");
    },
    invalidInput: function(id, text){
        $("#" + id).addClass("error");
        
        if(id.indexOf("login") != -1)
            $("#" + app.ids.error_login).html(text);
        else if(id.indexOf("apply") != -1)
            $("#" + app.ids.error_apply).html(text);
        else if(id.indexOf("createacc") != -1)
            $("#" + app.ids.error_createacc).html(text);
        else
            alert(text);
    },
    ajaxError: function(){
        alert(app.texts.error_ajax);
    },
    inputFocus: function(){
        $("#" + app.ids.error_apply).html("");
        $("#" + app.ids.error_login).html("");
        $("#" + app.ids.error_createacc).html("");
        
        $(this).removeClass("error");
        
        $("#" + app.ids.button_login).html("DO IT").removeClass("success");
        $("#" + app.ids.button_apply).html("DO IT").removeClass("success");
        $("#" + app.ids.button_createacc).html("DO IT").removeClass("success");
    }
}

app.domain = {
    init: function(){
        
    },
    login: function(email, password){
        $.ajax({type:'POST', url:'core/api.php', data: 'action=login&email=' + email + '&password=' + password, error: function(xhr, ajaxOptions, thrownError){
            app.presentation.ajaxError();
            app.domain.ajaxError(xhr, ajaxOptions, thrownError);
        },
        success: function(data){
            response = $.parseJSON(data);
            switch(response){
                case app.responses.success:
                    app.domain.loginSuccess();
                    break;
                case app.responses.invalid_email:
                    app.presentation.invalidInput(app.ids.login_email, app.texts.invalid_email);
                    break;
                case app.responses.invalid_password:
                    app.presentation.invalidInput(app.ids.login_password, app.texts.invalid_password);
                    break;
                case app.responses.database_empty_result:
                    app.presentation.loginFailed();
                    break;
                case app.responses.database_connection_error:
                case app.responses.database_result_error:
                    app.domain.databaseError();
                    break;
                default:
                    app.presentation.ajaxError();
                    app.domain.invalidAjaxResult(response);
            }
        }});
    },
    loginSuccess: function(){
        window.location.href = "http://www.achtungonline.com/game/index.php";
    },
    apply: function(email, awesome){
        $.ajax({type:'POST', url:'core/api.php', data: 'action=apply_beta&email=' + email + '&awesome=' + awesome, error: function(xhr, ajaxOptions, thrownError){
            app.presentation.ajaxError();
            app.domain.ajaxError(xhr, ajaxOptions, thrownError);
        },
        success: function(data){
            response = $.parseJSON(data);
            switch(response){
                case app.responses.success:
                    app.presentation.applySuccess();
                    break;
                case app.responses.invalid_email:
                    app.presentation.invalidInput(app.ids.apply_email, app.texts.invalid_email);
                    break;
                case app.responses.invalid_awesome:
                    app.presentation.invalidInput(app.ids.apply_awesome, app.texts.invalid_awesome);
                    break;
                default:
                    app.presentation.ajaxError();
                    app.domain.invalidAjaxResult(response);
            }
        }});
    },
    createAccountFromBetaApplication: function(key, username, password, password2){
        $.ajax({type:'POST', url:'core/api.php', data: 'action=create_account_from_application&key=' + key + '&username=' + username + '&password=' + password + '&password2=' +password2, error: function(xhr, ajaxOptions, thrownError){
            app.presentation.ajaxError();
            app.domain.ajaxError(xhr, ajaxOptions, thrownError);
        },
        success: function(data){
            response = $.parseJSON(data);
            switch(response){
                case app.responses.success:
                    app.presentation.createAccFromBetaApplicationSuccess();
                    break;
                case app.responses.invalid_username:
                    app.presentation.invalidInput(app.ids.createacc_username, app.texts.invalid_username);
                    break;
                case app.responses.already_exists_username:
                    app.presentation.invalidInput(app.ids.createacc_username, app.texts.username_exists);
                    break;
                case app.responses.invalid_password:
                    app.presentation.invalidInput(app.ids.createacc_password, app.texts.invalid_password);
                    break;
                case app.responses.passwords_not_matching:
                    app.presentation.invalidInput(app.ids.createacc_password2, app.texts.passwords_not_matching);
                    break;
                default:
                    app.presentation.ajaxError();
                    app.domain.invalidAjaxResult(response);
            }
        }});
    },
    invalidAjaxResult: function(result){
        //Log
    },
    ajaxError: function(xhr, ajaxOptions, thrownError){
        //Log
    }
    
}

app.core = {
    init: function(){
        app.domain.init();
        app.presentation.init();
    }
}

$(document).ready(function(){
    app.core.init();
});