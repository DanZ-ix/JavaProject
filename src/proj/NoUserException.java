package proj;

class NoUserException extends Exception{

    public NoUserException(){
        super("Нет такого пользователя");
    }
}