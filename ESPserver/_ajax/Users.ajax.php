<?php

require '../_app/Config.inc.php';

$jSON = null;
$CallBack = 'Users';
$getPost = filter_input_array(INPUT_POST, FILTER_DEFAULT);

//echo "getpost";
//var_dump($getPost);
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

    switch ($Action):
        case 'user_verify_email_pass':
            $Read->FullRead("SELECT user_email,user_password FROM users WHERE user_email = :email AND user_password = :pass", "email={$Post['user_email']},pass={$Post['user_password']}");
            if (!$Read->getResult()):
                $jSON = false;
            else:
                $jSON = true;
            endif;
            break;

        case 'user_verify_email':
            $Read->FullRead("SELECT user_email FROM users WHERE user_email = :email", "email={$Post['user_email']}");
            if (!$Read->getResult()):
                $jSON = false;
            else:
                $jSON = true;
            endif;
            break;

        case 'user_verify_address':
            $Read->FullRead("SELECT * FROM address WHERE user_id = :id", "id={$Post['user_id']}");
            if (!$Read->getResult()):
                break;
            else:
                $UserAddress = $Read->getResult()[0];
                $jSON = $UserAddress;
            endif;
            break;

        case 'user_create':
            //VERIFICA CAMPOS EM BRANCO
            if (in_array('', $Post)):
                $jSON = 0;
                break;
            endif;

            //VERIFICA E-MAIL
            $Read->FullRead("SELECT user_id FROM users WHERE user_email = :email", "email={$Post['user_email']}");
            if ($Read->getResult()):
                $jSON = 1;
                break;
            endif;

            $UserCreate = [
                'user_id_fb' => $Post['user_id_fb'],
                'user_cover' => $Post['user_cover'],
                'user_name' => $Post['user_name'],
                'user_lastname' => $Post['user_lastname'],
                'user_email' => $Post['user_email'],
                'user_password' => $Post['user_password'],
                'user_genre' => $Post['user_genre'],
                'user_datebirth' => Check::Nascimento($Post['user_datebirth']),
                'user_registration' => date(DATE_W3C)
            ];

            $Create->ExeCreate('users', $UserCreate);
            if ($Create->getResult()) {
                $jSON = $Create->getResult();
            } else {
                $jSON = 2;
            }
            break;

        case 'user_create_address':
            $UserAddress = [
                'user_id' => $Post['user_id'],
                'addr_state' => $Post['addr_state'],
                'addr_city' => $Post['addr_city'],
                'addr_street' => $Post['addr_street'],
                'addr_number' => $Post['addr_number']
            ];

            $Create->ExeCreate('address', $UserAddress);
            if ($Create->getResult()) {
                $jSON = $Create->getResult();
            } else {
                $jSON = "0";
            }
            break;

        case 'user_update_address':
            $UserAddress = [
                'user_id' => $Post['user_id'],
                'addr_state' => $Post['addr_state'],
                'addr_city' => $Post['addr_city'],
                'addr_street' => $Post['addr_street'],
                'addr_number' => $Post['addr_number']
            ];
            $Update->ExeUpdate('address', $UserAddress,"WHERE addr_id = :id","id={$Post['addr_id']}");
            if ($Update->getResult()) {
                $jSON = $Update->getResult();
            } else {
                $jSON = "0";
            }
            break;
            
        case 'user_get':
//            $Read->FullRead(
//                    "SELECT "
//                    . "u.*,"
//                    . "e.* "
//                    . "FROM users u "
//                    . "LEFT JOIN address e ON u.user_id = e.user_id "
//                    . "WHERE u.user_email = :user", "user={$Post['user_email']}"
//            );
                    $Read->FullRead("SELECT * FROM users WHERE user_email = :user", "user={$Post['user_email']}");
            if (!$Read->getResult()):
            else:
                $User = $Read->getResult()[0];
                $User['user_datebirth'] = date("d/m/Y", strtotime($User['user_datebirth']));

                $jSON = $User;
            endif;
            break;

        case 'user_update':
            $UserId = $Post['user_id'];
            $AddrId = $Post['addr_id'];

            //VERIFICA E-MAIL
            $Read->FullRead("SELECT user_id FROM users WHERE user_id != :id AND user_email = :email", "id={$UserId}&email={$Post['user_email']}");
            if ($Read->getResult()):
                $jSON['trigger'] = AjaxErro("<span class='icon-cross al_center ds_block'>Desculpe mas o e-mail {$Post['user_email']} já existe na base para outro usuários!</span>", E_USER_ERROR);
                break;
            endif;

            $UserUpdate = [
                'user_name' => $Post['user_name'],
                'user_lastname' => $Post['user_lastname'],
                'user_email' => $Post['user_email'],
                'user_genre' => $Post['user_genre'],
                'user_datebirth' => Check::Nascimento($Post['user_datebirth']),
                'user_registration' => date(DATE_W3C)
            ];
            $Update->ExeUpdate("users", $UserUpdate, "WHERE user_id = :user", "user={$UserId}");

            $UserAddress = [
                'user_id' => $UserId,
                'addr_state' => $Post['addr_state'],
                'addr_city' => $Post['addr_city'],
                'addr_street' => $Post['addr_street'],
                'addr_number' => $Post['addr_number']
            ];
            $Update->ExeUpdate("address", $UserAddress, "WHERE addr_id = :addr", "addr={$AddrId}");

            $jSON['trigger'] = AjaxErro("<span class='icon-checkmark al_center ds_block'>O usuário {$Post['user_name']} foi atualizado com sucesso!</span>");
            $jSON['content'] = ["#{$UserId}.wc_single_user", "<p class='row title'>{$Post['user_name']} {$Post['user_lastname']}</p>"
                . "<p class='row'>{$Post['user_email']}</p>"
                . "<p class='row'>{$Post['user_datebirth']}</p>"
                . "<p class='row'>{$Post['addr_street']}, {$Post['addr_number']}</p>"
                . "<p class='row'>{$Post['addr_city']}/{$Post['addr_state']}</p>"
                . "<p class='row al_right'>"
                . "<span class='btn btn_blue icon-pencil2 icon-notext wc_edit wc_tooltip' id='{$UserId}' data-c='Users' data-ca='user_get'><span class='wc_tooltip_balloon'>Editar Dados de {$Post['user_name']}!</span></span>"
                . "<span class='btn btn_red icon-cross icon-notext wc_delete wc_tooltip' id='{$UserId}' data-c='Users' data-ca='user_delete'><span class='wc_tooltip_balloon'>Deletar Conta de {$Post['user_name']}!</span></span>"
                . "</p>"];
            break;

        case 'user_delete':
            $Delete->ExeDelete("users", "WHERE user_id = :user", "user={$Post['user_id']}");
            $jSON['success'] = true;
            break;

        default :
            $jSON['trigger'] = AjaxErro("<span class='icon-cross al_center ds_block'>Uma ação não foi selecionada no formulário!</span>", E_USER_ERROR);
            break;
    endswitch;
else:
    $jSON['trigger'] = AjaxErro("<span class='icon-cross al_center ds_block'>Uma ação não foi selecionada no formulário!</span>", E_USER_ERROR);
endif;

//var_dump(json_encode($jSON));
echo json_encode($jSON);
