package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "7721580519:AAGOaaDUs5Ha7G13mMMU9WVqUbs7is4h7aY"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "gpt:n4QuBf69EdaWO7T77o0oJFkblB3Tof5yWFt5yj7DNhdQFq4K"; //TODO: añadir el token de ChatGPT entre comillas

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode;

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí

    public void iniciarcomando(){

        mode = DialogMode.MAIN;
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);

        showMainMenu(
                "start", "menú principal del bot",
                "profile", "generación de perfil de Tinder 😎",
                "opener", "mensaje para iniciar conversación 🥰",
                "message", "correspondencia en su nombre 😈",
                "date", "correspondencia con celebridades 🔥",
                "gpt", "hacer una pregunta a chat GPT 🧠"
        );

    }
    public void GPTcomando(){
        mode = DialogMode.GPT;

        String text = loadMessage("gpt");
        sendPhotoMessage("gpt");
        sendTextMessage(text);

    }
    public void GPTDiaLog(){

        String text = getMessageText();
        String prompt = loadPrompt("gpt");
        String answer = chatGPT.sendMessage(prompt, text);
        sendTextMessage(answer);

    }


    public void hola(){

        if (mode == DialogMode.GPT){
            GPTDiaLog();
        } else {

            String text = getMessageText();
            sendTextMessage("*Hola soy Viko, bienvenido a mi bot de Tinder*");
            sendTextMessage("_¿Quien eres?_");
            sendTextMessage("Tu eres:" + text);

            sendPhotoMessage("avatar_main");
            sendTextButtonsMessage("Launch process",
                    "Start", "Empezar", "Stop", "Detener");
        }
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
        addCommandHandler("start",this::iniciarcomando);
        addCommandHandler("gpt",this::GPTcomando);
        addMessageHandler(this::hola);
        addButtonHandler("^.*",this::HolaButton);

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
