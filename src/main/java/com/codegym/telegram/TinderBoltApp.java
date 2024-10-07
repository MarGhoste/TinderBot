package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "7721580519:AAGOaaDUs5Ha7G13mMMU9WVqUbs7is4h7aY"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "chat-gpt-token"; //TODO: añadir el token de ChatGPT entre comillas

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí

    public void iniciarcomando(){
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);
    }

    public void hola(){

        String text = getMessageText();
        sendTextMessage("*Hola soy Viko, bienvenido a mi bot de Tinder*");
        sendTextMessage("_¿Quien eres?_");
        sendTextMessage("Tu eres:" + text);

        sendPhotoMessage("avatar_main");
        sendTextButtonsMessage("Launch process",
                "Start", "Empezar","Stop","Detener");

    }

    public void HolaButton(){
        String key = getButtonKey();
        if (key.equals("Start")){
            sendTextMessage("Empezando");
        }else {
            sendTextMessage("Deteniendo");
        }

    }
    @Override
    public void onInitialize() {
        //TODO: y un poco más aquí :)
        addCommandHandler("Iniciar",this::iniciarcomando);
        addMessageHandler(this::hola);
        addButtonHandler("^.*",this::HolaButton);

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
