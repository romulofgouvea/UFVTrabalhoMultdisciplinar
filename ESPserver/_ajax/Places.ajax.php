<?php

require '../_app/Config.inc.php';

$jSON = null;
$CallBack = 'Places';
$getPost = filter_input_array(INPUT_POST, FILTER_DEFAULT);

//VALIDA AÇÃO
if ($getPost && $getPost['callback_action'] && $getPost['callback'] == $CallBack):
    //PREPARA OS DADOS
    $Post = array_map("strip_tags", $getPost);
    $Action = $Post['callback_action'];
    unset($Post['callback'], $Post['callback_action']);

    $Create = new Create;
    $Read = new Read;
    $Update = new Update;
    $Delete = new Delete;

    $Upload = new Upload('../uploads/');


    switch ($Action):
        case 'place_verify':
            $Read->FullRead("SELECT * FROM users WHERE place_email = :email", "email={$Post['place_email']}");
            if (!$Read->getResult()):
                break;
            else:
                $Place = $Read->getResult()[0];
                $Place['place_datebirth'] = date("d/m/Y", strtotime($Place['place_datebirth']));
//                $jSON = array('User' => $Read->getResult());
                $jSON = $Place;
            endif;
            break;
        case 'place_create':
            $Read->FullRead('SELECT count(place_user_id) as quantidade FROM ' . DB_PLACES . ' WHERE DATE(place_registration) = "' . date("Y-m-d") . '"');
           
            if ($Read->getResult()[0]['quantidade'] < 10):

                $PlaceId = $Post['place_id'];
                if (!empty($_FILES['place_thumb'])):
                    $PlaceThumb = $_FILES['place_thumb'];
                    $Read->FullRead("SELECT place_thumb FROM " . DB_PLACES . " WHERE place_id = :id", "id={$PlaceId}");
                    if ($Read->getResult()):
                        if (file_exists("../uploads/{$Read->getResult()[0]['place_thumb']}") && !is_dir("../uploads/{$Read->getResult()[0]['place_thumb']}")):
                            unlink("../uploads/{$Read->getResult()[0]['place_thumb']}");
                        endif;
                    endif;
                    $Upload->Image($PlaceThumb, $Post['place_user_id'] . "-" . Check::Name($Post['place_name']) . '-' . time(), 600, "places");
                    if ($Upload->getResult()):
                        $Post['place_thumb_result'] = $Upload->getResult();
                    else:
                        return;
                    endif;
                endif;

                $PlaceCreate = [
                    'place_user_id' => $Post['place_user_id'],
                    'place_thumb' => $Post['place_thumb_result'],
                    'place_name' => $Post['place_name'],
                    'place_description' => $Post['place_description'],
                    'place_latitude' => $Post['place_latitude'],
                    'place_longitude' => $Post['place_longitude'],
                    'place_status' => 0,
                    'place_registration' => date(DATE_W3C)
                ];

                $Create->ExeCreate('places', $PlaceCreate);
                if ($Create->getResult()) :
                    $jSON = $Create->getResult();
                endif;
            else:
                $jSON = 1111111111;
            endif;
            break;

        case 'place_get':
            $Read->FullRead(
                    "SELECT "
                    . "u.*,"
                    . "e.* "
                    . "FROM users u "
                    . "LEFT JOIN address e ON u.place_id = e.place_id "
                    . "WHERE u.place_id = :user", "user={$Post['place_id']}"
            );
            if (!$Read->getResult()):
            else:
                $Place = $Read->getResult()[0];
                $Place['place_datebirth'] = date("d/m/Y", strtotime($Place['place_datebirth']));

                $jSON = $Place;
            endif;
            break;

        case 'place_update':
            $PlaceId = $Post['place_id'];
            $AddrId = $Post['addr_id'];

            //VERIFICA E-MAIL
            $Read->FullRead("SELECT place_id FROM users WHERE place_id != :id AND place_email = :email", "id={$PlaceId}&email={$Post['place_email']}");
            if ($Read->getResult()):
//                $jSON['trigger'] = AjaxErro("<span class='icon-cross al_center ds_block'>Desculpe mas o e-mail {$Post['place_email']} já existe na base para outro usuários!</span>", E_USER_ERROR);
                break;
            endif;

            $PlaceUpdate = [
                'place_name' => $Post['place_name'],
                'place_lastname' => $Post['place_lastname'],
                'place_email' => $Post['place_email'],
                'place_genre' => $Post['place_genre'],
                'place_datebirth' => Check::Nascimento($Post['place_datebirth']),
                'place_registration' => date(DATE_W3C)
            ];
            $Update->ExeUpdate("users", $PlaceUpdate, "WHERE place_id = :user", "user={$PlaceId}");

            $PlaceAddress = [
                'place_id' => $PlaceId,
                'addr_state' => $Post['addr_state'],
                'addr_city' => $Post['addr_city'],
                'addr_street' => $Post['addr_street'],
                'addr_number' => $Post['addr_number']
            ];
            $Update->ExeUpdate("address", $PlaceAddress, "WHERE addr_id = :addr", "addr={$AddrId}");

//            $jSON['trigger'] = AjaxErro("<span class='icon-checkmark al_center ds_block'>O usuário {$Post['place_name']} foi atualizado com sucesso!</span>");
            $jSON['content'] = ["#{$PlaceId}.wc_single_user", "<p class='row title'>{$Post['place_name']} {$Post['place_lastname']}</p>"
                . "<p class='row'>{$Post['place_email']}</p>"
                . "<p class='row'>{$Post['place_datebirth']}</p>"
                . "<p class='row'>{$Post['addr_street']}, {$Post['addr_number']}</p>"
                . "<p class='row'>{$Post['addr_city']}/{$Post['addr_state']}</p>"
                . "<p class='row al_right'>"
                . "<span class='btn btn_blue icon-pencil2 icon-notext wc_edit wc_tooltip' id='{$PlaceId}' data-c='Users' data-ca='place_get'><span class='wc_tooltip_balloon'>Editar Dados de {$Post['place_name']}!</span></span>"
                . "<span class='btn btn_red icon-cross icon-notext wc_delete wc_tooltip' id='{$PlaceId}' data-c='Users' data-ca='place_delete'><span class='wc_tooltip_balloon'>Deletar Conta de {$Post['place_name']}!</span></span>"
                . "</p>"];
            break;

        case 'place_delete':
            $Delete->ExeDelete("places", "WHERE place_id = :place", "place={$Post['place_id']}");
            $jSON = true;
            break;

        default :
            $jSON = 999;
            break;
    endswitch;
else:
    $jSON = 9991;
endif;

echo json_encode($jSON);
