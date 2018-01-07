/*
 * Copyright (C) 2018 iwantuss
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package matchmaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;


/**
 *
 * @author iwantuss
 */
public class MatchMaker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Генерируем трафик игроков
        Stream<Player> traffic = Stream.generate(()->
                new Player(""+(int)(1+(Math.random()*1000))+"-"+(int)(1000+(Math.random()*10000)),(int)(Math.random()*30))
        ).limit(1000*8);
        
        
        MatchMaker mm = new MatchMaker(8, traffic);
        
    }
    
    
    /**
     * Максимальное количество игроков в матче
     */
    private final int MAX_PLAYERS;   
    /**
     * Список матчей
     */
    private final List<Match> matches = new ArrayList<>();
    /**
     * Устанавливает максимальное количество игроков 
     * @param maxPlayer 
     * @param traffic 
     */
    public MatchMaker(int maxPlayer, Stream<Player> traffic){
        MAX_PLAYERS = maxPlayer;
        try {
            traffic.forEach((pl)-> {
                registrateNewPlayer(pl);
            /*/Задержка между регистрациями игроков от 100мс до 500мс
                try {
                    Thread.sleep((long)(100+(Math.random()*500)));
                } catch (InterruptedException ex) {
                    Logger.getLogger(MatchMaker.class.getName()).log(Level.SEVERE, null, ex);
                }
            /*/
            });
            //Игроки больше не будут добавляться
            throw new Exception("Игроки больше не будут заходить в игру...");
        } catch (Exception ex) {
            //Logger.getLogger(MatchMaker.class.getName()).log(Level.SEVERE, null, ex);
            endlessMatchmaking();
        }
    }
    
    /**
     * Регистрирует нового пользователя и решает
     * добавить ли его в существующий матч или
     * создать для него новый, потому что он не подходит условиям матча.
     * @param pl
     */
    private void  registrateNewPlayer(Player pl){
        if(!matches.parallelStream()
                .filter((match) -> {//Матч должен быть и не пустым, и не полным
                    if(match.size()==MAX_PLAYERS){
                        match.print();
                    }
                    return !match.isEmpty();
                }).anyMatch((match) -> {
                    return match.addPlayer(pl);
                }))
            matches.add(new Match(pl));
        Finisher();
    }
    
    
    /**
     * Метод объединяющий созданные матчи до тех пор, пока
     * список матчей не станет пустым
     */
    private void endlessMatchmaking() {
        while(matches.size()>1)
            for (int i = 1; i < matches.size(); i++) {
                //Производим цикл по игрокам i-ой команды
                while (!matches.get(i).isEmpty()) {
                    if(replacePlayer(matches.get(i).players.peek())){
                        matches.get(i).players.poll();
                    }
                    else {i--;break;}
                }
                
            Finisher();
            //Приостанавливаем поток, чтобы цикл не нагружал процессор
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(MatchMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    /**
     * Перемещает игрока в самый старый матч, которому он соответствует
     * @param pl перемещаемый игрок
     * @return true если игрок был перемещен в новый матч
     */
    private boolean replacePlayer(Player pl){        
        
        for (Iterator<Match> it = matches.iterator(); it.hasNext();) {
            
            Match match = it.next();
            if(match.size()==0){
                it.remove();
                return false;
            }
            if(match.size()==MAX_PLAYERS){
                match.print();
                return false;
            }
            return match.addPlayer(pl);
            
        }
        
        return false;
    }
    
    /**
     * Удаляет пустые матчи и выводит на экран заполненые
     */
    private boolean Finisher(){
        return matches.removeIf((match)-> {
                    if(match.size()==MAX_PLAYERS){
                        match.print();
                    }
                    return match.isEmpty();
        });
    }
}
