<?php

/*
 * BANCO DE DADOS
 */
define('SIS_DB_HOST', 'mysql.hostinger.com.br'); //Link do banco de dados
define('SIS_DB_USER', 'u511772499_rf'); //Usuário do banco de dados
define('SIS_DB_PASS', 'romulo00'); //Senha  do banco de dados
define('SIS_DB_DBSA', 'u511772499_rf'); //Nome  do banco de dados


/*
 * TABELAS DO BANCO DE DADOS
 */
define('DB_USERS', 'users');
define('DB_USERS_ADDRESS', 'address');
define('DB_PLACES', 'places');
define('DB_API', 'espplay_api');

/*
  AUTO LOAD DE CLASSES
 */
function MyAutoLoad($Class) {
    $cDir = ['Conn', 'Helpers', 'Models'];
    $iDir = null;

    foreach ($cDir as $dirName):
        if (!$iDir && file_exists(__DIR__ . '/' . $dirName . '/' . $Class . '.class.php') && !is_dir(__DIR__ . '/' . $dirName . '/' . $Class . '.class.php')):
            include_once (__DIR__ . '/' . $dirName . '/' . $Class . '.class.php');
            $iDir = true;
        endif;
    endforeach;
}

spl_autoload_register("MyAutoLoad");

$DefineConf = null;

/*
 * Exibe erros lançados
 */
function Erro($ErrMsg, $ErrNo = null) {
    $CssClass = ($ErrNo == E_USER_NOTICE ? 'trigger_info' : ($ErrNo == E_USER_WARNING ? 'trigger_alert' : ($ErrNo == E_USER_ERROR ? 'trigger_error' : 'trigger_success')));
    echo "<div class='trigger {$CssClass}'>{$ErrMsg}<span class='ajax_close'></span></div>";
}

/*
 * Exibe erros lançados por ajax
 */
function AjaxErro($ErrMsg, $ErrNo = null) {
    $CssClass = ($ErrNo == E_USER_NOTICE ? 'trigger_info' : ($ErrNo == E_USER_WARNING ? 'trigger_alert' : ($ErrNo == E_USER_ERROR ? 'trigger_error' : 'trigger_success')));
    return "<div class='trigger trigger_ajax {$CssClass}'>{$ErrMsg}<span class='ajax_close'></span></div>";
}

/*
 * personaliza o gatilho do PHP
 */
function PHPErro($ErrNo, $ErrMsg, $ErrFile, $ErrLine) {
    echo "<div class='trigger trigger_error'>";
    echo "<b>Erro na Linha: #{$ErrLine} ::</b> {$ErrMsg}<br>";
    echo "<small>{$ErrFile}</small>";
    echo "<span class='ajax_close'></span></div>";

    if ($ErrNo == E_USER_ERROR):
        die;
    endif;
}

set_error_handler('PHPErro');
