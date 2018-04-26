<?php

if (!$DefineConf):
    /*
     * URL DO SISTEMA
     */
    define('BASE', 'http://w2fcreative.esy.es/ESPserver'); //Url rais do site
endif;

if (!$DefineConf):
    /*
     * VARIAVEIS DO SISTEMA
     */
    define('LEVEL_USERS', 6);
    define('LEVEL_CONFIG_API', 10);
   
    
    /*
     * MEDIA CONFIG
     */
    define('IMAGE_W', 1600); //Tamanho da imagem (WIDTH)
    define('IMAGE_H', 800); //Tamanho da imagem (HEIGHT)
    define('THUMB_W', 800); //Tamanho da miniatura (WIDTH) PDTS
    define('THUMB_H', 1000); //Tamanho da minuatura (HEIGHT) PDTS
    define('AVATAR_W', 500); //Tamanho da miniatura (WIDTH) USERS
    define('AVATAR_H', 500); //Tamanho da minuatura (HEIGHT) USERS
    define('SLIDE_W', 1920); //Tamanho da miniatura (WIDTH) SLIDE
    define('SLIDE_H', 600); //Tamanho da minuatura (HEIGHT) SLIDE
endif;