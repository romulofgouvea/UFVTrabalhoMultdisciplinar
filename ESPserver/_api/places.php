<?php

header('Content-Type: application/json; charset=utf-8');

require '../_app/Config.inc.php';

if (empty($Read)) {
    $Read = new Read;
}

$getPost = filter_input_array(INPUT_POST, FILTER_DEFAULT);

//VALIDA AÇÃO
if ($getPost && $getPost['key'] && $getPost['token']):

    $ApiKey = $getPost['key'];
    $ApiToken = $getPost['token'];
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
            $jSON = array();
            $QuerySQL = "";

            //VARS
            $QuerySearch = (isset($getPost['query_search']) ? $getPost['query_search'] : null);
            $Limit = (isset($getPost['limit']) ? $getPost['limit'] : 30);
            $Offset = (isset($getPost['offset']) ? $getPost['offset'] : 0);
            $Order = (isset($getPost['order']) ? $getPost['order'] : null);
            $By = (isset($getPost['by']) ? $getPost['by'] : 'place_date');

            switch ($By):
                case 2;
                    $By = 'place_views';
                    break;
                default :
                    $By = 'place_registration';
            endswitch;

            if ($QuerySearch != NULL):
                $QuerySQL = "AND place_name LIKE '%{$QuerySearch}%' OR place_description LIKE '%{$QuerySearch}%'";
            endif;

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
                    $Order = "ORDER BY {$By} DESC";
            endswitch;

            $Read = new Read;
            $Read->ExeRead(DB_PLACES, "WHERE place_status = 1 {$QuerySQL} {$Order} LIMIT :limit OFFSET :offset", "limit={$Limit}&offset={$Offset}");
            if ($Read->getResult()):
                foreach ($Read->getResult() as $REST):
                    unset($REST['place_status']);
                    $jSON[] = $REST;
                endforeach;

                echo json_encode($jSON, JSON_PRETTY_PRINT);
            else:
                $jSON[] = "ERRO";
                return false;
            endif;
        endif;
    else:
        $Error['error'] = "Informar dados de acesso!";
        echo json_encode($Error, JSON_PRETTY_PRINT);
    endif;
endif;