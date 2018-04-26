<?php
header('Content-Type: application/json; charset=utf-8');
require '../_app/Config.inc.php';

if(empty($Read)){
    $Read = new Read;
}

$ApiKey = filter_input(INPUT_GET, 'key', FILTER_DEFAULT);
$ApiToken = filter_input(INPUT_GET, 'token', FILTER_DEFAULT);
$Error = array();

if (!empty($ApiKey) && !empty($ApiToken)):
    $Read->ExeRead(DB_API, "WHERE api_key = :key AND api_token = :token AND api_status = 1", "key={$ApiKey}&token={$ApiToken}");
    if (!$Read->getResult()):
        $Error['error'] = "Acesso negado ao APP!";
        echo json_encode($Error);
    else:
        $ApiLoadUpdate = ['api_loads' => $Read->getResult()[0]['api_loads'] + 1, "api_lastload" => date('Y-m-d H:i:s')];
        $Update = new Update;
        $Update->ExeUpdate(DB_API, $ApiLoadUpdate, "WHERE api_id = :id", "id={$Read->getResult()[0]['api_id']}");
        $jSON = null;

        //VARS
        $Limit = (filter_input(INPUT_GET, 'limit', FILTER_VALIDATE_INT) ? filter_input(INPUT_GET, 'limit', FILTER_VALIDATE_INT) : 5);
        $Offset = (filter_input(INPUT_GET, 'offset', FILTER_VALIDATE_INT) ? filter_input(INPUT_GET, 'offset', FILTER_VALIDATE_INT) : 0);
        $Order = (filter_input(INPUT_GET, 'order', FILTER_VALIDATE_INT) ? filter_input(INPUT_GET, 'order', FILTER_VALIDATE_INT) : null);
        $By = (filter_input(INPUT_GET, 'by', FILTER_VALIDATE_INT) ? filter_input(INPUT_GET, 'order', FILTER_VALIDATE_INT) : 'post_date');

        switch ($By):
            case 1;
                $By = 'user_id';
                break;
            case 2;
                $By = 'user_email';
                break;
            default :
                $By = 'user_name';
        endswitch;

        //SET ORDER
        switch ($Order):
            case 1:
                $Order = "ORDER BY {$By} ASC";
                break;
            case 2:
                $Order = "ORDER BY {$By} DESC";
                break;
            case 3:
                $Order = "ORDER BY RAND()";
                break;
            default :
                $Order = "ORDER BY {$By} ASC";
        endswitch;

        $Read = new Read;
        $Read->ExeRead(DB_USERS, "{$Order} LIMIT :limit OFFSET :offset", "limit={$Limit}&offset={$Offset}");
        if ($Read->getResult()):
            foreach ($Read->getResult() as $REST):
            
                $jSON[] = $REST;
            endforeach;
            echo json_encode($jSON, JSON_PRETTY_PRINT);
        else:
            return false;
        endif;
    endif;
else:
    $Error['error'] = "Informar dados de acesso!";
    echo json_encode($Error, JSON_PRETTY_PRINT);
endif;